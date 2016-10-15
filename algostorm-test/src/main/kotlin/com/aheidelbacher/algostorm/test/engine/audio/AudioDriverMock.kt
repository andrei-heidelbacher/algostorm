package com.aheidelbacher.algostorm.test.engine.audio

import com.aheidelbacher.algostorm.engine.audio.AudioDriver

class AudioDriverMock : AudioDriver {
    override fun loadMusic(musicSource: String) {
    }

    override fun playMusic(musicSource: String, loop: Boolean) {
    }

    override fun pauseMusic() {
    }

    override fun resumeMusic() {
    }

    override fun stopMusic() {
    }

    override fun loadSound(soundSource: String) {
    }

    override fun playSound(soundSource: String): Int {
        return -1
    }

    override fun pauseStream(streamId: Int) {
    }

    override fun resumeStream(streamId: Int) {
    }

    override fun stopStream(streamId: Int) {
    }

    override fun release() {
    }
}
