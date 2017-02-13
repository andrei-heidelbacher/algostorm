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

package com.aheidelbacher.algostorm.systems.graphics2d

import com.aheidelbacher.algostorm.core.ecs.EntityRef.Id
import com.aheidelbacher.algostorm.core.ecs.MutableEntityGroup
import com.aheidelbacher.algostorm.core.event.Publisher
import com.aheidelbacher.algostorm.core.event.Request
import com.aheidelbacher.algostorm.core.event.Subscribe
import com.aheidelbacher.algostorm.core.event.Subscriber
import com.aheidelbacher.algostorm.systems.Update
import com.aheidelbacher.algostorm.core.engine.graphics2d.TileSet.Frame

class AnimationSystem(
        private val group: MutableEntityGroup,
        private val tileSetCollection: TileSetCollection
) : Subscriber {
    class Animate(
            val entityId: Id,
            val animation: String,
            val loop: Boolean
    ) : Request<Boolean>()

    private lateinit var animated: MutableEntityGroup

    override fun onSubscribe(publisher: Publisher) {
        animated = group.addGroup { Animation::class in it }
    }

    @Subscribe fun onAnimate(request: Animate) {
        val entity = animated[request.entityId] ?: return
        val aid = entity[Animation::class]?.aid ?: return
        val frames = tileSetCollection.getAnimation("$aid:${request.animation}")
        if (frames != null) {
            entity.set(Animation(aid, request.animation, 0, request.loop))
            //entity.set(Sprite(frames.first()))
            request.complete(true)
        } else {
            request.complete(false)
        }
    }

    @Subscribe fun onUpdate(event: Update) {
        animated.entities.forEach {
            val animation = it[Animation::class] ?: error("")
            val frames = tileSetCollection.getAnimation(
                    animation = "${animation.aid}:${animation.animation}"
            ) ?: error("")
            val totalDuration = frames.sumBy(Frame::duration)
            val elapsedMillis = animation.elapsedMillis + event.elapsedMillis
            if (elapsedMillis >= totalDuration && animation.loop) {

            }
            it.set(animation.copy(elapsedMillis = elapsedMillis))
            var t = elapsedMillis
            var i = 0
            do {
                t -= frames[i].duration
                i++
            } while (t >= 0 && i < frames.size)
            it.set(it.sprite?.copy(gid = frames[i - 1].tileId) ?: error(""))
        }
    }
}
