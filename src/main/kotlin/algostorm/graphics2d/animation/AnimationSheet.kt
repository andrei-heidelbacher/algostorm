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

/**
 * A container that maps animation names to frame sequences. It must contain the
 * [idle] animation.
 *
 * Every animation must contain at least two frames.
 *
 * @throws IllegalArgumentException if one of the animations has less than two
 * frames or if the `idle` animation is missing
 */
data class AnimationSheet(private val animations: Map<String, List<Frame>>) {
    init {
        require(AnimationSheet::idle.name in animations) {
            "Idle animation is missing!"
        }
        require(animations.all { animation -> animation.value.size > 1 }) {
            "Animation must contain at least two frames!"
        }
    }

    /**
     * The default animation that is to be displayed if no other animation is
     * active. The name of this animation is "idle" (without quotes).
     */
    val idle: List<Frame> by animations

    /**
     * Returns the frame sequence associated to the given [animation].
     *
     * @param animation the name of the requested animation
     * @return the requested animation, or the [idle] animation if it isn't
     * contained in the sheet
     */
    operator fun get(animation: String): List<Frame> =
            animations[animation] ?: idle
}
