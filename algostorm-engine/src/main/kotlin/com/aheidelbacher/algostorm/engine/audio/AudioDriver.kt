package com.aheidelbacher.algostorm.engine.audio

import com.aheidelbacher.algostorm.engine.driver.Driver

interface AudioDriver : Driver, MusicPlayer, SoundPlayer {
}
