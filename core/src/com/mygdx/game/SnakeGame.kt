package com.mygdx.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion

class SnakeGame : Game() {

    internal var batch: SpriteBatch? = null
    internal var img: Texture? = null
    internal var snakeHead: TextureRegion? = null

    override fun create() {
        setScreen(GameScreen())
    }

    override fun render() {
        super.render()
    }

    override fun dispose() {
        screen.dispose()
    }
}
