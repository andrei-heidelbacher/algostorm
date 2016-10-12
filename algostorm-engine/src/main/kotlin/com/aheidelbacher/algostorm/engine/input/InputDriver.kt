package com.aheidelbacher.algostorm.engine.input

import com.aheidelbacher.algostorm.engine.driver.Driver

interface InputDriver<T: Any> : Driver, InputReader<T>, InputWriter<T>
