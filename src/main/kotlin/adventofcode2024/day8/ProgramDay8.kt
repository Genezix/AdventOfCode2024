package adventofcode2024.day8

import common.Grid2D
import common.Position2D
import common.Program

class ProgramDay8(brutInputs: List<String>, private val debug: Boolean = false) : Program {
    private val grid = Grid2D.build(brutInputs)

    override fun part1(): String = grid.positions.asSequence()
        .map { it.value }
        .distinct()
        .filter { it != '.' }
        .flatMap { getAllAntinodeOf(it) }
        .distinct()
//        .apply { grid.print(this.map { it.toPosition2D() }) }
        .count().toString()

    override fun part2(): String = grid.positions.asSequence()
        .map { it.value }
        .distinct()
        .filter { it != '.' }
        .flatMap { getAllAntinodeOf(it, true) }
        .distinct()
//        .apply { grid.print(this.map { it.toPosition2D() }) }
        .count().toString()


    private fun getAllAntinodeOf(name: Char, infinity: Boolean = false): List<Antinode> {
        val antennas = grid.positions.filter { it.value == name }

        val linearFunctions = antennas.flatMap { first ->
            antennas.mapNotNull { second ->
                LinearFunction(first, second).takeIf { first != second }
            }
        }.distinct()

        return linearFunctions.flatMap {
            val deltaX = it.start.x - it.end.x
            val deltaY = it.start.y - it.end.y


            val antinodes = mutableListOf<Antinode>()

            if (infinity) {
                var newX = it.end.x + deltaX
                var newY = it.end.y + deltaY
                while (newX in 0..grid.maxX && newY in 0..grid.maxY) {
                    antinodes.add(Antinode(newX, newY))
                    newX += deltaX
                    newY += deltaY
                }
            } else {
                val newX = it.start.x + deltaX
                val newY = it.start.y + deltaY
                if (newX in 0..grid.maxX && newY in 0..grid.maxY
                    && grid.positions.none { it.x == newX && it.y == newY && it.value == name }
                ) {
                    antinodes.add(Antinode(newX, newY))
                }
            }

            antinodes
        }
    }

    data class Antinode(val x: Int, val y: Int) {
        fun toPosition2D() : Position2D = Position2D('#', this.x, this.y)
    }
    data class LinearFunction(val start: Position2D, val end: Position2D)
}

