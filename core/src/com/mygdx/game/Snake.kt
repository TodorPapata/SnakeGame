package com.mygdx.game

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array
import com.mygdx.game.Direction.*

const val SNAKE_MOVEMENT = 32

class Snake(val head: Head, var direction: Direction) {
    var snakeXBeforeUpdate = 0
    var snakeYBeforeUpdate = 0
    var body: Array<BodyPart> = Array()

    fun moveSnake() {
        moveHead()
        moveBody()
    }

    private fun moveHead() {
        snakeXBeforeUpdate = head.x
        snakeYBeforeUpdate = head.y
        when (direction) {
            RIGHT -> head.x += SNAKE_MOVEMENT
            LEFT -> head.x -= SNAKE_MOVEMENT
            UP -> head.y += SNAKE_MOVEMENT
            DOWN -> head.y -= SNAKE_MOVEMENT
        }
    }

    private fun moveBody() {
        if (body.size > 0) {
            val bodyPart = body.removeIndex(0)
            bodyPart.updateBodyPosition(snakeXBeforeUpdate, snakeYBeforeUpdate)
            body.add(bodyPart)
        }
    }

    fun extendBody(snakeBody: TextureRegion) {
        val bodyPart = BodyPart(snakeBody)
        bodyPart.updateBodyPosition(head.x, head.y)
        body.insert(0, bodyPart)
    }

    fun draw(batch: Batch) {
        head.draw(batch, direction)
        for (bodyPart in body) {
            bodyPart.draw(batch)
        }
    }

    inner class BodyPart(private val texture: TextureRegion, var x: Int, var y: Int) {

        constructor(texture: TextureRegion) : this(texture, 0, 0)

        fun updateBodyPosition(x: Int, y: Int) {
            this.x = x
            this.y = y
        }

        fun draw(batch: Batch) {
            if (!(x == head.x && y == head.y)) {
                batch.draw(texture, x.toFloat(), y.toFloat(), 16f, 16f, 32f, 32f, 1f, 1f, direction.rotationAngle.toFloat())
            }
        }
    }
}

class Head(var texture: TextureRegion, var x: Int, var y: Int) {
    constructor(texture: TextureRegion) : this(texture, 0, 0) {
        this.texture = texture
    }

    fun updateHeadPosition(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    fun draw(batch: Batch, direction: Direction) {
        batch.draw(texture, x.toFloat(), y.toFloat(), 16f, 16f, 32f, 32f, 1f, 1f, direction.rotationAngle.toFloat())
    }
}

