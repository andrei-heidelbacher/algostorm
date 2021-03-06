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

package com.andreihh.algostorm.core.ecs

/**
 * An abstract component which holds data about a certain aspect of the game.
 *
 * All components should be immutable and final data classes. The type of a
 * component is denoted by its kotlin class object. Generic components are not
 * allowed.
 *
 * Components must be serializable.
 *
 * Components can only contain fields of valid types, which are:
 * - primitive types and their boxed equivalents
 * - [String]
 * - immutable collections of valid types (the generics must be specified)
 * - immutable data classes composed of valid types
 */
interface Component
