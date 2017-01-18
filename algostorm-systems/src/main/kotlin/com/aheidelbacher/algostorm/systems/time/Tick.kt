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

package com.aheidelbacher.algostorm.systems.time

import com.aheidelbacher.algostorm.core.event.Event

/**
 * An event which signals an atomic time unit has passed.
 *
 * @property elapsedMillis the number of milliseconds of an atomic time unit
 */
data class Tick(val elapsedMillis: Int) : com.aheidelbacher.algostorm.core.event.Event
