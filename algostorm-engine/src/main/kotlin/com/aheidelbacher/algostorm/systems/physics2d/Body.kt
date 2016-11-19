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

package com.aheidelbacher.algostorm.systems.physics2d

import com.fasterxml.jackson.annotation.JsonProperty

import com.aheidelbacher.algostorm.state.Component

/**
 * A component which contains data about the physical behavior of the entity.
 *
 * @property type the type which describes how this entity interacts with other
 * bodies
 */
data class Body(val type: Type) : Component {
    /** A property that indicates how a body interacts with other bodies. */
    enum class Type {
        /**
         * Activates collisions with other rigid bodies and triggers with other
         * trigger bodies.
         */
        @JsonProperty("rigid") RIGID,

        /**
         * Activates triggers with other rigid bodies, but does not activate
         * other triggers.
         */
        @JsonProperty("trigger") TRIGGER,
    }
}
