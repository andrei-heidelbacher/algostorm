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
    @Test
    fun translateOverlappingShouldTriggerCollision() {
        val entityManager = MutableEntityManagerMock(mapOf(
                0 to listOf(Box(0, 0, 32, 32), Rigid),
                1 to listOf(Box(32, 32, 32, 32), Rigid),
                2 to listOf(Box(32, 0, 32, 32))
        ))
        val publisher = PublisherMock()
        val physicsSystem = PhysicsSystem(entityManager, publisher)
        val dx = 32
        val dy = 32
        physicsSystem.handleTranslateIntent(TranslateIntent(0, dx, dy))
        publisher.verify(Collision(0, 1))
        publisher.verifyEmpty()
        entityManager.verify(mapOf(
                0 to listOf(Box(0, 0, 32, 32), Rigid),
                1 to listOf(Box(32, 32, 32, 32), Rigid),
                2 to listOf(Box(32, 0, 32, 32))
        ))
    }

    @Test
    fun translateNoOverlappingShouldTriggerTranslated() {
        val entityManager = MutableEntityManagerMock(mapOf(
                0 to listOf(Box(0, 0, 32, 32), Rigid),
                1 to listOf(Box(32, 32, 32, 32)),
                2 to listOf(Box(32, 0, 32, 32))
        ))
        val publisher = PublisherMock()
        val physicsSystem = PhysicsSystem(entityManager, publisher)
        val dx = 32
        val dy = 32
        physicsSystem.handleTranslateIntent(TranslateIntent(0, dx, dy))
        publisher.verify(Translated(0, dx, dy))
        publisher.verifyEmpty()
        entityManager.verify(mapOf(
                0 to listOf(Box(32, 32, 32, 32), Rigid),
                1 to listOf(Box(32, 32, 32, 32)),
                2 to listOf(Box(32, 0, 32, 32))
        ))
    }
}
