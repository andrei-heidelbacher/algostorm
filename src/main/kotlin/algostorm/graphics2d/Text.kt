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

package algostorm.graphics2d

import algostorm.assets.FontId
import algostorm.ecs.Component

/**
 * A component that contains text information that should be rendered.
 *
 * @property text the text that should be rendered
 * @property fontId the font that should be used
 * @property size the size of the text
 * @property color the color of the text
 */
data class Text(val text: String, val fontId: FontId, val size: Int, val color: Color) : Component
