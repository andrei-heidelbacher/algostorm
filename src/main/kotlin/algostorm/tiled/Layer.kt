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

package algostorm.tiled

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes(
        Type(value = Layer.TileLayer::class, name = "tilelayer"),
        Type(value = Layer.ImageLayer::class, name = "imagelayer"),
        Type(value = Layer.ObjectGroup::class, name = "objectgroup")
)
sealed class Layer {
    abstract val name: String
    abstract val isVisible: Boolean
    abstract val opacity: Float
    abstract val offsetX: Int
    abstract val offsetY: Int

    abstract class TileLayer : Layer()

    abstract class ImageLayer : Layer()

    abstract class ObjectGroup : Layer()
}
