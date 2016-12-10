package com.aheidelbacher.algostorm.test.engine.audio

import com.aheidelbacher.algostorm.engine.audio.AudioDriver
import com.aheidelbacher.algostorm.engine.driver.Resource

class AudioDriverMock : AudioDriver {
    override fun loadMusic(musicSource: Resource) {
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

    override fun playSound(soundSource: Resource): Int {
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
