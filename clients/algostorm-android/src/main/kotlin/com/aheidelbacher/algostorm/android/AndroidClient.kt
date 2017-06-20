/*
 * Copyright 2017 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
import android.os.AsyncTask
import android.os.Bundle
import android.view.SurfaceView
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT

import com.aheidelbacher.algostorm.core.drivers.client.audio.AudioDriver
import com.aheidelbacher.algostorm.core.drivers.client.graphics2d.GraphicsDriver
import com.aheidelbacher.algostorm.core.drivers.client.input.InputDriver
import com.aheidelbacher.algostorm.core.drivers.io.FileSystemDriver
import com.aheidelbacher.algostorm.core.engine.Engine

import java.io.InputStream

abstract class AndroidClient : Activity() {
    companion object {
        const val EXTRA_SAVE_FILE_NAME =
                "com.aheidelbacher.algostorm.android.test.SAVE_FILE_NAME"

        protected inline fun <reified T : AndroidClient> start(
                context: Context,
                saveFileName: String
        ) {
            val intent = Intent(context, T::class.java)
            intent.putExtra(EXTRA_SAVE_FILE_NAME, saveFileName)
            context.startActivity(intent)
        }
    }

    private inner class InitEngine : AsyncTask<InputStream?, Int, Unit>() {
        override fun doInBackground(vararg params: InputStream?) {
            val src = params.first()
            engine.init(src)
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            setContentView(contentLayoutId)
            (findViewById(surfaceViewContainerLayoutId) as ViewGroup)
                    .addView(surfaceView)
            isInitialized = true
            if (isActivityRunning) {
                engine.start()
            }
        }
    }

    private lateinit var saveFileName: String
    private lateinit var surfaceView: SurfaceView
    private lateinit var audioDriver: AndroidAudioDriver
    private lateinit var graphicsDriver: AndroidGraphicsDriver
    private lateinit var inputDriver: AndroidInputDriver
    private lateinit var fileSystemDriver: AndroidFileSystemDriver
    private lateinit var engine: Engine

    private var isInitialized = false
    private var isActivityRunning = false

    protected abstract val splashLayoutId: Int

    protected abstract val contentLayoutId: Int

    protected abstract val surfaceViewContainerLayoutId: Int

    protected abstract fun createEngine(
            audioDriver: AudioDriver,
            graphicsDriver: GraphicsDriver,
            inputDriver: InputDriver,
            fileSystemDriver: FileSystemDriver
    ): Engine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(splashLayoutId)
        surfaceView = SurfaceView(this).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }
        saveFileName = savedInstanceState?.getString(EXTRA_SAVE_FILE_NAME)
                ?: intent.getStringExtra(EXTRA_SAVE_FILE_NAME)
                ?: "autosave.json"
        val density = resources.displayMetrics.density
        audioDriver = AndroidAudioDriver(this)
        graphicsDriver = AndroidGraphicsDriver(this, surfaceView.holder)
        inputDriver = AndroidInputDriver(this, density)
        val src = savedInstanceState?.let { openFileInput(saveFileName) }
        engine = createEngine(
                audioDriver = audioDriver,
                graphicsDriver = graphicsDriver,
                inputDriver = inputDriver,
                fileSystemDriver = fileSystemDriver
        )
        surfaceView.setOnTouchListener(inputDriver)

        InitEngine().execute(src)
    }

    override fun onResume() {
        super.onResume()
        isActivityRunning = true
        if (isInitialized) {
            engine.start()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EXTRA_SAVE_FILE_NAME, saveFileName)
        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        isActivityRunning = false
        if (isInitialized) {
            engine.stop(1000)
        }
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
