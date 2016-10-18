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

package com.aheidelbacher.algostorm.android.audio

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool

import com.aheidelbacher.algostorm.engine.audio.AudioDriver

/**
 * Android implementation of an audio driver.
 *
 * All music and sound resources are loaded relative to `/assets`.
 */
class AndroidAudioDriver(private val assetManager: AssetManager) : AudioDriver {
    private var mediaPlayer: MediaPlayer? = null
    private val soundPool = SoundPool(3, AudioManager.STREAM_MUSIC, 0)
    private var isReleased = false
    private val musicResources = hashMapOf<String, AssetFileDescriptor>()
    private val sounds = hashMapOf<String, Int>()
    private val ids = hashMapOf<Int, String>()

    fun autoResume() {
        mediaPlayer?.start()
        soundPool.autoResume()
    }

    fun autoPause() {
        mediaPlayer?.pause()
        soundPool.autoPause()
    }

    override fun loadMusic(musicSource: String) {
        musicResources[musicSource] = assetManager.openFd(musicSource.drop(1))
    }

    override fun loadSound(soundSource: String) {
        check(!isReleased) { "Can't load sound after manager is released!" }
        val id = soundPool.load(assetManager.openFd(soundSource.drop(1)), 1)
        sounds[soundSource] = id
        ids[id] = soundSource
    }

    override fun playMusic(musicSource: String, loop: Boolean) {
        check(!isReleased) { "Can't play sound after manager is released!" }
        val afd = musicResources[musicSource] ?: error("")
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            prepare()
            isLooping = loop
            setVolume(1F, 1F)
            start()
        }
    }

    override fun pauseMusic() {
        mediaPlayer?.pause()
    }

    override fun resumeMusic() {
        mediaPlayer?.start()
    }

    override fun stopMusic() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun playSound(soundSource: String): Int {
        check(!isReleased) { "Can't play sound after manager is released!" }
        return soundPool.play(
                requireNotNull(sounds[soundSource]) {
                    "Invalid sound $soundSource!"
                },
                1.0F,
                1.0F,
                1,
                0,
                1.0F
        ) - 1
    }

    override fun pauseStream(streamId: Int) {
        soundPool.pause(streamId)
    }

    override fun resumeStream(streamId: Int) {
        soundPool.resume(streamId)
    }

    override fun stopStream(streamId: Int) {
        check(!isReleased) { "Cant play sound after manager is released!" }
        soundPool.stop(streamId + 1)
    }

    override fun release() {
        check(!isReleased) { "Cant release sound manager multiple times!" }
        isReleased = true
        soundPool.release()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
