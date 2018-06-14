package com.larken.roguelike.map

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