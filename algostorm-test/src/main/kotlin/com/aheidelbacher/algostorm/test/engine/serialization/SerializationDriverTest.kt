package com.aheidelbacher.algostorm.test.engine.serialization

import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

import com.aheidelbacher.algostorm.engine.serialization.Deserializer.Companion.readValue
import com.aheidelbacher.algostorm.engine.serialization.SerializationDriver

import java.io.ByteArrayOutputStream
import java.io.InputStream

@Ignore
abstract class SerializationDriverTest {
    protected abstract fun createSerializationDriver(): SerializationDriver

    protected abstract val testDataMock: TestDataMock

    protected abstract val inputStream: InputStream

    @Test
    fun testDataInlineDeserialization() {
        val serializationDriver = createSerializationDriver()
        assertEquals(
                testDataMock,
                serializationDriver.readValue<TestDataMock>(inputStream)
        )
    }

    @Test
    fun testDataDeserialization() {
        val serializationDriver = createSerializationDriver()
        assertEquals(
                testDataMock,
                serializationDriver.readValue(inputStream, TestDataMock::class)
        )
    }

    @Test
    fun testDataSerialization() {
        val serializationDriver = createSerializationDriver()
        val byteStream = ByteArrayOutputStream()
        serializationDriver.writeValue(byteStream, testDataMock)
        val inputStream = byteStream.toByteArray().inputStream()
        assertEquals(
                testDataMock,
                serializationDriver.readValue<TestDataMock>(inputStream)
        )
    }
}
