package com.aheidelbacher.algostorm.engine.serialization

import java.io.IOException
import java.io.OutputStream

interface Deserializer {
    @Throws(IOException::class)
    fun writeValue(out: OutputStream, value: Any)
}
