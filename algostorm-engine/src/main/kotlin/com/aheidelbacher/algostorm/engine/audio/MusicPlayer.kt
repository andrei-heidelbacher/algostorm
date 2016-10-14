package com.aheidelbacher.algostorm.engine.audio

import java.io.FileNotFoundException

/**
 * A music player which should be used for playing longer sounds.
 *
 * Only one stream is active at all times. As such, only one music resource can
 * be played at once.
 */
interface MusicPlayer {
    /**
     * Synchronously loads the music resource located at the specified path,
     * making it available to future calls of [playMusic].
     *
     * Paths are relative to a specialized location of music resources.
     *
     * @param musicSource the path of the music resource which should be loaded
     * @throws FileNotFoundException if the given resource doesn't exist
     */
    @Throws(FileNotFoundException::class)
    fun loadMusic(musicSource: String): Unit

    /**
     * Asynchronously plays the given music resource.
     *
     * @param musicSource the location of the sound which should be played
     * @param loop whether the sound should be looped or not
     * @throws IllegalArgumentException if the [musicSource] was not loaded
     */
    fun playMusic(musicSource: String, loop: Boolean = false): Unit

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
