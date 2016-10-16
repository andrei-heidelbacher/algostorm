package com.aheidelbacher.algostorm.test.engine.serialization

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

import com.aheidelbacher.algostorm.engine.serialization.Deserializer.Companion.readValue
import com.aheidelbacher.algostorm.engine.serialization.SerializationDriver

import java.io.ByteArrayOutputStream
import java.io.InputStream

@Ignore
abstract class SerializationDriverTest {
    private lateinit var serializationDriver: SerializationDriver

    protected abstract fun createSerializationDriver(): SerializationDriver

    protected abstract val testDataMock: TestDataMock

    protected abstract val inputStream: InputStream

    @Before
    fun initializeSerializationDriver() {
        serializationDriver = createSerializationDriver()
    }

    @Test
    fun testDataInlineDeserialization() {
        assertEquals(
                testDataMock,
                serializationDriver.readValue<TestDataMock>(inputStream)
        )
    }

    @Test
    fun testDataDeserialization() {
        assertEquals(
                testDataMock,
                serializationDriver.readValue(inputStream, TestDataMock::class)
        )
    }

    @Test
    fun testDataSerialization() {
        val byteStream = ByteArrayOutputStream()
        serializationDriver.writeValue(byteStream, testDataMock)
        val inputStream = byteStream.toByteArray().inputStream()
        assertEquals(
                testDataMock,
                serializationDriver.readValue<TestDataMock>(inputStream)
        )
    }
}
