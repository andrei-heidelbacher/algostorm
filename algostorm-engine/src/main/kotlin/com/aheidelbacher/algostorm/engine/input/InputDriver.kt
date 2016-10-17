package com.aheidelbacher.algostorm.engine.input

import com.aheidelbacher.algostorm.engine.driver.Driver

/** A driver that allows listening for input events. */
interface InputDriver : Driver, InputSource
