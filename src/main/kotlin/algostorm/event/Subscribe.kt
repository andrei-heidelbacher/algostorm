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
 * Annotation to mark a method of a [Subscriber] as an event handler.
 *
 * An event handler is a method that is annotated with the [Subscribe]
 * annotation, is public, returns [Unit] / void and receives a single parameter
 * which is a subtype of [Event].
 *
 * Event-handling methods which are inherited are also registered for events
 * upon subscription. Methods which are not public are ignored.
 */
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class Subscribe
