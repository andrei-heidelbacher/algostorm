package com.aheidelbacher.algostorm.engine.serialization

import com.aheidelbacher.algostorm.engine.driver.Driver

interface SerializationDriver : Driver, Serializer, Deserializer
