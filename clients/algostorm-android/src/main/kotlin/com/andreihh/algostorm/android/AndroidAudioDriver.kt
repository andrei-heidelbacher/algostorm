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

package com.andreihh.algostorm.android

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import com.andreihh.algostorm.core.drivers.audio.AudioDriver
import com.andreihh.algostorm.core.drivers.audio.AudioStream
import com.andreihh.algostorm.core.drivers.io.InvalidResourceException
import com.andreihh.algostorm.core.drivers.io.Resource
import java.io.IOException

class AndroidAudioDriver(private val context: Context) : AudioDriver {
    private val sounds = hashMapOf<Resource<AudioStream>, AssetFileDescriptor>()
    private var musicPlayer: MediaPlayer? = null
    private var musicVolume = 1F
    private val soundPlayers = hashSetOf<MediaPlayer>()
    private var soundVolume = 1F

    private fun openAssetFd(
        resource: Resource<AudioStream>
    ): AssetFileDescriptor = try {
        context.assets.openFd(resource.path)
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

    override fun loadAudioStream(resource: Resource<AudioStream>) {
        sounds[resource] = openAssetFd(resource)
    }

    override fun setMusicVolume(volume: Float) {
        require(volume in 0F..1F) { "Invalid volume '$volume'!" }
        musicVolume = volume
        musicPlayer?.setVolume(volume, volume)
    }

    override fun setSoundVolume(volume: Float) {
        require(volume in 0F..1F) { "Invalid volume '$volume'!" }
        soundVolume = volume
        soundPlayers.forEach { it.setVolume(volume, volume) }
    }

    override fun pauseMusic() {
        musicPlayer?.pause()
    }

    override fun pauseSounds() {
        soundPlayers.forEach(MediaPlayer::pause)
    }

    override fun playMusic(music: Resource<AudioStream>, loop: Boolean) {
        val assetFd = requireNotNull(sounds[music]) { "'$music' not loaded!" }
        musicPlayer?.release()
        val player = createMediaPlayer(assetFd, musicVolume, loop)
        player.setOnCompletionListener {
            it.release()
            musicPlayer = null
        }
        musicPlayer = player
        player.start()
    }

    override fun playSound(sound: Resource<AudioStream>) {
        val assetFd = requireNotNull(sounds[sound]) { "'$sound' not loaded!" }
        val player = createMediaPlayer(assetFd, soundVolume, false)
        soundPlayers += player
        player.setOnCompletionListener {
            it.release()
            soundPlayers -= it
        }
        player.start()
    }

    override fun resumeMusic() {
        musicPlayer?.start()
    }

    override fun resumeSounds() {
        soundPlayers.forEach(MediaPlayer::start)
    }

    override fun stopMusic() {
        musicPlayer?.release()
        musicPlayer = null
    }

    override fun stopSounds() {
        soundPlayers.forEach(MediaPlayer::release)
        soundPlayers.clear()
    }

    override fun release() {
        stopMusic()
        stopSounds()
        sounds.clear()
    }
}
