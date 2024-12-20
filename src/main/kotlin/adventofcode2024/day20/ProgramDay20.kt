package adventofcode2024.day20

import common.Grid2DWith4Neighbor
import common.Heading4
import common.Position2D
import common.Program
import kotlin.math.abs

// 45773 to low
// 49165 to low
// 60650 ????
class ProgramDay20(val brutInputs: List<String>, private val debug: Boolean = false, val maxPicoSeconds: Long = 100) :
    Program {
    private val grid = Grid2DWith4Neighbor.build(brutInputs)

    override fun part1(): String {
        val start = grid.positions.first { it.value == 'S' }
        val end = grid.positions.first { it.value == 'E' }

        start.score = 0L

        var currentPosition: List<Position2D> = listOf(start)

        while (currentPosition.isNotEmpty()) {
            currentPosition = currentPosition.flatMap { it.calculateScoreAndGetNextPosition(end) }.distinct()
        }

        val shortcuts = grid.positions.filter { it.value == '.' || it == start }.flatMap { position ->
            Heading4.values().mapNotNull { heading ->
                position.getNeighbor(heading)?.takeIf { it.value == '#' }?.getNeighbor(heading)
                    ?.takeIf { (it.value == '.' || it == end) && (it.score!! > position.score!!) }
            }.map { it.score!! - position.score!! - 2 }
        }

        return shortcuts.count { it >= maxPicoSeconds }.toString()
    }

    override fun part2(): String {
        val start = grid.positions.first { it.value == 'S' }
        val end = grid.positions.first { it.value == 'E' }

        start.score = 0L

        var currentPosition: List<Position2D> = listOf(start)

        while (currentPosition.isNotEmpty()) {
            currentPosition = currentPosition.flatMap { it.calculateScoreAndGetNextPosition(end) }.distinct()
        }

        val shortcuts = grid.positions.filter { it.value == '.' || it == start }.flatMap { position ->
            position.getAllInRange(listOf('.', 'E'), 20)
        }

        return shortcuts.count().toString()
    }

    private fun Position2D.getAllInRange(filter: List<Char>, range: Int): List<Shortcut> {
        return grid.positions.filter {
            abs(this.x - it.x) + abs(this.y - it.y) <= range
                    && filter.contains(it.value)
                    && this.score!! < it.score!!
        }.mapNotNull {
            val xy = abs(this.x - it.x) + abs(this.y - it.y)
            val picosecondsWon = (it.score!! - this.score!!) - xy
            Shortcut(this, it, picosecondsWon).takeIf { picosecondsWon >= maxPicoSeconds }
        }
    }

    private fun Position2D.findShortcuts1(original: Position2D): List<Shortcut> {
        if (this.score!! - original.score!! > 20) return emptyList()

        val neighbors = Heading4.values().mapNotNull { heading ->
            this.getNeighbor(heading)?.takeIf { !it.passed }
        }

        return neighbors.flatMap { neighbor ->
            if (neighbor.value == '.' || neighbor.value == 'E') {
                val previousDelta = neighbor.score!! - original.score!!
                val newDelta = this.score!! - original.score!!
                val picosecondsWon = (previousDelta - newDelta) - 1

                val afterDot = if (neighbor.value == '.' && newDelta < 19) {
                    val previousScore = neighbor.score
                    neighbor.score = this.score!! + 1
                    neighbor.passed = true
                    neighbor.findShortcuts1(original).also {
                        neighbor.score = previousScore
                        neighbor.passed = false
                    }
                } else emptyList()

                listOfNotNull(
                    Shortcut(original, neighbor, picosecondsWon).takeIf { picosecondsWon >= maxPicoSeconds },
                ) + afterDot
            } else {
                neighbor.score = this.score!! + 1
                neighbor.passed = true
                neighbor.findShortcuts1(original).also {
                    neighbor.score = null
                    neighbor.passed = false
                }
            }
        }
    }

    data class Shortcut(val start: Position2D, val end: Position2D, val picoseconds: Long) 
}
