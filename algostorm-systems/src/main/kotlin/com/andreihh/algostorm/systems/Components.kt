/*
 * Copyright (c) 2017  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.andreihh.algostorm.systems

import com.andreihh.algostorm.core.ecs.Component
import com.andreihh.algostorm.core.ecs.ComponentLibrary
import com.andreihh.algostorm.systems.graphics2d.Animation
import com.andreihh.algostorm.systems.graphics2d.Sprite
import com.andreihh.algostorm.systems.physics2d.Body
import com.andreihh.algostorm.systems.physics2d.Position
import kotlin.reflect.KClass

class Components : ComponentLibrary {
    override val components: Map<KClass<out Component>, String> = mapOf(
            Animation::class to "Animation",
            Sprite::class to "Sprite",
            Body::class to "Body",
            Position::class to "Position"
    )
}
