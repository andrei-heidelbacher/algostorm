package com.aheidelbacher.algostorm.test.engine.audio

import com.aheidelbacher.algostorm.engine.audio.AudioDriver
import com.aheidelbacher.algostorm.engine.driver.Resource

class AudioDriverMock : AudioDriver {
    override fun loadMusic(musicSource: Resource) {
    }

    override fun setMusicVolume(volume: Float) {
    }

    override fun playMusic(musicSource: Resource, loop: Boolean) {
    }

    override fun pauseMusic() {
    }

    override fun resumeMusic() {
    }

    override fun stopMusic() {
    }

    override fun loadSound(soundSource: Resource) {
    }

    override fun setSoundVolume(volume: Float) {
    }

    override fun playSound(soundSource: Resource) {
    }

    override fun pauseSounds() {
    }

    override fun resumeSounds() {
    }

    override fun stopSounds() {
    }

    override fun release() {
    }
}
