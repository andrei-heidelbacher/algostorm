package com.aheidelbacher.algostorm.engine.serialization

import java.io.IOException
import java.io.InputStream

import kotlin.reflect.KClass

/** A deserializer of non-generic objects. */
interface Deserializer {
    companion object {
        @Throws(IOException::class)
        inline fun <reified T : Any> Deserializer.readValue(
                inputStream: InputStream
        ): T = readValue(inputStream, T::class)
    }

    /**
     * Deserializers an object of the given type from the given stream.
     *
     * @param inputStream the stream from which the object is deserialized
     * @param type the class of the deserialized object type
     * @param T the type of the deserialized object type
     * @return the deserialized object
     * @throws IOException if there were any input-related or deserialization
     * errors
     */
    @Throws(IOException::class)
    fun <T : Any> readValue(inputStream: InputStream, type: Class<T>): T

    @Throws(IOException::class)
    fun <T : Any> readValue(inputStream: InputStream, type: KClass<T>): T =
            readValue(inputStream, type.java)
}
