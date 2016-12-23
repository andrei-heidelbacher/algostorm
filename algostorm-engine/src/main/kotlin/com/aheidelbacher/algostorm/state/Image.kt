/*
 * Copyright (c) 2016  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aheidelbacher.algostorm.state

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
