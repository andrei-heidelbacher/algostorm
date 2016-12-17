package com.aheidelbacher.algostorm.engine.audio

import com.aheidelbacher.algostorm.engine.driver.Resource

/**
 * A music player which should be used for playing longer sounds.
 *
 * Only one stream is active at all times. As such, only one music resource can
 * be played at once.
 */
interface MusicPlayer {
    /**
     * Synchronously loads the given music resource, making it available to
     * future calls of [prepareMusic].
     *
     * @param musicSource the music which should be loaded
     */
    fun loadMusic(musicSource: Resource): Unit

    /**
     * Stops the previously playing music and prepares the given music resource
     * to be played by setting it in the paused state.
     *
     * To start playing, invoke [resumeMusic] after this method call.
     *
     * @param musicSource the sound which should be prepared
     * @param loop whether the sound should be looped or not
     * @throws IllegalArgumentException if the [musicSource] was not loaded
     */
    fun prepareMusic(musicSource: Resource, loop: Boolean = false): Unit

    /**
     * Pauses the currently playing music.
     *
     * If no music resource is currently playing, this method has no effect.
     */
    fun pauseMusic(): Unit

    /**
     * Asynchronously resumes the currently paused music.
     *
     * If no music resource was previously paused, prepared or if it was
     * stopped, this method has no effect.
     */
    fun resumeMusic(): Unit

    /**
     * Stops the currently playing music.
     *
     * If no music resource is currently playing, this method has no effect.
     */
    fun stopMusic(): Unit
}
