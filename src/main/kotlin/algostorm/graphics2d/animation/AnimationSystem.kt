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

import algostorm.ecs.EntitySystem
import algostorm.ecs.MutableEntity
import algostorm.ecs.MutableEntityManager
import algostorm.event.Subscriber
import algostorm.graphics2d.Sprite.Companion.sprite
import algostorm.graphics2d.animation.Animation.Companion.animation
import algostorm.graphics2d.animation.AnimationSheet.Frame
import algostorm.time.Tick

/**
 * A system that manages the animation information of entities.
 *
 * Upon receiving a [Tick] event, it updates all the [Animation] components. If
 * any of them completes, they are continued with the [AnimationSheet.IDLE]
 * animation.
 *
 * Upon receiving an [Animate] event, it overwrites the current animation with
 * the indicated animation, or with the `IDLE` animation if the indicated one
 * doesn't exist in the animation sheet.
 *
 * Entities without `Animation` and `Sprite` components can't be animated.
 *
 * @property entityManager the entity manager used to retrieve and update entity
 * animations
 * @property properties the properties of the game
 */
class AnimationSystem(
        private val entityManager: MutableEntityManager,
        private val properties: Map<String, Any>
) : EntitySystem {
    companion object {
        /**
         * The name of the property used by this system. It should be an object
         * of type [AnimationSet].
         */
        const val ANIMATION_SET: String = "animationSet"
    }

    private val animationSet: AnimationSet
        get() = properties[ANIMATION_SET] as AnimationSet

    private fun getSheet(sheetId: Int): AnimationSheet =
            animationSet[sheetId] ?: error("Missing sheet id!")

    private fun Animation.tick(): Animation =
            copy(remainingTicks = remainingTicks - 1)

    private fun MutableEntity.updateSprite(frame: Frame) {
        set(sprite?.copy(
                tileId = frame.tileId,
                flippedHorizontally = frame.flippedHorizontally,
                flippedVertically = frame.flippedVertically,
                flippedDiagonally = frame.flippedDiagonally
        ) ?: error("Can't animate entity without a sprite!"))
    }

    private fun MutableEntity.setAnimation(sheetId: Int, name: String) {
        val frames = getSheet(sheetId)[name]
        set(Animation(sheetId, name, frames.sumBy { it.durationInTicks }))
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

    private val tickHandler = Subscriber(Tick::class) { event ->
        entityManager.filterEntities(Animation::class).forEach { entity ->
            val animation = entity.animation?.tick()
                    ?: error("Entity is missing animation component!")
            val (sheetId, name, remainingTicks) = animation
            val sheet = getSheet(sheetId)
            if (remainingTicks == 0) {
                entity.setAnimation(sheetId, AnimationSheet.IDLE)
            } else {
                entity.set(animation)
                entity.updateSprite(sheet[name].getFrame(remainingTicks))
            }
        }
    }

    private val animateHandler = Subscriber(Animate::class) { event ->
        entityManager[event.entityId]?.let { entity ->
            val sheetId = entity.animation?.sheetId
                    ?: error("Can't animate entity without animation!")
            entity.setAnimation(sheetId, event.animationName)
        }
    }

    /**
     * This system handles [Tick] and [Animate] events.
     */
    override val handlers: List<Subscriber<*>> =
            listOf(tickHandler, animateHandler)
}
