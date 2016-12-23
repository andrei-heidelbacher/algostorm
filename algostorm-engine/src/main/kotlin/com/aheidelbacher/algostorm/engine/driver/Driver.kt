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

package com.aheidelbacher.algostorm.engine.driver

/**
 * A core component of the engine which offers specialized, lower level
 * services.
 */
interface Driver {
    /**
     * Releases all resources acquired by this driver.
     *
     * This method should be idempotent.
     *
     * Invoking any other service after this driver was released may lead to
     * undefined behavior.
     */
    fun release(): Unit
}
