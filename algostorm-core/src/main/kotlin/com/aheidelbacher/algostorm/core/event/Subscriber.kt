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

package com.aheidelbacher.algostorm.core.event

/**
 * Marker interface for objects that wish to register event handling methods to
 * an event bus.
 *
 * To mark a method as an event handler, annotate it with [Subscribe].
 */
interface Subscriber {
    /**
     * Initializes resources after this subscriber subscribed to the given
     * `publisher`.
     *
     * @param publisher the publisher to which this object was subscribed
     */
    fun onSubscribe(publisher: Publisher) {}

    /**
     * Releases resources after this subscriber unsubscribed from the given
     * `publisher`.
     *
     * @param publisher the publisher from which this object was unsubscribed
     */
    fun onUnsubscribe(publisher: Publisher) {}
}
