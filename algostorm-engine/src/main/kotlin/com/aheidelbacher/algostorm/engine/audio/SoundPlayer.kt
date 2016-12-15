package com.aheidelbacher.algostorm.engine.audio

import com.aheidelbacher.algostorm.engine.driver.Resource

/**
 * A sound player which should be used for short sound effects (at most `1 MB`
 * for all loaded sound effects).
 */
interface SoundPlayer {
    /**
     * Synchronously loads the given sound resource, making it available to
     * future calls of [playSound].
     *
     * @param soundSource the sound resource which should be loaded
     */
    fun loadSound(soundSource: Resource): Unit

    /**
     * Asynchronously plays the given sound resource and returns the stream id
     * on which the sound is played.
     *
     * @param soundSource the sound resource which should be played
     * @return the id of the stream on which the sound is played, or `-1` if the
     * sound could not be played
     * @throws IllegalArgumentException if the [soundSource] was not loaded
     */
    fun playSound(soundSource: Resource): Int

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
