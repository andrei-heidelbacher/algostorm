/*
 * Copyright (c) 2017  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.andreihh.algostorm.android

import android.app.Activity
import android.app.LoaderManager.LoaderCallbacks
import android.content.AsyncTaskLoader
import android.content.Context
import android.content.Intent
import android.content.Loader
import android.os.Bundle
import android.view.SurfaceView

abstract class ClientActivity : Activity(), LoaderCallbacks<EngineHolder> {
    companion object {
        protected inline fun <reified T : ClientActivity> start(
                context: Context,
                args: Bundle
        ) {
            val intent = Intent(context, T::class.java)
            intent.putExtras(args)
            context.startActivity(intent)
        }
    }

    private class EngineLoader(
            context: Context,
            bundle: Bundle?
    ) : AsyncTaskLoader<EngineHolder>(context) {
        private val args = bundle ?: Bundle.EMPTY
        private var engineHolder: EngineHolder? = null

        override fun onStartLoading() {
            val holder = engineHolder
            if (holder != null) {
                deliverResult(holder)
            } else {
                forceLoad()
            }
        }

        override fun loadInBackground(): EngineHolder {
            val data = EngineHolder(context)
            data.init(args)
            return data
        }

        override fun deliverResult(data: EngineHolder) {
            engineHolder = data
            super.deliverResult(data)
        }

        override fun onReset() {
            cancelLoad()
        }

        override fun onCanceled(data: EngineHolder?) {
            data?.release()
            engineHolder = null
        }
    }

    protected abstract val splashLayoutId: Int
    protected abstract val clientLayoutId: Int
    protected abstract val surfaceViewId: Int

    private var engineHolder: EngineHolder? = null
    private var isRunning = false

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<EngineHolder> =
            EngineLoader(context = this, bundle = args ?: intent.extras)

    override fun onLoadFinished(
            loader: Loader<EngineHolder>,
            data: EngineHolder
    ) {
        engineHolder = data
        setContentView(clientLayoutId)
        val surfaceView = findViewById(surfaceViewId) as SurfaceView
        data.attachSurface(surfaceView)
        if (isRunning) {
            data.start()
        }
    }

    override fun onLoaderReset(loader: Loader<EngineHolder>?) {
        engineHolder = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(splashLayoutId)
        loaderManager.initLoader(0, savedInstanceState, this)
    }

    override fun onStart() {
        isRunning = true
        engineHolder?.start()
        super.onStart()
    }

    override fun onStop() {
        engineHolder?.stop()
        isRunning = false
        super.onStop()
    }

    override fun onDestroy() {
        engineHolder?.detachSurface()
        engineHolder = null
        super.onDestroy()
    }
}
