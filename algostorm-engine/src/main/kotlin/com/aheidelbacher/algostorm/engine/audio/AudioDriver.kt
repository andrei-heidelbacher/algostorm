package com.aheidelbacher.algostorm.engine.audio

import com.aheidelbacher.algostorm.engine.driver.Driver

/** A driver that offers audio services. */
interface AudioDriver : Driver, MusicPlayer, SoundPlayer
