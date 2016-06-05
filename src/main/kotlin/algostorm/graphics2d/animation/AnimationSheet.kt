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
 * A container that maps animation names to frame sequences.
 *
 * It must contain the [IDLE] animation. Every animation must contain at least
 * one frame.
 *
 * @property animations the underlying map containing the animations
 * @throws IllegalArgumentException if one of the frame sequences is empty or if
 * the `IDLE` animation is missing
 */
data class AnimationSheet(private val animations: Map<String, List<Frame>>) {
    companion object {
        /**
         * The name of the idle animation.
         */
        const val IDLE: String = "idle"
    }

    /**
     * A building-block for frame sequences used in animations.
     *
     * @property tileId the id of the tile that should be rendered
     * @property flippedHorizontally whether the tile should be flipped
     * horizontally
     * @property flippedVertically whether the tile should be flipped vertically
     * @property flippedDiagonally whether the tile should be flipped diagonally
     * @property durationInTicks how long should the current sprite be rendered
     * @throws IllegalArgumentException if [durationInTicks] is not positive
     */
    data class Frame(
            val tileId: Int,
            val flippedHorizontally: Boolean,
            val flippedVertically: Boolean,
            val flippedDiagonally: Boolean,
            val durationInTicks: Int) {
        init {
            require(durationInTicks > 0) {
                "Frame duration must be positive!"
            }
        }
    }

    init {
        require(IDLE in animations) { "Idle animation is missing!" }
        require(animations.all { it.value.isNotEmpty() }) {
            "Frame sequences can't be empty!"
        }
    }

    /**
     * The default animation that is to be displayed if no other animation is
     * active. The name of this animation is [IDLE].
     */
    val idle: List<Frame>
        get() = animations[IDLE] ?: error("Missing $IDLE animation!")

    /**
     * Returns the frame sequence associated to the given [name].
     *
     * @param name the name of the requested animation
     * @return the requested animation, or the [idle] animation if it isn't
     * contained in the sheet
     */
    operator fun get(name: String): List<Frame> = animations[name] ?: idle
}
