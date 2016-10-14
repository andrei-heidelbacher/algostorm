package com.aheidelbacher.algostorm.engine.serialization

import com.aheidelbacher.algostorm.engine.driver.Driver

/** A driver that offers serialization and deserialization services. */
interface SerializationDriver : Driver, Serializer, Deserializer
