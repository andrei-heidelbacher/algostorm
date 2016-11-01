package com.aheidelbacher.algostorm.engine.audio

import java.io.FileNotFoundException

/**
 * A sound player which should be used for short sound effects (at most `1 MB`
 * for all loaded sound effects).
 */
interface SoundPlayer {
    /**
     * Synchronously loads the sound resource located at the specified path,
     * making it available to future calls of [playSound].
     *
     * Paths are relative to a specialized location of sound resources.
     *
     * @param soundSource the path of the sound resource which should be loaded
     * @throws FileNotFoundException if the given resource doesn't exist
     */
    @Throws(FileNotFoundException::class)
    fun loadSound(soundSource: String): Unit

    /**
     * Asynchronously plays the given sound resource and returns the stream id
     * on which the sound is played.
     *
     * @param soundSource the path of the sound resource which should be played
     * @return the id of the stream on which the sound is played, or `-1` if the
     * sound could not be played
     * @throws IllegalArgumentException if the [soundSource] was not loaded
     */
    fun playSound(soundSource: String): Int

    /**
     * Pauses the stream with the given id.
     *
     * If the given stream isn't currently playing any sounds, this method has
     * no effect.
     *
     * @param streamId the id of the stream which should be paused
     */
    fun pauseStream(streamId: Int): Unit

    /**
     * Asynchronously resumes the stream with the given id.
     *
     * If the given stream isn't currently paused or was stopped, this method
     * has no effect.
     *
     * @param streamId the id of the stream which should be paused
     */
    fun resumeStream(streamId: Int): Unit

    /**
     * Stops the stream with the given id.
     *
     * @param streamId the id of the stream which should stop playing sounds
     */
    fun stopStream(streamId: Int): Unit
}
