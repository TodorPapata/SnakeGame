package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color.BLACK
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.mygdx.game.Direction.*
import com.mygdx.game.State.GAME_OVER
import com.mygdx.game.State.PLAYING

const val MOVE_TIME = 0.2f

class GameScreen : ScreenAdapter() {
    private var state: State = PLAYING
    private var timer = MOVE_TIME

    private var directionSet = false
    private var appleAvailable = false
    private var appleX: Int = 0
    private var appleY: Int = 0

    lateinit private var batch: SpriteBatch
    lateinit private var img: Texture
    lateinit private var snakeHeadImage: TextureRegion
    lateinit private var apple: TextureRegion
    lateinit private var snakeBodyImage: TextureRegion

    lateinit private var snake: Snake
    lateinit private var snakeHead: Head

    lateinit private var glyphLayout: GlyphLayout
    lateinit private var bitmapFont: BitmapFont

    override fun show() {
        batch = SpriteBatch()
        glyphLayout = GlyphLayout()
        bitmapFont = BitmapFont()
        img = Texture("snake.png")
        snakeHeadImage = TextureRegion(img, 8, 24, 8, 8)
        snakeBodyImage = TextureRegion(img, 40, 24, 8, 8)
        apple = TextureRegion(img, 48, 0, 8, 8)
        snakeHead = Head(snakeHeadImage)

        snake = Snake(snakeHead, RIGHT)
    }

    override fun render(delta: Float) {
        clearScreen()
        when (state) {
            PLAYING -> {
                queryInput()
                checkAppleCollision()
                update(delta)
                draw()
            }
            GAME_OVER -> {
                batch.begin()
                glyphLayout.setText(bitmapFont, "GAME OVER")
                bitmapFont.draw(batch, "GAME OVER",
                        (480 - glyphLayout.width) / 2,
                        (800 - glyphLayout.height) / 2)
                batch.end()
            }
        }
    }

    private fun queryInput() {
        val lPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT)
        val rPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT)
        val uPressed = Gdx.input.isKeyPressed(Input.Keys.UP)
        val dPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN)

        if (!directionSet) {
            snake.direction = if (lPressed && snake.direction != RIGHT) {
                directionSet = true
                LEFT
            } else if (rPressed && snake.direction != LEFT) {
                directionSet = true
                RIGHT
            } else if (uPressed && snake.direction != DOWN) {
                directionSet = true
                UP
            } else if (dPressed && snake.direction != UP) {
                directionSet = true
                DOWN
            } else snake.direction
        }
    }

    private fun update(delta: Float) {
        timer -= delta
        if (timer <= 0) {
            timer = MOVE_TIME
            snake.moveSnake()
            checkForOutOfBounds()
            checkSnakeBodyCollision()
            directionSet = false
        }
    }

    private fun checkForOutOfBounds() {
        if (snake.head.x >= Gdx.graphics.width) {
            snake.head.x = 0
        }
        if (snake.head.x < 0) {
            snake.head.x = Gdx.graphics.width - SNAKE_MOVEMENT
        }
        if (snake.head.y >= Gdx.graphics.height) {
            snake.head.y = 0
        }
        if (snake.head.y < 0) {
            snake.head.y = Gdx.graphics.height - SNAKE_MOVEMENT
        }
    }

    private fun clearScreen() {
        Gdx.gl.glClearColor(BLACK.r, BLACK.g, BLACK.b, BLACK.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    private fun draw() {
        batch.begin()
        snake.draw(batch)
        if (!appleAvailable) {
            generateApple()
        }
        batch.draw(apple, appleX.toFloat(), appleY.toFloat(), 16f, 16f, 32f, 32f, 1f, 1f, 0f)
        batch.end()
    }

    private fun generateApple() {
        do {
            appleX = MathUtils.random(Gdx.graphics.width / SNAKE_MOVEMENT - 1) * SNAKE_MOVEMENT
            appleY = MathUtils.random(Gdx.graphics.height / SNAKE_MOVEMENT - 1) * SNAKE_MOVEMENT
            appleAvailable = true
        } while (appleX == snake.head.x && appleY == snake.head.y)
    }

    private fun checkAppleCollision() {
        if (appleAvailable && appleX == snake.head.x && appleY == snake.head.y) {
            snake.extendBody(snakeBodyImage)
            appleAvailable = false
        }
    }

    private fun checkSnakeBodyCollision() {
        for (bodyPart in snake.body) {
            if (bodyPart.x == snake.head.x && bodyPart.y == snake.head.y) state = GAME_OVER
        }
    }

    override fun dispose() {
        batch.dispose()
        img.dispose()
    }
}

enum class Direction(val rotationAngle: Int) {
    LEFT(90),
    RIGHT(-90),
    UP(0),
    DOWN(180)
}

enum class State {
    PLAYING,
    GAME_OVER;
}
