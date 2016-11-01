package com.aheidelbacher.algostorm.systems.audio

import com.aheidelbacher.algostorm.engine.audio.MusicPlayer
import com.aheidelbacher.algostorm.event.Event
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber
import com.aheidelbacher.algostorm.state.File

import java.io.FileNotFoundException

/**
 * A system which handles playing longer sounds.
 *
 * @property musicPlayer the music player used to play longer sounds
 * @param musicSources the paths of the music resources which are loaded at
 * construction time
 * @throws FileNotFoundException if any of the given resources doesn't exist
 */
class MusicSystem @Throws(FileNotFoundException::class) constructor(
        private val musicPlayer: MusicPlayer,
        musicSources: List<File>
) : Subscriber {
    /**
     * An event that requests a longer sound to be played, stopping the
     * previously playing music.
     *
     * @property source the sound which should be played
     * @property loop whether the sound should be looped or not
     */
    data class PlayMusic(val source: File, val loop: Boolean = false) : Event

    /** An event which signals the currently played music to be stopped. */
    object StopMusic : Event

    init {
        musicSources.forEach { musicPlayer.loadMusic(it.path) }
    }

    /**
     * After receiving a [PlayMusic] event, the previously playing music is
     * stopped and the given music is played.
     *
     * @param event the event which requests music to be played
     */
    @Subscribe fun onPlayMusic(event: PlayMusic) {
        musicPlayer.playMusic(event.source.path, event.loop)
    }

    /**
     * After receiving a [StopMusic] event, the currently playing music is
     * stopped.
     *
     * @param event the event which requests the music to be stopped
     */
    @Suppress("unused_parameter")
    @Subscribe fun onStopMusic(event: StopMusic) {
        musicPlayer.stopMusic()
    }
}
