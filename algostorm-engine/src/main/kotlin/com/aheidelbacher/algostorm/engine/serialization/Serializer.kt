package com.aheidelbacher.algostorm.engine.serialization

import java.io.IOException
import java.io.InputStream

import kotlin.reflect.KClass

interface Serializer {
    @Throws(IOException::class)
    fun <T : Any> readValue(src: InputStream, type: Class<T>): T

    @Throws(IOException::class)
    fun <T : Any> readValue(src: InputStream, type: KClass<T>): T =
            readValue(src, type.java)
}
