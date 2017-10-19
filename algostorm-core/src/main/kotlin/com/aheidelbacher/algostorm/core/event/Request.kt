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
 * An abstract request which should be completed by a service that subscribed
 * for a certain type of requests.
 *
 * Concrete implementations must be non-generic. The type of a request is
 * denoted by its kotlin class object. Excepting the result, requests should be
 * immutable classes. Requests can't be serialized.
 *
 * @param T the result type of this request
 */
abstract class Request<T> {
    private var result: T? = null

    /**
     * Returns the value with which this request was completed.
     *
     * @return the result of this request
     * @throws IllegalStateException if this request is not completed
     */
    @Suppress("unchecked_cast")
    fun get(): T {
        check(isCompleted) { "$this was not completed!" }
        return result as T
    }

    /**
     * Completes this request with the given `value`.
     *
     * @param value the value with which this request should be completed
     * @throws IllegalStateException if this request is already completed
     */
    fun complete(value: T) {
        check(!isCompleted) { "$this was already completed!" }
        result = value
        isCompleted = true
    }

    /** The status of this request. */
    var isCompleted: Boolean = false
        private set
}
