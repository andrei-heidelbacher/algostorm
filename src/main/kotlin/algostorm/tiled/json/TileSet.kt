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

class TileSet(
        val name: String,
        val tileWidth: Int,
        val tileHeight: Int,
        val image: String,
        val imageWidth: Int,
        val imageHeight: Int,
        val margin: Int,
        val spacing: Int,
        val firstGid: Int,
        val tileCount: Int,
        val properties: Map<String, Any> = emptyMap(),
        val terrains: List<Terrain> = emptyList(),
        private val tileProperties: Map<String, Map<String, Any>>? = null,
        private val tiles: Map<String, Tile>? = null,
        private val tilePropertyTypes: Map<String, Map<String, String>>? = null
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

        data class Frame(val tileId: Int, val duration: Int)

        init {
            require(animation == null || animation.isNotEmpty()) {
                "Animation can't be empty!"
            }
            require(terrain == null || terrain.size == 4) {
                "Terrain must contain exactly four elements!"
            }
        }
    }

    class Terrain(val name: String, val tileId: Int)

    fun getTileProperties(tileId: Int): Map<String, Any> =
            tileProperties?.get("$tileId") ?: emptyMap()

    fun getTile(tileId: Int): Tile? = tiles?.get("$tileId")

    fun getTileId(gid: Long): Int {
        val tileId = gid.and(0x1FFFFFFF).toInt() - firstGid
        require(tileId in 0..tileCount - 1) {
            "Gid $gid is not part of the $name tile set!"
        }
        return tileId
    }
}
