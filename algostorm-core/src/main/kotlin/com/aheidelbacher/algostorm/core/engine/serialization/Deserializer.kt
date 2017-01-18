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

package com.aheidelbacher.algostorm.core.engine.serialization

import java.io.IOException
import java.io.InputStream

import kotlin.reflect.KClass

/** A deserializer of non-generic objects. */
interface Deserializer {
    companion object {
        @Throws(IOException::class)
        inline fun <reified T : Any> Deserializer.readValue(
                src: InputStream
        ): T = readValue(src, T::class)
    }

    /** The format from which objects are deserialized. */
    val format: String

    /**
     * Deserializes an object of the given type from the given stream.
     *
     * @param src the stream from which the object is deserialized
     * @param type the class of the deserialized object type
     * @param T the type of the deserialized object
     * @return the deserialized object
     * @throws IOException if there were any input-related or deserialization
     * errors
     */
    @Throws(IOException::class)
    fun <T : Any> readValue(src: InputStream, type: KClass<T>): T
}
