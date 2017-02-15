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

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build

import com.aheidelbacher.algostorm.core.engine.audio.AudioDriver
import com.aheidelbacher.algostorm.core.engine.driver.Resource

class AndroidAudioDriver(context: Context) : AudioDriver {
    private data class Resources(
            val context: Context,
            var mediaPlayer: MediaPlayer? = null,
            var musicVolume: Float = 1F,
            val soundPool: SoundPool =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                SoundPool.Builder().setMaxStreams(3).build()
            else
                SoundPool(3, AudioManager.STREAM_MUSIC, 0),
            val soundIds: MutableMap<String, Int> = hashMapOf(),
            var soundVolume: Float = 1F
    )

    private var resources: Resources? = Resources(context = context)

    private val Resource.fdPath: String
        get() = uri.replace('/', '_')

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

    override fun loadMusic(resource: Resource) {
        resources?.apply {
            installResource(resource)
        }
    }

    override fun loadSound(resource: Resource) {
        resources?.apply {
            installResource(resource)
            val path = resource.fdPath
            soundIds[path] = soundPool.load(resource.fdPath, 1)
        }
    }

    override fun setMusicVolume(volume: Float) {
        require(volume in 0F..1F) { "Invalid music volume $volume!" }
        resources?.apply {
            musicVolume = volume
            mediaPlayer?.setVolume(volume, volume)
        }
    }

    override fun setSoundVolume(volume: Float) {
        require(volume in 0F..1F) { "Invalid sound volume $volume!" }
        resources?.apply {
            soundVolume = volume
        }
    }

    override fun pauseMusic() {
        resources?.mediaPlayer?.pause()
    }

    override fun pauseSounds() {
        resources?.soundPool?.autoPause()
    }

    override fun playMusic(resource: Resource, loop: Boolean) {
        resources?.apply {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(resource.fdPath)
                prepare()
                isLooping = loop
                setVolume(musicVolume, musicVolume)
                start()
            }
        }
    }

    override fun playSound(resource: Resource) {
        resources?.apply {
            val id = requireNotNull(soundIds[resource.fdPath])
            soundPool.play(id, soundVolume, soundVolume, 1, 0, 1F) - 1
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
