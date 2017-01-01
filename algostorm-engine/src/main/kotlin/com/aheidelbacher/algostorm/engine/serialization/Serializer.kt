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

import java.io.IOException
import java.io.OutputStream

/** A serializer of arbitrary objects. */
interface Serializer {
    /**
     * Serializes the given object to the given stream.
     *
     * @param out the stream to which the given object is serialized
     * @param value the object which should be serialized
     * @throws IOException if there were any input-related or serialization
     * errors
     */
    @Throws(IOException::class)
    fun writeValue(out: OutputStream, value: Any): Unit
}
