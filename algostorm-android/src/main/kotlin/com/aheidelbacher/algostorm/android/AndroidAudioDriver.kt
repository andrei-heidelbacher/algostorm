/*
 * Copyright 2016 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool

import com.aheidelbacher.algostorm.engine.audio.AudioDriver
import com.aheidelbacher.algostorm.engine.driver.Resource

class AndroidAudioDriver(context: Context) : AudioDriver {
    private data class Resources(
            val context: Context,
            var mediaPlayer: MediaPlayer? = null,
            val soundPool: SoundPool = SoundPool(3, AudioManager.STREAM_MUSIC, 0),
            val soundIds: MutableMap<String, Int> = hashMapOf()
    )

    private var resources: Resources? = Resources(context = context)

    private val Resource.fdPath: String
        get() = path.replace('/', '_')

    private fun Resources.installResource(resource: Resource) {
        val path = resource.fdPath
        if (path !in context.fileList()) {
            resource.inputStream().use { src ->
                context.openFileOutput(path, MODE_PRIVATE).use { dst ->
                    src.copyTo(out = dst)
                }
            }
        }
    }

    override fun loadMusic(musicSource: Resource) {
        resources?.apply {
            installResource(musicSource)
        }
    }

    override fun loadSound(soundSource: Resource) {
        resources?.apply {
            installResource(soundSource)
            val path = soundSource.fdPath
            soundIds[path] = soundPool.load(soundSource.fdPath, 1)
        }
    }

    override fun pauseMusic() {
        resources?.mediaPlayer?.pause()
    }

    override fun pauseSounds() {
        resources?.soundPool?.autoPause()
    }

    override fun playMusic(musicSource: Resource, loop: Boolean) {
        resources?.apply {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(musicSource.fdPath)
                prepare()
                isLooping = loop
                setVolume(1F, 1F)
                start()
            }
        }
    }

    override fun playSound(soundSource: Resource) {
        resources?.apply {
            val id = requireNotNull(soundIds[soundSource.fdPath])
            soundPool.play(id, 1F, 1F, 1, 0, 1F) - 1
        }
    }

    override fun resumeMusic() {
        resources?.mediaPlayer?.start()
    }

    override fun resumeSounds() {
        resources?.soundPool?.autoResume()
    }

    override fun stopMusic() {
        resources?.apply {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    override fun stopSounds() {
        //resources?.soundPool?.stop(0)
    }

    override fun release() {
        resources?.apply {
            mediaPlayer?.release()
            soundPool.release()
        }
        resources = null
    }
}
