/*
 * Copyright (c) 2016  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aheidelbacher.algostorm.android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.SurfaceView
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT

import com.aheidelbacher.algostorm.engine.Engine
import com.aheidelbacher.algostorm.engine.audio.AudioDriver
import com.aheidelbacher.algostorm.engine.graphics2d.GraphicsDriver
import com.aheidelbacher.algostorm.engine.input.InputDriver

abstract class EngineActivity : Activity() {
    companion object {
        const val EXTRA_SAVE_FILE_NAME =
                "com.aheidelbacher.algostorm.android.test.SAVE_FILE_NAME"

        protected inline fun <reified T : EngineActivity> start(
                context: Context,
                saveFileName: String
        ) {
            val intent = Intent(context, T::class.java)
            intent.putExtra(EXTRA_SAVE_FILE_NAME, saveFileName)
            context.startActivity(intent)
        }
    }

    private lateinit var saveFileName: String
    private lateinit var surfaceView: SurfaceView
    protected lateinit var audioDriver: AndroidAudioDriver
        private set
    protected lateinit var graphicsDriver: AndroidGraphicsDriver
        private set
    protected lateinit var inputDriver: AndroidInputDriver
        private set
    private lateinit var engine: Engine

    protected abstract val layoutResourceId: Int
    protected abstract val surfaceViewContainerId: Int

    protected abstract fun createEngine(
            audioDriver: AudioDriver,
            graphicsDriver: GraphicsDriver,
            inputDriver: InputDriver
    ): Engine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResourceId)
        surfaceView = SurfaceView(this).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }
        (findViewById(surfaceViewContainerId) as ViewGroup).addView(surfaceView)
        saveFileName = savedInstanceState?.getString(EXTRA_SAVE_FILE_NAME)
                ?: intent.getStringExtra(EXTRA_SAVE_FILE_NAME)
                ?: "autosave.json"
        val density = resources.displayMetrics.density
        audioDriver = AndroidAudioDriver(this)
        graphicsDriver = AndroidGraphicsDriver(surfaceView.holder, density * 2)
        inputDriver = AndroidInputDriver(density * 2)
        engine = createEngine(audioDriver, graphicsDriver, inputDriver)
        surfaceView.setOnTouchListener(inputDriver)
    }

    override fun onResume() {
        super.onResume()
        engine.start()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EXTRA_SAVE_FILE_NAME, saveFileName)
        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        engine.stop()
        openFileOutput(saveFileName, Context.MODE_PRIVATE).use {
            engine.serializeState(it)
        }
    }

    override fun onDestroy() {
        surfaceView.setOnTouchListener(null)
        engine.shutdown()
        super.onDestroy()
    }
}
