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

package algostorm.graphics2d.animation

import algostorm.ecs.MutableEntity
import algostorm.ecs.MutableEntityManager
import algostorm.event.Subscribe
import algostorm.event.Subscriber
import algostorm.graphics2d.Sprite
import algostorm.graphics2d.Sprite.Companion.sprite
import algostorm.graphics2d.animation.Animation.Companion.animation
import algostorm.graphics2d.animation.AnimationSheet.Frame
import algostorm.time.Tick

/**
 * A system that manages the animation information of entities.
 *
 * @property entityManager the entity manager used to retrieve and update entity
 * animations
 * @property animationSet the collection which maps animation sheet ids to
 * animation sheets
 */
class AnimationSystem(
        private val entityManager: MutableEntityManager,
        private val animationSet: Map<Int, AnimationSheet>
) : Subscriber {
    private fun getSheet(sheetId: Int): AnimationSheet =
            animationSet[sheetId] ?: error("Missing sheet id $sheetId!")

    private fun Animation.tick(): Animation =
            copy(remainingTicks = remainingTicks - 1)

    private fun MutableEntity.updateSprite(frame: Frame) {
        set(Sprite.PROPERTY, sprite?.copy(
                spriteId = frame.spriteId
        ) ?: error("Can't animate entity $this without a sprite!"))
    }

    private fun MutableEntity.setAnimation(sheetId: Int, name: String) {
        val frames = getSheet(sheetId)[name]
        val newAnimation = Animation(
                sheetId = sheetId,
                name = name,
                remainingTicks = frames.sumBy { it.durationInTicks }
        )
        set(Animation.PROPERTY, newAnimation)
        updateSprite(frames.first())
    }

    private fun List<Frame>.getFrame(remainingTicks: Int): Frame {
        var ticks = remainingTicks
        for (frame in asReversed()) {
            if (frame.durationInTicks < ticks) {
                ticks -= frame.durationInTicks
            } else {
                return frame
            }
        }
        return first()
    }

    /**
     * Upon receiving a [Tick] event, it updates all the [Animation] components.
     * If any of them completes, they are continued with the
     * [AnimationSheet.IDLE] animation.
     *
     * @param event the event which signals a tick has elapsed
     */
    @Subscribe fun handleTick(event: Tick) {
        entityManager.entities.forEach { entity ->
            entity.animation?.tick()?.let { animation ->
                val (sheetId, name, remainingTicks) = animation
                val sheet = getSheet(sheetId)
                if (remainingTicks == 0) {
                    entity.setAnimation(sheetId, AnimationSheet.IDLE)
                } else {
                    entity[Animation.PROPERTY] = animation
                    entity.updateSprite(sheet[name].getFrame(remainingTicks))
                }
            }
        }
    }

    /**
     * Upon receiving an [Animate] event, it overwrites the current animation
     * with the indicated animation, or with the `IDLE` animation if the
     * indicated one doesn't exist in the animation sheet.
     *
     * Entities without `Animation` and `Sprite` components can't be animated.
     *
     * @param event the animation request
     */
    @Subscribe fun handleAnimate(event: Animate) {
        entityManager[event.entityId]?.let { entity ->
            val sheetId = entity.animation?.sheetId
                    ?: error("Can't animate entity $entity without animation!")
            entity.setAnimation(sheetId, event.animationName)
        }
    }
}
