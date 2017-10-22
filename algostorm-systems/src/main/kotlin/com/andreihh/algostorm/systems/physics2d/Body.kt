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

package com.andreihh.algostorm.systems.physics2d

import com.andreihh.algostorm.core.ecs.Component

/**
 * A component which indicates how this entity interacts with other bodies.
 *
 * Kinematic and static bodies are colliders (they can trigger collisions).
 *
 * Kinematic bodies can be transformed, activate triggers and collide with other
 * static or kinematic bodies.
 *
 * Static bodies can't be transformed and can be collided by other kinematic
 * bodies.
 *
 * Triggers can't be transformed and can be activated by other kinematic bodies.
 */
enum class Body : Component {
    KINEMATIC, STATIC, TRIGGER
}
