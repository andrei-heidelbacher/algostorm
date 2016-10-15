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

package com.aheidelbacher.algostorm.state

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

import com.aheidelbacher.algostorm.state.Layer.ObjectGroup

class ObjectGroupTest {
    companion object {
        val OBJECT_GROUP_NAME = "objectGroup"
        val TILE_WIDTH = 32
        val TILE_HEIGHT = 32
        val FIRST_ID = 1
        val OBJECT_COUNT = 1000
        val ID_RANGE = FIRST_ID until FIRST_ID + OBJECT_COUNT

        fun makeObject(id: Int) = Object(
                id = id,
                x = id * TILE_WIDTH,
                y = id * TILE_HEIGHT,
                width = TILE_WIDTH,
                height = TILE_HEIGHT
        )
    }

    val objectGroup: ObjectGroup = ObjectGroup(
            name = OBJECT_GROUP_NAME,
            objects = (FIRST_ID until FIRST_ID + OBJECT_COUNT / 2).map {
                makeObject(it)
            }
    )
    val map = MapObject(
            width = 32,
            height = 32,
            tileWidth = TILE_WIDTH,
            tileHeight = TILE_HEIGHT,
            layers = listOf(objectGroup),
            nextObjectId = FIRST_ID + OBJECT_COUNT / 2
    )

    @Before
    fun setUp() {
        for (id in FIRST_ID + OBJECT_COUNT / 2 until FIRST_ID + OBJECT_COUNT) {
            objectGroup.add(map.create(
                    x = id * TILE_WIDTH,
                    y = id * TILE_HEIGHT,
                    width = TILE_WIDTH,
                    height = TILE_HEIGHT
            ))
        }
    }

    @Test
    fun entitiesShouldReturnAllExistingEntities() {
        assertEquals(
                ID_RANGE.toSet(),
                objectGroup.objectSet.map { it.id }.toSet()
        )
    }

    @Test
    fun getNonExistingShouldReturnNull() {
        assertEquals(null, objectGroup[ID_RANGE.endInclusive + 1])
    }

    @Test
    fun getExistingShouldReturnNonNull() {
        for (id in ID_RANGE) {
            assertEquals(id, objectGroup[id]?.id)
        }
    }

    @Test
    fun getExistingShouldHaveSameProperties() {
        for (id in ID_RANGE) {
            val obj = objectGroup[id]
            assertEquals(obj?.x, id * TILE_WIDTH)
            assertEquals(obj?.y, id * TILE_HEIGHT)
        }
    }

    @Test
    fun removeExistingShouldReturnTrue() {
        for (id in ID_RANGE) {
            assertEquals(true, objectGroup.remove(id))
        }
    }

    @Test
    fun getAfterRemoveShouldReturnNull() {
        for (id in ID_RANGE) {
            objectGroup.remove(id)
            assertEquals(null, objectGroup[id])
        }
    }

    @Test
    fun removeNonExistingShouldReturnFalse() {
        assertEquals(false, objectGroup.remove(ID_RANGE.endInclusive + 1))
    }

    @Test
    fun containsExistingShouldReturnTrue() {
        for (id in ID_RANGE) {
            assertEquals(true, id in objectGroup)
        }
    }

    @Test
    fun containsNonExistingShouldReturnFalse() {
        assertEquals(false, ID_RANGE.endInclusive + 1 in objectGroup)
    }
}
