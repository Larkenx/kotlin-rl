package com.larken.roguelike.map

import com.badlogic.gdx.graphics.Color
import com.halfdeadgames.kterminal.KTerminalGlyph
import com.larken.roguelike.actors.Entity
import squidpony.squidgrid.mapping.*

data class Obstacle(val glyph: KTerminalGlyph, val name: String, val walkable: Boolean)

val wall: Obstacle = Obstacle(
        KTerminalGlyph('#', Color.LIGHT_GRAY, Color.BLACK),
        "Wall",
        false
)

val floor: Obstacle = Obstacle(
        KTerminalGlyph('.', Color.BROWN, Color.BLACK),
        "Floor",
        true
)



class Tile(val x: Int, val y: Int) {
    val entities: ArrayList<Entity> = ArrayList<Entity>()
    val obstacles: ArrayList<Obstacle> = ArrayList<Obstacle>()

    val blocked: Boolean get() = obstacles.any {o -> !o.walkable}
    fun addObstacle(obstacle: Obstacle) {
        obstacles.add(obstacle)
    }

    fun addEntity(entity: Entity) {
        entities.add(entity)
    }

    fun removeEntity(entity: Entity) {
        entities.remove(entity)
    }

    fun removeObstacle(obstacle: Obstacle) {
        obstacles.remove(obstacle)
    }
}

class GameMap(val width: Int, val height: Int) {
    val data: ArrayList<ArrayList<Tile>> = ArrayList<ArrayList<Tile>>()

    val tiles: ArrayList<Tile> get() = ArrayList(data.flatten())

    init {
        for (y in 0..height) {
            val row: ArrayList<Tile> = ArrayList<Tile>()
            for (x in 0..width) {
                row.add(Tile(x, y))
            }
            data.add(row)
        }
    }

    val actors: ArrayList<Entity>
        get() {
            val allEntities: ArrayList<Entity> = ArrayList<Entity>()
            data.forEach { row -> row.forEach { tile -> allEntities.addAll(tile.entities) } }
            return allEntities
        }

    fun tileAt(x: Int, y: Int): Tile {
        return data.get(y).get(x)
    }

    fun print() {
        for (row in data) {
            for (tile in row) {
                if (tile.obstacles.size > 0)
                    print(tile.obstacles.get(0).glyph.char)
                else
                    print(" ")
            }
            print("\n")
        }
    }

    fun addBox() {
        // Add obstacles to the game map
        for (y in 0..height) {
            for (x in 0..width) {
                val tile = tileAt(x, y)
                if (x == 0 || y == 0 || x == width || y == height) {
                    tile.addObstacle(wall)
                } else {
                    tile.addObstacle(floor)
                }
            }
        }
    }

    fun generateRogueDungeon() {
        val mapGenerator: ClassicRogueMapGenerator = ClassicRogueMapGenerator(4, 5, width, height, 5, 10, 5, 10)
        val generatedMap: Array<CharArray> = mapGenerator.generate()
        println(generatedMap.size)
        println(generatedMap[0].size)

        var playerNotPlaced: Boolean = true
        for (y in 0..height - 1) {
            for (x in 0..width - 1) {
                val character: Char = generatedMap.get(y).get(x)
                if (character == '#') {
                    tileAt(x, y).addObstacle(wall)
                } else {
                    tileAt(x, y).addObstacle(floor)
                }
            }
        }
    }

    fun pacMazeDungeon() {
        val mapGenerator: PacMazeGenerator = PacMazeGenerator(width, height)
        val generatedMap: Array<CharArray> = mapGenerator.generate()
        println(generatedMap.size)
        println(generatedMap[0].size)

        var playerNotPlaced: Boolean = true
        for (y in 0..height - 1) {
            for (x in 0..width - 1) {
                val character: Char = generatedMap.get(y).get(x)
                if (character == '#') {
                    tileAt(x, y).addObstacle(wall)
                } else {
                    tileAt(x, y).addObstacle(floor)
                }
            }
        }
    }

}