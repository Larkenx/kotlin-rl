package com.larken.roguelike

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import ktx.app.KtxInputAdapter

class Vector(var x: Int, var y: Int) {
    operator fun component1(): Int {
        return x
    }

    operator fun component2(): Int {
        return y
    }

    fun add(v: Vector) {
        val (dx, dy) = v
        x += dx
        y += dy
    }

    fun set(nx: Int, ny: Int) {
        x = nx
        y = ny
    }
}

class InputHandler : KtxInputAdapter {
    val selector = Vector(0, 0)
    val up = Vector(0, -1)
    val down = Vector(0, 1)
    val left = Vector(-1, 0)
    val right = Vector(1, 0)
    val ul = Vector(-1, -1)
    val ur = Vector(1, -1)
    val ll = Vector(-1, 1)
    val lr = Vector(1, 1)

    override fun keyUp(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.W -> selector.add(up)
            Input.Keys.S -> selector.add(down)
            Input.Keys.A -> selector.add(left)
            Input.Keys.D -> selector.add(right)
            // VIM keys
            Input.Keys.K -> selector.add(up)
            Input.Keys.J -> selector.add(down)
            Input.Keys.H -> selector.add(left)
            Input.Keys.L -> selector.add(right)
            Input.Keys.Y -> selector.add(ul)
            Input.Keys.U -> selector.add(ur)
            Input.Keys.B -> selector.add(ll)
            Input.Keys.N -> selector.add(lr)
        }
        return true
    }
}