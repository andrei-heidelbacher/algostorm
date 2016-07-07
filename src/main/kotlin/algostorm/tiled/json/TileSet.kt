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

package algostorm.tiled.json

import com.fasterxml.jackson.annotation.JsonProperty

class TileSet(
        val name: String,
        @JsonProperty("tilewidth") val tileWidth: Int,
        @JsonProperty("tileheight") val tileHeight: Int,
        val image: String,
        @JsonProperty("imagewidth") val imageWidth: Int,
        @JsonProperty("imageheight") val imageHeight: Int,
        val margin: Int,
        val spacing: Int,
        @JsonProperty("firstgid") val firstGid: Int,
        @JsonProperty("tilecount") val tileCount: Int,
        val properties: Map<String, Any> = emptyMap(),
        val terrains: List<Terrain> = emptyList(),
        @JsonProperty("tileproperties")
        private val tileProperties: Map<String, Map<String, Any>> = emptyMap(),
        private val tiles: Map<String, Tile> = emptyMap()
) {
    class Tile(
            val animation: List<Frame>? = null,
            val terrain: IntArray? = null
    ) {
        companion object {
            val Long.isFlippedHorizontally: Boolean
                get() = and(0x80000000) != 0L

            val Long.isFlippedVertically: Boolean
                get() = and(0x40000000) != 0L

            val Long.isFlippedDiagonally: Boolean
                get() = and(0x20000000) != 0L

            fun Long.flipHorizontally(): Long = xor(0x8000000)

            fun Long.flipVertically(): Long = xor(0x40000000)

            fun Long.flipDiagonally(): Long = xor(0x20000000)
        }

        data class Frame(
                @JsonProperty("tileid") val tileId: Int,
                val duration: Int
        ) {
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
            require(animation == null || animation.isNotEmpty()) {
                "Animation can't be empty!"
            }
            require(terrain == null || terrain.size == 4) {
                "Terrain must contain exactly four elements!"
            }
        }
    }

    class Terrain(val name: String, @JsonProperty("tileid") val tileId: Int)

    data class Viewport(
            val image: String,
            val x: Int,
            val y: Int,
            val width: Int,
            val height: Int
    ) {
        init {
            require(width >= 0 && height >= 0) {
                "Viewport sizes ($width x $height) can't be negative!"
            }
        }
    }

    init {
        require(tileWidth > 0 && tileHeight > 0) {
            "$name tile sizes ($tileWidth x $tileHeight) must be positive!"
        }
        require(imageWidth > 0 && imageHeight > 0) {
            "$name image sizes ($imageWidth x $imageHeight) must be positive!"
        }
        require(margin >= 0) { "$name margin $margin can't be negative!" }
        require(spacing >= 0) { "$name spacing $spacing can't be negative!" }
        require(firstGid > 0) { "$name first gid $firstGid must be positive!" }
        require(tileCount > 0) {
            "$name tile count $tileCount must be positive!"
        }
        val widthOffset = (imageWidth - 2 * margin + spacing) %
                (tileWidth + spacing)
        val heightOffset = (imageHeight - 2 * margin + spacing) %
                (tileHeight + spacing)
        require(widthOffset == 0 && heightOffset == 0) {
            "$name image sizes don't match margin, spacing and tile sizes!"
        }
    }


    private fun getTileId(gid: Long): Int {
        val tileId = gid.and(0x1FFFFFFF).toInt() - firstGid
        require(tileId in 0..tileCount - 1) {
            "Gid $gid is not part of the $name tile set!"
        }
        return tileId
    }

    fun getTileProperties(gid: Long): Map<String, Any> =
            tileProperties["${getTileId(gid)}"] ?: emptyMap()

    fun getTile(gid: Long): Tile = tiles["${getTileId(gid)}"] ?: Tile()

    fun getViewport(gid: Long): Viewport {
        val tileId = getTileId(gid)
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
}
