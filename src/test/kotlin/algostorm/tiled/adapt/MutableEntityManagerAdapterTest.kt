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

package algostorm.tiled.adapt

import algostorm.ecs.MutableEntityManagerTest
import algostorm.tiled.json.Layer
import algostorm.tiled.json.TiledMap

class MutableEntityManagerAdapterTest : MutableEntityManagerTest(
        entityManager = MutableEntityManagerAdapter(
                tiledMap = TiledMap(
                        width = 32,
                        height = 32,
                        tileWidth = 32,
                        tileHeight = 32,
                        orientation = TiledMap.Orientation.ORTHOGONAL,
                        tileSets = emptyList(),
                        layers = listOf(
                                Layer.ObjectGroup(
                                        name = MutableEntityManagerAdapter
                                                .ENTITY_LAYER_NAME,
                                        objects = hashSetOf()
                                )
                        ),
                        nextObjectId = 0
                )
        ),
        entityCount = 1000
)