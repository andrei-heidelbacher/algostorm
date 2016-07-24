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

package algostorm.physics2d

import org.junit.Test

import algostorm.ecs.MutableEntityManagerMock
import algostorm.event.PublisherMock

class PhysicsSystemTest {
    val tileWidth: Int = 32
    val tileHeight: Int = 32

    fun makeEntity(x: Int, y: Int, isRigid: Boolean): Map<String, Any> = mapOf(
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
    }
}
