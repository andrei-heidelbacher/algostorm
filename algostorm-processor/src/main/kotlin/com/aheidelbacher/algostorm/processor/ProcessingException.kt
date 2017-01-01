/*
 * Copyright 2017 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.aheidelbacher.algostorm.processor

import javax.lang.model.element.Element

/**
 * Indicates that an error occurred while processing the specified `element`.
 *
 * @constructor builds a new exception with the given `element` and `message`
 * @property element the element which caused the error
 * @param message the error message
 */
class ProcessingException(
        val element: Element?,
        message: String
) : Exception(message)
