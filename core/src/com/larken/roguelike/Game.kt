package com.larken.roguelike

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.halfdeadgames.kterminal.KTerminalData
import com.halfdeadgames.kterminal.KTerminalRenderer
import com.larken.roguelike.map.GameMap
import com.larken.roguelike.map.Glyph
import com.larken.roguelike.map.Obstacle
import ktx.app.clearScreen
import ktx.app.use

class Game : ApplicationAdapter() {
    lateinit var spriteBatch: SpriteBatch
    lateinit var terminalData: KTerminalData
    lateinit var terminalRenderer: KTerminalRenderer
    val inputAdapter: InputHandler = InputHandler()
    lateinit var gameMap: GameMap

    override
    fun create() {
        val width: Int = 50
        val height: Int = 26
        spriteBatch = SpriteBatch()
        terminalData = KTerminalData(width, height, Color.WHITE, Color.BLACK)
        terminalRenderer = KTerminalRenderer("fontSheet.png", 1f, spriteBatch)
        Gdx.input.inputProcessor = inputAdapter
        inputAdapter.selector.set(25, 13) // set position to be center of the screen
        gameMap = GameMap(width-1, height-1)
        gameMap.addBox()
        gameMap.print()
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
        for (y in 0..gameMap.height) {
            for (x in 0..gameMap.width) {
                val glyph = gameMap.tileAt(x, y).obstacles.get(0).glyph
//                println("Writing " + glyph.character + " at " + x.toString() + "," + y.toString())
                terminalData[x, y].write(glyph.character)
            }
        }
        val (x, y) = inputAdapter.selector
        terminalData[x, y].write('@')

    }

    override
    fun dispose() {
        spriteBatch.dispose()
        terminalRenderer.dispose()
    }
}

