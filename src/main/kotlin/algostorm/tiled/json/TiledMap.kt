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

class TiledMap(
        val width: Int,
        val height: Int,
        val tileWidth: Int,
        val tileHeight: Int,
        val orientation: Orientation,
        val tileSets: List<TileSet>,
        val layers: List<Layer>,
        var nextObjectId: Int,
        val properties: MutableMap<String, Any> = hashMapOf()
) {
    enum class Orientation {
        ORTHOGONAL, ISOMETRIC, STAGGERED
    }
}
