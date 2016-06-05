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
 * A container which maps animation sheet ids to [AnimationSheet]. This object
 * should be saved as a property of the game.
 *
 * @property animations the underlying map of this container
 */
data class AnimationSet(private val animations: Map<Int, AnimationSheet>) {
    /**
     * Returns the animation sheet with the given id.
     *
     * @param animationSheetId the id of the requested animation sheet
     * @return the requested animation sheet, or `null` if the given id doesn't
     * exist in this container
     */
    operator fun get(animationSheetId: Int): AnimationSheet? =
            animations[animationSheetId]
}
