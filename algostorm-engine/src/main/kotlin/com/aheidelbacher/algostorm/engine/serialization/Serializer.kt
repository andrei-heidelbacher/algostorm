package com.aheidelbacher.algostorm.engine.serialization

import java.io.IOException
import java.io.OutputStream

/** A serializer of arbitrary objects. */
interface Serializer {
    @Throws(IOException::class)
    fun writeValue(out: OutputStream, value: Any)
}
