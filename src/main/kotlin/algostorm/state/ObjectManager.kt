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

class ObjectManager(private val map: Map, name: String) {
    private val objectGroup = requireNotNull(map.layers.find {
        it.name == name
    } as Layer.ObjectGroup?) {
        "Map doesn't contain an ObjectGroup named $name!"
    }

    private val objectMap =
            objectGroup.objects.associateByTo(hashMapOf()) { it.id }

    val objects: Sequence<Object>
        get() = objectGroup.objects.asSequence()

    operator fun get(objectId: Int): Object? = objectMap[objectId]

    fun create(
            x: Int,
            y: Int,
            width: Int,
            height: Int,
            gid: Int = 0,
            rotation: Float = 0F,
            isVisible: Boolean = true,
            properties: MutableMap<String, Any> = hashMapOf()
    ) : Object = with(Object(
            id = map.getNextObjectId(),
            x = x,
            y = y,
            width = width,
            height = height,
            gid = gid,
            rotation = rotation,
            isVisible = isVisible,
            properties = properties
    )) {
        objectGroup.objects.add(this)
        objectMap[id] = this
        this
    }

    fun delete(id: Int): Boolean = objectMap[id]?.let { obj ->
        objectGroup.objects.remove(obj)
    } ?: false
}
