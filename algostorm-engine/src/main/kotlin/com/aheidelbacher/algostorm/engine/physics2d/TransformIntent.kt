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

package com.aheidelbacher.algostorm.engine.physics2d

import com.aheidelbacher.algostorm.event.Event

/**
 * An event which signals a transformation that should be applied on the given
 * object.
 *
 * @property objectId the id of the object which should be transformed
 * @property dx the translation amount on the x-axis in pixels
 * @property dy the translation amount on the y-axis in pixels
 * @property rotate the rotation amount in radians
 */
data class TransformIntent(
        val objectId: Int,
        val dx: Int,
        val dy: Int,
        val rotate: Float
) : Event
