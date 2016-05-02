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

package algostorm.physics2d

import algostorm.ecs.Component
import algostorm.ecs.Entity

/**
 * A component which tells that the owner entity will block translations.
 */
object Rigid : Component {
  /**
   * Returns `true` if this entity contains the [Rigid] component, `false` otherwise.
   */
  val Entity.isRigid: Boolean
    get() = contains<Rigid>()
}
