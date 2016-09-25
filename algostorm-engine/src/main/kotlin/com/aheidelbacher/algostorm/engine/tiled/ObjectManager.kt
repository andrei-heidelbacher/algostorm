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

package com.aheidelbacher.algostorm.engine.tiled

/**
 * A manager which offers easy creation, deletion and retrieval of objects from
 * a specified [Layer.ObjectGroup] of a given [MapObject].
 *
 * At most one `ObjectManager` should be associated to an `ObjectGroup`. If an
 * `ObjectManager` is associated to an `ObjectGroup`, the objects should be
 * referenced only through the `ObjectManager`, otherwise the `ObjectManager`
 * state might be corrupted.
 *
 * @property mapObject the associated mapObject
 * @param name the name of the associated [Layer.ObjectGroup]
 * @throws IllegalArgumentException if the given `mapObject` doesn't contain an
 * `ObjectGroup` layer with the given `name`
 */
class ObjectManager(private val mapObject: MapObject, name: String) {
    private val objectGroup = requireNotNull(mapObject.layers.find {
        it.name == name
    } as Layer.ObjectGroup?) {
        "MapObject doesn't contain an object group named $name!"
    }

    private val objectMap =
            objectGroup.objects.associateByTo(hashMapOf(), Object::id)

    /**
     * A lazy view of all the objects in the associated object group.
     */
    val objects: List<Object>
        get() = objectGroup.objects

    /**
     * Returns the object with the given id.
     *
     * @param objectId the id of the requested object
     * @return the requested object, or `null` if it doesn't exist
     */
    operator fun get(objectId: Int): Object? = objectMap[objectId]

    /**
     * Checks whether this object group contains an object with the given id.
     *
     * @param objectId the id of the requested object
     * @return `true` if the object with the given id exists in this object
     * group, `false` otherwise
     */
    operator fun contains(objectId: Int): Boolean = objectId in objectMap

    /**
     * Creates an object with the specified parameters, adds it to this object
     * group and returns it.
     *
     * @param name the name of this object
     * @param type the type of this object
     * @param x the x-axis coordinate of the top-left corner of this object in
     * pixels
     * @param y the y-axis coordinate of the top-left corner of this object in
     * pixels
     * @param width the width of this object in pixels
     * @param height the height of this object in pixels
     * @param gid the global id of the object tile. A value of `0` indicates the
     * empty tile (nothing to draw)
     * @param isVisible whether this object should be rendered or not
     * @throws IllegalStateException if there are too many objects in this
     * object group
     * @throws IllegalArgumentException if [gid] is negative or if [width] or
     * [height] are not positive
     */
    fun create(
            name: String = "",
            type: String = "",
            x: Int,
            y: Int,
            width: Int,
            height: Int,
            isVisible: Boolean = true,
            gid: Long = 0L
    ) : Object = Object(
            id = mapObject.getAndIncrementNextObjectId(),
            name = name,
            type = type,
            x = x,
            y = y,
            width = width,
            height = height,
            gid = gid,
            isVisible = isVisible
    ).apply {
        objectGroup.objects.add(this)
        objectMap[id] = this
    }

    /**
     * Removes the object with the given id from this object manager.
     *
     * @param objectId the id of the object that should be removed
     * @return `true` if the specified object was successfully removed, `false`
     * if it didn't exist in this object group
     */
    fun remove(objectId: Int): Boolean = objectMap[objectId]?.let { obj ->
        objectMap.remove(objectId)
        objectGroup.objects.remove(obj)
    } ?: false
}
