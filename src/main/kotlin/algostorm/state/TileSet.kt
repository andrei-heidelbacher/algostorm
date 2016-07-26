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

import kotlin.collections.Map

class TileSet(
        val name: String,
        val tileWidth: Int,
        val tileHeight: Int,
        val image: String,
        val imageWidth: Int,
        val imageHeight: Int,
        val margin: Int,
        val spacing: Int,
        val tileCount: Int,
        val properties: Map<String, Any> = emptyMap(),
        val tiles: Map<Int, Tile> = emptyMap()
) {
    class Tile(
            val animation: List<Frame>? = null,
            val properties: Map<String, Any> = emptyMap()
    ) {
        companion object {
            /**
             * Whether this global tile id is flipped horizontally.
             */
            val Int.isFlippedHorizontally: Boolean
                get() = and(0x40000000) != 0

            /**
             * Whether this global tile id is flipped vertically.
             */
            val Int.isFlippedVertically: Boolean
                get() = and(0x20000000) != 0

            /**
             * Whether this global tile id is flipped diagonally.
             */
            val Int.isFlippedDiagonally: Boolean
                get() = and(0x10000000) != 0

            /**
             * Flips this global tile id horizontally.
             */
            fun Int.flipHorizontally(): Int = xor(0x4000000)

            /**
             * Flips this global tile id vertically.
             */
            fun Int.flipVertically(): Int = xor(0x20000000)

            /**
             * Flips this global tile id diagonally.
             */
            fun Int.flipDiagonally(): Int = xor(0x10000000)
        }

        data class Frame(val tileId: Int, val duration: Int) {
            init {
                require(tileId >= 0) {
                    "Frame tile id $tileId can't be negative!"
                }
                require(duration > 0) {
                    "Frame duration $duration must be positive!"
                }
            }
        }

        init {
            require(animation?.isNotEmpty() ?: true) {
                "Animation can't have empty frame sequence!"
            }
        }
    }

    data class Viewport(
            val image: String,
            val x: Int,
            val y: Int,
            val width: Int,
            val height: Int
    ) {
        init {
            require(width >= 0 && height >= 0) {
                "Viewport sizes ($width, $height) can't be negative!"
            }
        }
    }

    init {
        require(tileWidth > 0 && tileHeight > 0) {
            "$name tile sizes ($tileWidth, $tileHeight) must be positive!"
        }
        require(imageWidth > 0 && imageHeight > 0) {
            "$name image sizes ($imageWidth, $imageHeight) must be positive!"
        }
        require(margin >= 0) { "$name margin $margin can't be negative!" }
        require(spacing >= 0) { "$name spacing $spacing can't be negative!" }
        require(tileCount > 0) { "$name tile count $tileCount isn't positive!" }
        val widthOffset =
                (imageWidth - 2 * margin + spacing) % (tileWidth + spacing)
        val heightOffset =
                (imageHeight - 2 * margin + spacing) % (tileHeight + spacing)
        require(widthOffset == 0 && heightOffset == 0) {
            "$name image sizes don't match margin, spacing and tile sizes!"
        }
    }

    fun getViewport(tileId: Int): Viewport {
        val columns = (imageWidth - 2 * margin + spacing) /
                (tileWidth + spacing)
        val row = tileId / columns
        val column = tileId % columns
        return Viewport(
                image = image,
                x = margin + column * (tileWidth + spacing),
                y = margin + row * (tileHeight + spacing),
                width = tileWidth,
                height = tileHeight
        )
    }

    override fun equals(other: Any?): Boolean =
            other is TileSet && name == other.name

    override fun hashCode(): Int = name.hashCode()
}
