package com.larken.roguelike

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.halfdeadgames.kterminal.KTerminalData
import com.halfdeadgames.kterminal.KTerminalGlyph
import com.halfdeadgames.kterminal.KTerminalRenderer
import com.larken.roguelike.actors.Entity
import com.larken.roguelike.map.GameMap
import com.larken.roguelike.map.Tile
import com.larken.roguelike.map.floor
import com.larken.roguelike.map.wall
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
    var width = 50
    var height = 26

    fun tileAt(x: Int, y: Int): Tile {
        return gameMap.tileAt(x, y)
    }

    override
    fun create() {
        /* Initialize the terminal renderer */
        spriteBatch = SpriteBatch()
        terminalData = KTerminalData(width, height, Color.WHITE, Color.BLACK)
        terminalRenderer = KTerminalRenderer("fontSheet.png", 1f, spriteBatch)
        inputAdapter = InputHandler(this)
        Gdx.input.inputProcessor = inputAdapter
        /* Create the game map, actors, and scheduler */
        val mapWidth = 100
        val mapHeight = 100
        gameMap = GameMap(mapWidth, mapHeight)
        val mapGenerator: ClassicRogueMapGenerator = ClassicRogueMapGenerator(4, 5, mapWidth, mapHeight, 5, 10, 5, 10)
        val generatedMap: Array<CharArray> = mapGenerator.generate()
        println(generatedMap.size)
        println(generatedMap[0].size)

        var playerNotPlaced: Boolean = true
        for (y in 0..gameMap.height - 1) {
            for (x in 0..gameMap.width - 1) {
                val character: Char = generatedMap.get(y).get(x)
                if (character == '#') {
                    tileAt(x, y).addObstacle(wall)
                } else {
                    tileAt(x, y).addObstacle(floor)
                    if (playerNotPlaced) {
                        /* Create player */
                        player = Entity(x, y, "Larken", KTerminalGlyph('@', Color.CYAN, Color.BLACK))
                        tileAt(x, y).addEntity(player)
                        playerNotPlaced = false
                    }
                }
            }
        }

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

    /*
    		let camera = {
			// camera x,y resides in the upper left corner
			x: Game.player.x - ~~(Game.width / 2),
			y: Game.player.y - ~~(Game.height / 2),
			width: Math.ceil(Game.width),
			height: Game.height
		}
		let startingPos = [camera.x, camera.y]
		if (camera.x < 0) {
			// far left
			startingPos[0] = 0
		}
		if (camera.x + camera.width > Game.map.width) {
			// far right
			startingPos[0] = Game.map.width - camera.width
		}
		if (camera.y <= 0) {
			// at the top of the map
			startingPos[1] = 0
		}
		if (camera.y + camera.height > Game.map.height) {
			// at the bottom of the map
			startingPos[1] = Game.map.height - camera.height
		}
		let endingPos = [startingPos[0] + camera.width, startingPos[1] + camera.height]

     */

    fun drawGame() {
        terminalData.resetCursor()
        var cameraX = player.x - (width / 2)
        var cameraY = player.y - (height / 2)
        var startingPos: IntArray = intArrayOf(cameraX, cameraY)
        if (cameraX < 0) startingPos[0] = 0
        if (cameraX + width > gameMap.width) startingPos[0] = gameMap.width - width
        if (cameraY < 0) startingPos[1] = 0
        if (cameraY + height > gameMap.height) startingPos[1] = gameMap.height - height
        val endingPos: IntArray = intArrayOf(startingPos[0] + width, startingPos[1] + height)

        var dx: Int = 0
        var dy: Int = 0
        for (y in startingPos[1]..endingPos[1]) {
            for (x in startingPos[0]..endingPos[0]) {
                val tile = tileAt(x, y)
                for (obstacle in tile.obstacles) {
                    terminalData[dx, dy].write(obstacle.glyph)
                }

                for (actor in tile.entities) {
                    terminalData[dx, dy].write(actor.glyph)
                }
                dx++
            }
            dy++
            dx = 0
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

