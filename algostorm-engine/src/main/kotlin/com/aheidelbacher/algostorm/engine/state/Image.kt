package com.aheidelbacher.algostorm.engine.state

/**
 * Meta-data associated to an image.
 *
 * @property source the location of the image
 * @property width the width in pixels of this image
 * @property height the height in pixels of this image
 * @throws IllegalArgumentException if [width] or [height] are not positive
 */
data class Image(val source: File, val width: Int, val height: Int) {
    init {
        require(width > 0) { "$this width must be positive!" }
        require(height > 0) { "$this height must be positive!" }
    }
}
