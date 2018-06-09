package com.larken.roguelike

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.halfdeadgames.kterminal.KTerminalData
import com.halfdeadgames.kterminal.KTerminalRenderer
import ktx.app.clearScreen
import ktx.app.use

class Game : ApplicationAdapter() {
    lateinit var spriteBatch: SpriteBatch
    lateinit var terminalData: KTerminalData
    lateinit var terminalRenderer: KTerminalRenderer
    val inputAdapter: InputHandler = InputHandler()

    override
    fun create() {
        spriteBatch = SpriteBatch()
        terminalData = KTerminalData(50, 26, Color.WHITE, Color.BLACK)
        terminalRenderer = KTerminalRenderer("fontSheet.png", 1f, spriteBatch)
        Gdx.input.inputProcessor = inputAdapter
        inputAdapter.selector.set(25, 13) // set position to be center of the screen
    }

    override
    fun render() {
        clearScreen(0f, 0f, 0f, 1f)
        terminalData.clearAll()
        drawGame()
        spriteBatch.use {
            terminalRenderer.render(0f, 0f, terminalData)
        }

    }

    fun drawGame() {
        terminalData.resetCursor()
        terminalData[0,0].drawBox(
                width = 50,
                height = 26,
                topRight = KTerminalData.BOX_DOUBLE_DOWN_LEFT,
                bottomLeft = KTerminalData.BOX_DOUBLE_UP_RIGHT,
                bottomRight = KTerminalData.BOX_DOUBLE_UP_LEFT,
                topLeft = KTerminalData.BOX_DOUBLE_DOWN_RIGHT,
                horizontal = KTerminalData.BOX_DOUBLE_HORIZONTAL,
                vertical = KTerminalData.BOX_DOUBLE_VERTICAL
        )
        terminalData[1, 1].write("Hello world, it's raining!")
        val (x, y) = inputAdapter.selector
        terminalData[x, y].write('@')

    }

    override
    fun dispose() {
        spriteBatch.dispose()
        terminalRenderer.dispose()
    }
}

