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

package algostorm.event

/**
 * Allows a [Subscriber] to cancel its subscription to an [EventBus].
 */
interface Subscription {
    /**
     * Cancels the subscription. Can only be called once.
     *
     * @throws IllegalStateException if the subscription is cancelled more than
     * once
     */
    fun unsubscribe(): Unit
}
