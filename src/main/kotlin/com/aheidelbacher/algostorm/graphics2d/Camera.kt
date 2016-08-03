/*
 * Copyright 2016 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aheidelbacher.algostorm.graphics2d

/**
 * A camera representing the captured area by the screen.
 *
 * @property x the x-axis coordinate of the upper-left corner of the camera in
 * pixels
 * @property y the y-axis coordinate of the upper-left corner of the camera in
 * pixels
 * @property width the width of the camera in pixels
 * @property height the height of the camera in pixels
 */
data class Camera(val x: Int, val y: Int, val width: Int, val height: Int)
