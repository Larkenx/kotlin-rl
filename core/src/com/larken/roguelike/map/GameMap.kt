package com.larken.roguelike.map

import com.larken.roguelike.actors.Entity

data class Glyph(val character: String, val foreground: String, val background: String = "transparent")
data class Obstacle(val glyph: Glyph, val name: String, val walkable: Boolean)

class Tile(val x: Int, val y: Int) {
    val entities: ArrayList<Entity> = ArrayList<Entity>()
    val obstacles: ArrayList<Obstacle> = ArrayList<Obstacle>()

    fun addObstacle(obstacle: Obstacle) {
        obstacles.add(obstacle)
    }

    fun addEntity(entity: Entity) {
        entities.add(entity)
    }

    fun removeActor(entity: Entity) {
        entities.remove(entity)
    }

    fun removeObstacle(obstacle: Obstacle) {
        obstacles.remove(obstacle)
    }
}

class GameMap(val width: Int, val height: Int) {
    val data: ArrayList<ArrayList<Tile>> = ArrayList<ArrayList<Tile>>()
    init {
        for (y in 0..height) {
            val row: ArrayList<Tile> = ArrayList<Tile>()
            for (x in 0..width) {
                row.add(Tile(x, y))
            }
            data.add(row)
        }
    }

    val actors: ArrayList<Entity> get() {
        val allEntities: ArrayList<Entity> = ArrayList<Entity>()
        data.forEach {row -> row.forEach { tile -> allEntities.addAll(tile.entities)}}
        return allEntities
    }

    fun tileAt(x: Int, y: Int): Tile {
        return data.get(y).get(x)
    }

    fun print() {
        for (row in data) {
            for (tile in row) {
                print(tile.obstacles.get(0).glyph.character)
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
                    tile.addObstacle(Obstacle(
                            Glyph("#", "white"),
                            "Wall",
                            false
                    ))
                } else {
                    tile.addObstacle(Obstacle(
                            Glyph(".", "brown"),
                            "Floor",
                            true
                    ))
                }
            }
        }
    }
}