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

package com.aheidelbacher.algostorm.test.engine.input

import com.aheidelbacher.algostorm.core.engine.input.AbstractInputDriver

class InputDriverMock : AbstractInputDriver() {
    fun touch(x: Int, y: Int) {
        notify { onTouch(x, y) }
    }

    fun key(keyCode: Int) {
        notify { onKey(keyCode) }
    }

    fun scroll(dx: Int, dy: Int) {
        notify { onScroll(dx, dy) }
    }
}
