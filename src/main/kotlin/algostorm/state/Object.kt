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

package algostorm.state

/**
 * A physical and renderable object in the game. Two objects are equal if and
 * only if they have the same [id].
 *
 * @property id the unique identifier of this object
 * @property x the x-axis coordinate of the lower-left corner of this object in
 * pixels
 * @property y the y-axis coordinate of the lower-left corner of this object in
 * pixels
 * @property width the x-axis width of this object in pixels
 * @property height the y-axis height of this object in pixels
 * @property gid the global id of the object tile. A value of `0` indicates the
 * empty tile (nothing to render)
 * @property rotation the rotation of this object around the lower-left corner
 * in radians
 * @property isVisible whether this object should be rendered or not
 * @property properties the properties of this object
 * @throws IllegalArgumentException if [id] or [gid] is negative or if [width]
 * or [height] are not positive
 */
class Object(
        val id: Int,
        var x: Int,
        var y: Int,
        val width: Int,
        val height: Int,
        var gid: Int = 0,
        var rotation: Float = 0F,
        var isVisible: Boolean = true,
        val properties: MutableMap<String, Any> = hashMapOf()
) {
    init {
        require(id >= 0) { "Object id $id can't be negative!" }
        require(gid >= 0) { "Object gid $gid can't be negative!" }
        require(width > 0 && height > 0) {
            "Object $id sizes ($width, $height) must be positive!"
        }
    }

    override fun equals(other: Any?): Boolean =
            other is Object && id == other.id

    override fun hashCode(): Int = id
}
