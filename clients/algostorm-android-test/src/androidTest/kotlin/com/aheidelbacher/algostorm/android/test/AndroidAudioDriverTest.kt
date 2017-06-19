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

package com.aheidelbacher.algostorm.android.test

import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager
import android.support.test.InstrumentationRegistry

import com.aheidelbacher.algostorm.android.AndroidAudioDriver
import com.aheidelbacher.algostorm.core.drivers.io.InvalidResourceException
import com.aheidelbacher.algostorm.core.drivers.io.Resource.Companion.resourceOf

import org.junit.After
import org.junit.Before
import org.junit.Test

import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AndroidAudioDriverTest {
    companion object {
        private const val TIMEOUT = 1000L
        private const val STEP = 100L
    }

    private val context = InstrumentationRegistry.getTargetContext()
    private val audioDriver = AndroidAudioDriver(context)
    private val audioManager = context.getSystemService(AUDIO_SERVICE)
            as AudioManager

    private val music = resourceOf("music.mp3")
    private val sounds = setOf(
            resourceOf("sound-1.wav"),
            resourceOf("sound-2.wav")
    )

    private fun assertAudioIsActive() {
        var steps = TIMEOUT / STEP
        while (!audioManager.isMusicActive && steps > 0) {
            Thread.sleep(STEP)
            steps--
        }
        assertTrue(audioManager.isMusicActive)
    }

    private fun assertAudioIsNotActive() {
        var steps = TIMEOUT / STEP
        while (audioManager.isMusicActive && steps > 0) {
            Thread.sleep(STEP)
            steps--
        }
        assertFalse(audioManager.isMusicActive)
    }

    @Before fun loadResources() {
        audioDriver.loadMusic(music)
        sounds.forEach(audioDriver::loadSound)
        assertAudioIsNotActive()
    }

    @Test fun testLoadNonExistingMusicThrows() {
        val path = "non-existing.mp3"
        val resource = resourceOf(path)
        assertFailsWith<InvalidResourceException> {
            audioDriver.loadMusic(resource)
        }
    }

    @Test fun testLoadInvalidMusicThrows() {
        val path = "tileset.mp3"
        val resource = resourceOf(path)
        assertFailsWith<InvalidResourceException> {
            audioDriver.loadMusic(resource)
        }
    }

    @Test fun testPlayMusic() {
        audioDriver.playMusic(music)
        assertAudioIsActive()
    }

    @Test fun testPauseMusic() {
        audioDriver.playMusic(music)
        audioDriver.pauseMusic()
        assertAudioIsNotActive()
    }

    @Test fun testResumeMusic() {
        audioDriver.playMusic(music)
        audioDriver.pauseMusic()
        audioDriver.resumeMusic()
        assertAudioIsActive()
    }

    @Test fun testStopMusic() {
        audioDriver.playMusic(music)
        audioDriver.stopMusic()
        assertAudioIsNotActive()
    }

    @Test fun testLoadNonExistingSoundThrows() {
        val path = "non-existing.mp3"
        val resource = resourceOf(path)
        assertFailsWith<InvalidResourceException> {
            audioDriver.loadSound(resource)
        }
    }

    @Test fun testLoadInvalidSoundThrows() {
        val path = "tileset.mp3"
        val resource = resourceOf(path)
        assertFailsWith<InvalidResourceException> {
            audioDriver.loadSound(resource)
        }
    }

    @Test fun testPlaySounds() {
        sounds.forEach(audioDriver::playSound)
        assertAudioIsActive()
    }

    @Test fun testPauseSounds() {
        sounds.forEach(audioDriver::playSound)
        audioDriver.pauseSounds()
        assertAudioIsNotActive()
    }

    @Test fun testResumeSounds() {
        sounds.forEach(audioDriver::playSound)
        audioDriver.pauseSounds()
        audioDriver.resumeSounds()
        assertAudioIsActive()
    }

    @Test fun testStopSounds() {
        sounds.forEach(audioDriver::playSound)
        audioDriver.stopSounds()
        assertAudioIsNotActive()
    }

    @After fun release() {
        audioDriver.release()
        assertAudioIsNotActive()
    }
}
