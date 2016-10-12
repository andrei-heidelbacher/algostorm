package com.aheidelbacher.algostorm.engine.audio

import java.io.FileNotFoundException

interface SoundPlayer {
    /**
     * Loads the sound at the specified location, making it available to future
     * calls of [playSound].
     *
     * @param soundSource the location of the sound which should be loaded
     * @throws FileNotFoundException if the given sound doesn't exist
     * @throws IllegalStateException if this sound engine was released
     */
    @Throws(FileNotFoundException::class)
    fun loadSound(soundSource: String): Unit

    /**
     * Plays the given sound and returns the stream id on which the sound is
     * played.
     *
     * This should be used for short sound effects (at most a few seconds).
     *
     * @param soundSource the location of the sound which should be played
     * @return the id of the stream on which the sound is played, or `-1` if the
     * sound could not be played
     * @throws IllegalArgumentException if the [soundSource] was not loaded
     * @throws IllegalStateException if this sound engine was released
     */
    fun playSound(soundSource: String): Int

    /**
     * Stops the stream with the given id.
     *
     * @param streamId the id of the stream which should stop playing sounds
     * @throws IllegalStateException if this sound engine was released
     */
    fun stopStream(streamId: Int): Unit
}
