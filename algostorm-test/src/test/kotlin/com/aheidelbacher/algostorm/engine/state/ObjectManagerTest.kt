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

package com.aheidelbacher.algostorm.engine.state

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ObjectManagerTest {
    companion object {
        val OBJECT_GROUP_NAME = "objectGroup"
        val TILE_WIDTH = 32
        val TILE_HEIGHT = 32
        val FIRST_ID = 1
        val OBJECT_COUNT = 1000
        val ID_RANGE = FIRST_ID until FIRST_ID + OBJECT_COUNT

        fun makeObject(id: Int) = objectOf(
                id = id,
                x = id * TILE_WIDTH,
                y = id * TILE_HEIGHT,
                width = TILE_WIDTH,
                height = TILE_HEIGHT
        )
    }

    val map = mapObjectOf(
            width = 32,
            height = 32,
            tileWidth = TILE_WIDTH,
            tileHeight = TILE_HEIGHT,
            layers = listOf(objectGroupOf(
                    name = OBJECT_GROUP_NAME,
                    objects = (FIRST_ID until FIRST_ID + OBJECT_COUNT / 2).map {
                        makeObject(it)
                    }.let { mutableListOf(*it.toTypedArray()) }
            )),
            nextObjectId = FIRST_ID + OBJECT_COUNT / 2
    )
    val objectManager = ObjectManager(map, OBJECT_GROUP_NAME)

    @Before
    fun setUp() {
        for (id in FIRST_ID + OBJECT_COUNT / 2 until FIRST_ID + OBJECT_COUNT) {
            objectManager.create(
                    x = id * TILE_WIDTH,
                    y = id * TILE_HEIGHT,
                    width = TILE_WIDTH,
                    height = TILE_HEIGHT
            )
        }
    }

    @Test
    fun entitiesShouldReturnAllExistingEntities() {
        assertEquals(
                ID_RANGE.toSet(),
                objectManager.objects.map { it.id }.toSet()
        )
    }

    @Test
    fun getNonExistingShouldReturnNull() {
        assertEquals(null, objectManager[ID_RANGE.endInclusive + 1])
    }

    @Test
    fun getExistingShouldReturnNonNull() {
        for (id in ID_RANGE) {
            assertEquals(id, objectManager[id]?.id)
        }
    }

    @Test
    fun getExistingShouldHaveSameProperties() {
        for (id in ID_RANGE) {
            val obj = objectManager[id]
            assertEquals(obj?.x, id * TILE_WIDTH)
            assertEquals(obj?.y, id * TILE_HEIGHT)
        }
    }

    @Test
    fun removeExistingShouldReturnTrue() {
        for (id in ID_RANGE) {
            assertEquals(true, objectManager.remove(id))
        }
    }

    @Test
    fun getAfterRemoveShouldReturnNull() {
        for (id in ID_RANGE) {
            objectManager.remove(id)
            assertEquals(null, objectManager[id])
        }
    }

    @Test
    fun removeNonExistingShouldReturnFalse() {
        assertEquals(false, objectManager.remove(ID_RANGE.endInclusive + 1))
    }

    @Test
    fun containsExistingShouldReturnTrue() {
        for (id in ID_RANGE) {
            assertEquals(true, id in objectManager)
        }
    }

    @Test
    fun containsNonExistingShouldReturnFalse() {
        assertEquals(false, ID_RANGE.endInclusive + 1 in objectManager)
    }
}
