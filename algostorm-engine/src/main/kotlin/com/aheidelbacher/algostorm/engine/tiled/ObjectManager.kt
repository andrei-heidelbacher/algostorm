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

import com.aheidelbacher.algostorm.engine.tiled.Properties.PropertyType

/**
 * A manager which offers easy creation, deletion and retrieval of objects from
 * a specified [Layer.ObjectGroup] of a given [Map].
 *
 * At most one `ObjectManager` should be associated to an `ObjectGroup`. If an
 * `ObjectManager` is associated to an `ObjectGroup`, the objects should be
 * referenced only through the `ObjectManager`, otherwise the `ObjectManager`
 * state might be corrupted.
 *
 * @property map the associated map
 * @param name the name of the associated [Layer.ObjectGroup]
 * @throws IllegalArgumentException if the given `map` doesn't contain an
 * `ObjectGroup` layer with the given `name`
 */
class ObjectManager(private val map: Map, name: String) {
    private val objectGroup = requireNotNull(map.layers.find {
        it.name == name
    } as Layer.ObjectGroup?) {
        "Map doesn't contain an object group named $name!"
    }

    private val objectMap =
            objectGroup.objects.associateByTo(hashMapOf()) { it.id }

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
     * @param rotation the rotation of this object around the top-left corner in
     * clock-wise degrees
     * @param visible whether this object should be rendered or not
     * @param properties the properties of this object
     * @return the created object
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
            gid: Long = 0L,
            rotation: Float = 0F,
            visible: Boolean = true,
            properties: MutableMap<String, Any> = hashMapOf(),
            propertyTypes: MutableMap<String, PropertyType> = hashMapOf()
    ) : Object = Object(
            id = map.getAndIncrementNextObjectId(),
            name = name,
            type = type,
            x = x,
            y = y,
            width = width,
            height = height,
            gid = gid,
            rotation = rotation,
            visible = visible,
            properties = properties,
            propertyTypes = propertyTypes
    ).apply {
        objectGroup.objects.add(this)
        objectMap[id] = this
    }

    /**
     * Deletes the object with the given id.
     *
     * @param objectId the id of the object that should be deleted
     * @return `true` if the specified object was successfully deleted, `false`
     * if it didn't exist in this object group
     */
    fun delete(objectId: Int): Boolean = objectMap[objectId]?.let { obj ->
        objectMap.remove(objectId)
        objectGroup.objects.remove(obj)
    } ?: false
}
