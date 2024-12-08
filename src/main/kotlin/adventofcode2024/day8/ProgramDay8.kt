package adventofcode2024.day8

import common.Program

class ProgramDay8(brutInputs: List<String>, private val debug: Boolean = false) : Program {
    private val inputs = brutInputs.flatMapIndexed { y: Int, line: String ->
        line.mapIndexed { x, char -> Position(char, x, y) }
    }

    private val maxY = brutInputs.size - 1
    private val maxX = brutInputs.first().length - 1

    override fun part1(): String = inputs.asSequence()
        .map { it.name }
        .distinct()
        .filter { it != '.' }
        .flatMap { getAllAntinodeOf(it) }
        .distinct()
//        .apply { printAll(this) }
        .count().toString()

    override fun part2(): String = inputs.asSequence()
        .map { it.name }
        .distinct()
        .filter { it != '.' }
        .flatMap { getAllAntinodeOf(it, true) }
        .distinct()
//        .apply { printAll(this) }
        .count().toString()


    private fun getAllAntinodeOf(name: Char, infinity: Boolean = false): List<Antinode> {
        val antennas = inputs.filter { it.name == name }

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
                while (newX in 0..maxX && newY in 0..maxY) {
                    antinodes.add(Antinode(newX, newY))
                    newX += deltaX
                    newY += deltaY
                }
            } else {
                val newX = it.start.x + deltaX
                val newY = it.start.y + deltaY
                if (newX in 0..maxX && newY in 0..maxY
                    && inputs.none { it.x == newX && it.y == newY && it.name == name }
                ) {
                    antinodes.add(Antinode(newX, newY))
                }
            }

            antinodes
        }
    }

    private fun printAll(sequence: Sequence<Antinode>) {
        (0..maxY).forEach { y ->
            (0..maxX).forEach { x ->
                val name = inputs.first { it.x == x && it.y == y }.name
                print(sequence.find { it.x == x && it.y == y }?.let { '#' } ?: name)
            }
            println()
        }
    }

    data class Position(val name: Char, val x: Int, val y: Int)
    data class Antinode(val x: Int, val y: Int)
    data class LinearFunction(val start: Position, val end: Position)
}

