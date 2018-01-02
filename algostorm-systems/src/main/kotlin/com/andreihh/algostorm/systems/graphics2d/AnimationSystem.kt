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

package com.andreihh.algostorm.systems.graphics2d

import com.andreihh.algostorm.core.ecs.EntityGroup
import com.andreihh.algostorm.core.ecs.EntityRef
import com.andreihh.algostorm.core.ecs.EntityRef.Id
import com.andreihh.algostorm.core.event.Request
import com.andreihh.algostorm.core.event.Subscribe
import com.andreihh.algostorm.systems.Update
import com.andreihh.algostorm.systems.graphics2d.TileSet.Frame

class AnimationSystem : GraphicsSystem() {
    private val entities: EntityGroup by context(ENTITY_POOL)
    private val animated get() = entities.filter { Animation::class in it }

    class Animate(
            val entityId: Id,
            val animation: String,
            val loop: Boolean
    ) : Request<Unit>()

    @Subscribe
    fun onAnimate(request: Animate) {
        val entity = animated[request.entityId] ?: return
        val animation = entity[Animation::class]?.copy(
                name = request.animation,
                elapsedMillis = 0,
                loop = request.loop
        ) ?: return
        val frames = tileSetCollection.getAnimation(animation.animation)
        if (frames != null) {
            val sprite = entity.sprite?.copy(gid = frames.first().tileId)
                    ?: return
            entity.set(animation)
            entity.set(sprite)
            request.complete(Unit)
        }
    }

    private fun EntityRef.update(deltaMillis: Int) {
        val animation = get(Animation::class) ?: return
        val frames = tileSetCollection.getAnimation(animation.animation)
                ?: return
        val totalDuration = frames.sumBy(Frame::duration)
        val elapsedMillis = animation.elapsedMillis + deltaMillis
        if (elapsedMillis >= totalDuration && animation.loop) {
            set(animation.copy(elapsedMillis = elapsedMillis % totalDuration))
        } else {
            set(animation.copy(elapsedMillis = Math.min(elapsedMillis, totalDuration)))
        }
        var t = elapsedMillis
        var i = 0
        do {
            t -= frames[i].duration
            i++
        } while (t >= 0 && i < frames.size)
        set(sprite?.copy(gid = frames[i - 1].tileId) ?: return)
    }

    @Subscribe
    fun onUpdate(event: Update) {
        animated.forEach { it.update(event.elapsedMillis) }
    }
}
