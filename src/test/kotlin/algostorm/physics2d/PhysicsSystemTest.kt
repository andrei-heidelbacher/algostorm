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
    fun translateIntentTest() {
        val entityManager = MutableEntityManagerMock(mapOf(
                0 to listOf(),
                1 to listOf(),
                2 to listOf()
        ))
        val publisher = PublisherMock()
        val physicsSystem = PhysicsSystem(entityManager, publisher)
        val entityId = 1
        val dx = 32
        val dy = 32
        physicsSystem.handlers.forEach {
            it.notify(TranslateIntent(entityId, dx, dy))
        }
        publisher.verify(Translated(entityId, dx, dy))
        publisher.verifyEmpty()
        entityManager.verify(mapOf(
                0 to listOf(),
                1 to listOf(),
                2 to listOf()
        ))
    }
}
