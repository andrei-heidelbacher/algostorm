/*
 * Copyright 2016 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package algostorm.graphics2d.animation

import algostorm.ecs.Component
import algostorm.graphics2d.Sprite

/**
 * A component which contains all information required to animate an entity.
 *
 * @property animationSheet a map with all the animations associated with the
 * entity
 * @property frames the current animation applied to the entity
 * @property elapsedTicks how many ticks have elapsed from the current animation
 * @throws IllegalArgumentException if [frames] is empty or if [elapsedTicks] is
 * negative
 */
data class Animation private constructor(
        val animationSheet: AnimationSheet,
        val frames: List<Frame>,
        val elapsedTicks: Int
) : Component {
    companion object {
        const val IDLE = "idle"
    }

    init {
        require(frames.isNotEmpty()) { "Frame sequence can't be empty!" }
        require(elapsedTicks >= 0) { "Elapsed ticks can't be negative!" }
    }

    /**
     * Builds an animation which has the given [animationSheet], the `idle`
     * animation and `0` [elapsedTicks].
     *
     * @param animationSheet the animation sheet used for animating the owner
     * entity
     */
    constructor(animationSheet: AnimationSheet) : this(
            animationSheet = animationSheet,
            frames = animationSheet.idle,
            elapsedTicks = 0
    )

    /**
     * Returns the sprite that should be rendered at the time of calling.
     *
     * @return the sprite that should be rendered
     */
    internal fun getSprite(): Sprite {
        var remainingTicks = elapsedTicks
        for ((sprite, durationInTicks) in frames) {
            if (durationInTicks <= remainingTicks) {
                remainingTicks -= durationInTicks
            } else {
                return sprite
            }
        }
        return frames.last().sprite
    }

    /**
     * Returns a copy of the animation information after a tick has passed.
     *
     * If the current animation finishes, it continues with the idle animation
     * in the [animationSheet].
     *
     * @return the animation information after a tick
     */
    internal fun tick(): Animation {
        val durationInTicks = frames.sumBy { it.durationInTicks }
        val newElapsedTicks = (elapsedTicks + 1 - durationInTicks) %
                animationSheet.idle.sumBy { it.durationInTicks }
        return if (elapsedTicks + 1 < durationInTicks)
            copy(elapsedTicks = elapsedTicks + 1)
        else
            copy(frames = animationSheet.idle, elapsedTicks = newElapsedTicks)
    }
}
