package com.larken.roguelike

import com.badlogic.gdx.Input
import com.larken.roguelike.map.Vector
import ktx.app.KtxInputAdapter

class InputHandler (val game: Game) : KtxInputAdapter  {
    val up = Vector(0, -1)
    val down = Vector(0, 1)
    val left = Vector(-1, 0)
    val right = Vector(1, 0)
    val ul = Vector(-1, -1)
    val ur = Vector(1, -1)
    val ll = Vector(-1, 1)
    val lr = Vector(1, 1)

    override fun keyUp(keycode: Int): Boolean {
        var playerPosition: Vector = game.player.pos

        var newPosition = Vector(playerPosition.x, playerPosition.y)
        when (keycode) {
            Input.Keys.W -> newPosition.add(up)
            Input.Keys.S -> newPosition.add(down)
            Input.Keys.A -> newPosition.add(left)
            Input.Keys.D -> newPosition.add(right)
        // VIM keys
            Input.Keys.K -> newPosition.add(up)
            Input.Keys.J -> newPosition.add(down)
            Input.Keys.H -> newPosition.add(left)
            Input.Keys.L -> newPosition.add(right)
            Input.Keys.Y -> newPosition.add(ul)
            Input.Keys.U -> newPosition.add(ur)
            Input.Keys.B -> newPosition.add(ll)
            Input.Keys.N -> newPosition.add(lr)
        }
        if (newPosition.x != playerPosition.x || newPosition.y != playerPosition.y)
            game.moveEntity(game.player, newPosition.x, newPosition.y)
        return true
    }
}