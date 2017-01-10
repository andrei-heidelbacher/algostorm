/*
 * Copyright 2017 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.aheidelbacher.algostorm.systems.physics2d

import com.aheidelbacher.algostorm.ecs.EntityGroup
import com.aheidelbacher.algostorm.data.MapObject.Builder.Companion.mapObject
//import com.aheidelbacher.algostorm.systems.physics2d.PhysicsSystem.FindPath
import com.aheidelbacher.algostorm.systems.physics2d.geometry2d.Direction
import org.junit.Test

import com.aheidelbacher.algostorm.test.event.PublisherMock

import kotlin.test.assertEquals

class PhysicsSystemTest {
    val tileWidth: Int = 32
    val tileHeight: Int = 32

    /*fun makeMap(vararg rows: String): EntityGroup = mapObject {
        width = 32
        height = 32
        tileWidth = 32
        tileHeight = 32
        for (y in rows.indices) {
            for (x in rows[y].indices) {
                if (rows[y][x] !in ".SD") {
                    entity(listOf(Position(x, y), Body.STATIC))
                }
                if (rows[y][x] == 'S') {
                    entity(1, listOf(Position(x, y), Body.KINEMATIC))
                }
            }
        }
    }.entityPool.group

    private fun findSymbolInMap(symbol: Char, vararg rows: String): Position {
        val y = rows.indices.single { symbol in rows[it] }
        val x = rows[y].indices.single { rows[y][it] == symbol }
        return Position(x, y)
    }

    fun testMap(expectedPathLength: Int?, vararg rows: String) {
        val source = findSymbolInMap('S', *rows)
        val destination = findSymbolInMap('D', *rows)
        val request = FindPath(1, destination.x, destination.y)
        val path = emptyList<Direction>()//findPath(source, destination, makeMap(*rows))
        //assertEquals(expectedPathLength, path?.size)
        if (path != null) {
            var (x, y) = source
            assertEquals('S', rows[y][x])
            path.forEachIndexed { i, direction ->
                x += direction.dx
                y += direction.dy
                if (i + 1 < path.size) {
                    assertEquals('.', rows[y][x])
                }
            }
            assertEquals('D', rows[y][x])
        }
    }

    @Test
    fun testFindPath() {
        testMap(
                4,
                "SX..",
                "..X.",
                "..X.",
                "X..D"
        )
    }

    @Test
    fun testFindPath2() {
        testMap(
                11,
                ".......",
                "..SXXX.",
                ".XXX...",
                ".XX.XXX",
                ".X.XXD.",
                ".X...X."
        )
    }

    @Test
    fun testFindPath3() {
        testMap(
                9,
                ".......",
                "..SXXX.",
                ".XXX...",
                ".XX.XXX",
                ".X.XXD.",
                ".....X."
        )
    }

    @Test
    fun instantiatePublisherMock() {
        val publisherMock = PublisherMock()
    }*/

    /*fun makeEntity(x: Int, y: Int, isRigid: Boolean): MapObject<String, Any> = mapOf(
            Box.PROPERTY to Box(x, y, tileWidth, tileHeight),
            Rigid.PROPERTY to isRigid
    )

    @Test
    fun translateOverlappingShouldTriggerCollision() {
        val entityManager = MutableEntityManagerMock(mapOf(
                0 to makeEntity(0, 0, true),
                1 to makeEntity(tileWidth, tileHeight, true),
                2 to makeEntity(tileWidth, 0, false)
        ))
        val publisher = PublisherMock()
        val physicsSystem = PhysicsSystem(entityManager, publisher)
        val dx = tileWidth
        val dy = tileHeight
        physicsSystem.handleTranslateIntent(TransformIntent(0, dx, dy))
        publisher.verify(Collision(0, 1))
        publisher.verifyEmpty()
        entityManager.verify(mapOf(
                0 to makeEntity(0, 0, true),
                1 to makeEntity(tileWidth, tileHeight, true),
                2 to makeEntity(tileWidth, 0, false)
        ))
    }

    @Test
    fun translateLimitOverlappingShouldTriggerCollision() {
        val entityManager = MutableEntityManagerMock(mapOf(
                0 to makeEntity(0, 0, true),
                1 to makeEntity(2 * tileWidth - 1, 2 * tileHeight - 1, true),
                2 to makeEntity(tileWidth, 0, false)
        ))
        val publisher = PublisherMock()
        val physicsSystem = PhysicsSystem(entityManager, publisher)
        val dx = tileWidth
        val dy = tileHeight
        physicsSystem.handleTranslateIntent(TransformIntent(0, dx, dy))
        publisher.verify(Collision(0, 1))
        publisher.verifyEmpty()
        entityManager.verify(mapOf(
                0 to makeEntity(0, 0, true),
                1 to makeEntity(2 * tileWidth - 1, 2 * tileHeight - 1, true),
                2 to makeEntity(tileWidth, 0, false)
        ))
    }

    @Test
    fun translateNoOverlappingShouldTriggerTranslated() {
        val entityManager = MutableEntityManagerMock(mapOf(
                0 to makeEntity(0, 0, true),
                1 to makeEntity(tileWidth, tileHeight, false),
                2 to makeEntity(tileWidth, 0, true)
        ))
        val publisher = PublisherMock()
        val physicsSystem = PhysicsSystem(entityManager, publisher)
        val dx = tileWidth
        val dy = tileHeight
        physicsSystem.handleTranslateIntent(TransformIntent(0, dx, dy))
        publisher.verify(Transformed(0, dx, dy))
        publisher.verifyEmpty()
        entityManager.verify(mapOf(
                0 to makeEntity(tileWidth, tileHeight, true),
                1 to makeEntity(tileWidth, tileHeight, false),
                2 to makeEntity(tileWidth, 0, true)
        ))
    }*/
}
