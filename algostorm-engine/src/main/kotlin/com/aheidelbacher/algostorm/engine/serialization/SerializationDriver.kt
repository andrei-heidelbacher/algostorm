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

package com.aheidelbacher.algostorm.engine.serialization

import com.aheidelbacher.algostorm.engine.driver.Driver

/** A driver that offers serialization and deserialization services. */
interface SerializationDriver : Driver, Serializer, Deserializer {
    companion object {
        private const val SERVICE =
                "com.aheidelbacher.algostorm.engine.serialization.JsonDriver"

        /**
         * Returns the registered implementation of a serialization driver.
         */
        operator fun invoke(): SerializationDriver =
                Class.forName(SERVICE).newInstance() as SerializationDriver
    }

    /** The format used to serialize and deserialize objects. */
    override val format: String
}
