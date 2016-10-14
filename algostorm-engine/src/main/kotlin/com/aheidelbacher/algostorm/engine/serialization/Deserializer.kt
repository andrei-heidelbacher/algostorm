package com.aheidelbacher.algostorm.engine.serialization

import java.io.IOException
import java.io.InputStream

import kotlin.reflect.KClass

/** A deserializer of non-generic objects. */
interface Deserializer {
    /**
     * Deserializers an object of the given type from the given stream.
     *
     * @param src the stream from which the object is deserialized
     * @param type the class of the deserialized object type
     * @param T the type of the deserialized object type
     * @return the deserialized object
     * @throws IOException if there were any input-related or deserialization
     * errors
     */
    @Throws(IOException::class)
    fun <T : Any> readValue(src: InputStream, type: Class<T>): T

    /** Utility method override. */
    @Throws(IOException::class)
    fun <T : Any> readValue(src: InputStream, type: KClass<T>): T =
            readValue(src, type.java)
}
