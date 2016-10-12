package com.aheidelbacher.algostorm.engine.audio

import java.io.FileNotFoundException

interface MusicPlayer {
    /**
     * Loads the sound at the specified location, making it available to future
     * calls of [playMusic].
     *
     * @param musicSource the location of the sound which should be loaded
     * @throws FileNotFoundException if the given sound doesn't exist
     * @throws IllegalStateException if this sound engine was released
     */
    @Throws(FileNotFoundException::class)
    fun loadMusic(musicSource: String): Unit

    /**
     * Plays the given sound on a dedicated stream. This should be used to play
     * longer sounds. Only one stream is active at all times for this type of
     * sounds.
     *
     * @param musicSource the location of the sound which should be played
     * @param loop whether the sound should be looped or not
     * @return the id of the stream on which the sound is played, or `-1` if the
     * sound could not be played
     * @throws IllegalArgumentException if the [musicSource] was not loaded
     * @throws IllegalStateException if this sound engine was released
     */
    fun playMusic(musicSource: String, loop: Boolean = false): Unit

    /**
     * Stops the stream dedicated to longer sounds. This will stop any sounds
     * played with [playMusic].
     *
     * @throws IllegalStateException if this sound engine was released
     */
    fun stopMusic(): Unit
}
