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
