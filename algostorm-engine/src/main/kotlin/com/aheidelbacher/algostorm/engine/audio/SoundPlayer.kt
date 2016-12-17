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
     * Asynchronously plays the given sound resource.
     *
     * @param soundSource the sound resource which should be played
     * @throws IllegalArgumentException if the [soundSource] was not loaded
     */
    fun playSound(soundSource: Resource): Unit

    /** Pauses all of the currently playing sounds. */
    fun pauseSounds(): Unit

    /** Asynchronously resumes all the paused sounds. */
    fun resumeSounds(): Unit

    /** Stops all of the currently playing sounds. */
    fun stopSounds(): Unit
}
