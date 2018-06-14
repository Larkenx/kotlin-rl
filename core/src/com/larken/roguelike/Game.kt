package com.larken.roguelike

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.halfdeadgames.kterminal.KTerminalData
import com.halfdeadgames.kterminal.KTerminalGlyph
import com.halfdeadgames.kterminal.KTerminalRenderer
import com.larken.roguelike.actors.Entity
import com.larken.roguelike.map.*
import ktx.app.clearScreen
import ktx.app.use
import squidpony.squidgrid.mapping.ClassicRogueMapGenerator

class Scheduler {
    val actors: ArrayList<Entity> = ArrayList<Entity>()
    fun addAllActors(newActors: ArrayList<Entity>) {
        actors.addAll(newActors)
    }

    fun removeActor(actor: Entity) {
        actors.remove(actor)
    }

    val getTurnOrder: Iterator<Entity> get() = actors.iterator()
}

class Game : ApplicationAdapter() {
    lateinit var spriteBatch: SpriteBatch
    lateinit var terminalData: KTerminalData
    lateinit var terminalRenderer: KTerminalRenderer
    lateinit var inputAdapter: InputHandler
    lateinit var gameMap: GameMap
    lateinit var scheduler: Scheduler
    lateinit var player: Entity

    fun tileAt(x: Int, y: Int): Tile {
        return gameMap.tileAt(x, y)
    }

    override
    fun create() {
        /* Initialize the terminal renderer */
        val width: Int = 50
        val height: Int = 26
        spriteBatch = SpriteBatch()
        terminalData = KTerminalData(width, height, Color.WHITE, Color.BLACK)
        terminalRenderer = KTerminalRenderer("fontSheet.png", 1f, spriteBatch)
        inputAdapter = InputHandler(this)
        Gdx.input.inputProcessor = inputAdapter
        /* Create the game map, actors, and scheduler */
        gameMap = GameMap(width, height)
        val mapGenerator: ClassicRogueMapGenerator = ClassicRogueMapGenerator(4, 5, 100, 100, 5, 10, 5, 10)
        val generatedMap: Array<CharArray> = mapGenerator.generate()
        for (y in 0..gameMap.height) {
            for (x in 0..gameMap.width) {
                val character: Char = generatedMap.get(y).get(x)
                if (character == '#') {
                    tileAt(x, y).addObstacle(wall)
                } else {
                    tileAt(x, y).addObstacle(floor)
                }
            }
        }
        /* Create player */
        val px = width / 2
        val py = height / 2
        player = Entity(px, py, "Larken", KTerminalGlyph('@', Color.CYAN, Color.BLACK))
        tileAt(px, py).addEntity(player)
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
                val tile = tileAt(x, y)
                for (obstacle in tile.obstacles) {
                    terminalData[x, y].write(obstacle.glyph)
                }

                for (actor in tile.entities) {
                    terminalData[x, y].write(actor.glyph)
                }
            }
        }
    }

    override
    fun dispose() {
        spriteBatch.dispose()
        terminalRenderer.dispose()
    }

    fun inbounds(x: Int, y: Int): Boolean {
        return x >= 0 && x <= gameMap.width && y >= 0 && y <= gameMap.height
    }

    fun moveEntity(entity: Entity, x: Int, y: Int) {
        if (inbounds(x, y)) {
            val targetTile = tileAt(x, y)
            if (!targetTile.blocked) {
                val previousTile = tileAt(entity.x, entity.y)
                previousTile.removeEntity(entity)
                entity.moveTo(x, y)
                targetTile.addEntity(entity)
            }
        }
    }
}

