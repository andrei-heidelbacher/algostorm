package com.aheidelbacher.algostorm.engine.audio

import com.aheidelbacher.algostorm.engine.driver.Resource

import java.io.FileNotFoundException

/**
 * A music player which should be used for playing longer sounds.
 *
 * Only one stream is active at all times. As such, only one music resource can
 * be played at once.
 */
interface MusicPlayer {
    /**
     * Synchronously loads the given music resource, making it available to
     * future calls of [playMusic].
     *
     * @param musicSource the music which should be loaded
     * @throws FileNotFoundException if the given resource doesn't exist
     */
    @Throws(FileNotFoundException::class)
    fun loadMusic(musicSource: Resource): Unit

    /**
     * Stops the previously playing music and asynchronously plays the given
     * music resource.
     *
     * @param musicSource the sound which should be played
     * @param loop whether the sound should be looped or not
     * @throws IllegalArgumentException if the [musicSource] was not loaded
     */
    fun playMusic(musicSource: Resource, loop: Boolean = false): Unit

    /**
     * Pauses the currently playing music.
     *
     * If no music resource is currently playing, this method has no effect.
     */
    fun pauseMusic(): Unit

    /**
     * Asynchronously resumes the currently paused music.
     *
     * If no music resource was previously paused or if it was stopped, this
     * method has no effect.
     */
    fun resumeMusic(): Unit

    /**
     * Stops the currently playing music.
     *
     * If no music resource is currently playing, this method has no effect.
     */
    fun stopMusic(): Unit
}
