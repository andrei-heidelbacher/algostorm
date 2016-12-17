package com.aheidelbacher.algostorm.systems.audio

import com.aheidelbacher.algostorm.engine.audio.MusicPlayer
import com.aheidelbacher.algostorm.engine.driver.Resource
import com.aheidelbacher.algostorm.event.Request
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber

/**
 * A system which handles playing longer sounds.
 *
 * @property musicPlayer the music player used to play longer sounds
 * @param musicSources the music resources which are loaded at construction time
 */
class MusicSystem(
        private val musicPlayer: MusicPlayer,
        musicSources: List<Resource>
) : Subscriber {
    /**
     * A request to set the music volume to the given value.
     *
     * @property volume the new value of the volume
     * @throws IllegalArgumentException if [volume] is not in the range `0..1`
     */
    class SetMusicVolume(val volume: Float) : Request<Unit>() {
        init {
            require(volume in 0F..1F) { "Invalid music volume $volume!" }
        }
    }

    /**
     * A request to play a longer sound, stopping the previously playing music.
     *
     * @property source the music resource which should be played
     * @property loop whether the sound should be looped or not
     */
    class PlayMusic(
            val source: Resource,
            val loop: Boolean = false
    ) : Request<Unit>()

    /** A request to stop the currently played music. */
    class StopMusic : Request<Unit>()

    init {
        musicSources.forEach { musicPlayer.loadMusic(it) }
    }

    /**
     * After receiving a [SetMusicVolume] request, the volume of the music is
     * modified accordingly.
     *
     * @param request the request
     */
    @Subscribe fun onSetVolume(request: SetMusicVolume) {
        musicPlayer.setMusicVolume(request.volume)
        request.complete(Unit)
    }

    /**
     * After receiving a [PlayMusic] request, the previously playing music is
     * stopped and the given music is played.
     *
     * @param request the request
     */
    @Subscribe fun onPlayMusic(request: PlayMusic) {
        musicPlayer.playMusic(request.source, request.loop)
        request.complete(Unit)
    }

    /**
     * After receiving a [StopMusic] request, the currently playing music is
     * stopped.
     *
     * @param request the request
     */
    @Subscribe fun onStopMusic(request: StopMusic) {
        musicPlayer.stopMusic()
        request.complete(Unit)
    }
}
