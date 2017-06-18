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
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer

import com.aheidelbacher.algostorm.core.drivers.io.Resource
import com.aheidelbacher.algostorm.core.drivers.client.audio.AudioDriver
import com.aheidelbacher.algostorm.core.drivers.io.InvalidResourceException

import java.io.IOException

class AndroidAudioDriver(context: Context) : AudioDriver {
    private var context: Context? = context
    private val musicPool = hashMapOf<Resource, AssetFileDescriptor>()
    private var musicPlayer: MediaPlayer? = null
    private var musicVolume: Float = 1F
    private val soundPool: MutableMap<Resource, AssetFileDescriptor> = hashMapOf()
    private val soundPlayers: MutableSet<MediaPlayer> = hashSetOf()
    private var soundVolume: Float = 1F

    private fun Context.openAssetFd(resource: Resource): AssetFileDescriptor =
            try {
                assets.openFd(resource.path)
            } catch (e: IOException) {
                throw InvalidResourceException(e)
            }

    private fun createMediaPlayer(
            assetFd: AssetFileDescriptor,
            volume: Float,
            loop: Boolean
    ): MediaPlayer = MediaPlayer().apply {
        with(assetFd) {
            setDataSource(fileDescriptor, startOffset, declaredLength)
        }
        prepare()
        isLooping = loop
        setVolume(volume, volume)
    }

    override fun loadMusic(resource: Resource) {
        checkNotNull(context).apply {
            musicPool[resource] = openAssetFd(resource)
        }
    }

    override fun loadSound(resource: Resource) {
        checkNotNull(context).apply {
            soundPool[resource] = openAssetFd(resource)
        }
    }

    override fun setMusicVolume(volume: Float) {
        require(volume in 0F..1F) { "Invalid music volume $volume!" }
        checkNotNull(context)
        musicVolume = volume
        musicPlayer?.setVolume(volume, volume)
    }

    override fun setSoundVolume(volume: Float) {
        require(volume in 0F..1F) { "Invalid sound volume $volume!" }
        checkNotNull(context)
        soundVolume = volume
        soundPlayers.forEach { it.setVolume(volume, volume) }
    }

    override fun pauseMusic() {
        checkNotNull(context)
        musicPlayer?.pause()
    }

    override fun pauseSounds() {
        checkNotNull(context)
        soundPlayers.forEach(MediaPlayer::pause)
    }

    override fun playMusic(resource: Resource, loop: Boolean) {
        checkNotNull(context)
        val assetFd = requireNotNull(musicPool[resource])
        musicPlayer?.release()
        val player = createMediaPlayer(assetFd, musicVolume, loop)
        player.setOnCompletionListener {
            it.release()
            musicPlayer = null
        }
        musicPlayer = player
        player.start()
    }

    override fun playSound(resource: Resource) {
        checkNotNull(context)
        val assetFd = requireNotNull(soundPool[resource])
        val player = createMediaPlayer(assetFd, soundVolume, false)
        soundPlayers += player
        player.setOnCompletionListener {
            it.release()
            soundPlayers -= it
        }
        player.start()
    }

    override fun resumeMusic() {
        checkNotNull(context)
        musicPlayer?.start()
    }

    override fun resumeSounds() {
        checkNotNull(context)
        soundPlayers.forEach(MediaPlayer::start)
    }

    override fun stopMusic() {
        checkNotNull(context)
        musicPlayer?.release()
        musicPlayer = null
    }

    override fun stopSounds() {
        checkNotNull(context)
        soundPlayers.forEach(MediaPlayer::release)
        soundPlayers.clear()
    }

    override fun release() {
        if (context != null) {
            stopMusic()
            stopSounds()
            musicPool.clear()
            soundPool.clear()
            context = null
        }
    }
}
