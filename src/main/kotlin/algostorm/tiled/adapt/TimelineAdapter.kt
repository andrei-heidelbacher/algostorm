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

package algostorm.tiled.adapt

import algostorm.tiled.json.Layer
import algostorm.tiled.json.Object
import algostorm.tiled.json.TiledMap
import algostorm.time.Timeline
import algostorm.time.Timer

/**
 * A concrete implementation of a timeline that wraps a Tiled map. Changes made
 * to the timeline will be reflected in the Tiled map.
 *
 * @param tiledMap the underlying Tiled map
 * @throws IllegalArgumentException if the given [tiledMap] doesn't contain a
 * [Layer.ObjectGroup] with the name [TIMELINE_LAYER_NAME]
 */
class TimelineAdapter(private val tiledMap: TiledMap) : Timeline {
    companion object {
        /**
         * The name of the [Layer.ObjectGroup] which contains the timers.
         */
        const val TIMELINE_LAYER_NAME: String = "timeline"

        /**
         * The name of the [Timer] property of the layer objects.
         */
        const val TIMER_PROPERTY: String = "timer"
    }

    private val objectGroup = requireNotNull(tiledMap.layers.find {
        it.name == TIMELINE_LAYER_NAME
    } as? Layer.ObjectGroup) { "Tiled map doesn't contain timeline layer!" }

    private var Object.timer: Timer?
        get() = properties[TIMER_PROPERTY] as Timer?
        set(value) {
            if (value != null) {
                properties[TIMER_PROPERTY] = value
            } else {
                properties.remove(TIMER_PROPERTY)
            }
        }

    override fun registerTimer(timer: Timer) {
        val id = tiledMap.nextObjectId
        val timerObject = Object(id)
        tiledMap.nextObjectId++
        timerObject.properties[TIMER_PROPERTY] = timer
        objectGroup.objects.add(timerObject)
    }

    override fun tick(): List<Timer> {
        objectGroup.objects.forEach { timerObject ->
            timerObject.timer?.let { timer ->
                timerObject.timer = timer.copy(
                        remainingTicks = timer.remainingTicks - 1
                )
            }
        }
        val expired = objectGroup.objects.filter {
            it.timer?.remainingTicks == 0
        }
        objectGroup.objects.removeAll(expired)
        return expired.mapNotNull { it.timer }
    }
}
