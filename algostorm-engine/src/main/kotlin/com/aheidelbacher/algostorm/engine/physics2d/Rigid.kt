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

import com.aheidelbacher.algostorm.engine.state.Object

/**
 * A component which tells that the owner object will block movement and
 * triggers collisions.
 */
object Rigid {
    /**
     * The name of the rigid property. It is of type [Boolean].
     */
    const val PROPERTY: String = "isRigid"

    /**
     * Returns `true` if this object contains the rigid property and it is set
     * to `true`, `false` otherwise.
     */
    val Object.isRigid: Boolean
        get() = properties[PROPERTY] as? Boolean ?: false
}
