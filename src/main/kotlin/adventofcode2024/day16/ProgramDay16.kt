package adventofcode2024.day16

import common.Grid2DWith4Neighbor
import common.Heading4
import common.Position2D
import common.Program

class ProgramDay16(brutInputs: List<String>, private val debug: Boolean = false) : Program {
    private val grid = Grid2DWith4Neighbor.build(brutInputs)
    private val cache = mutableMapOf<Position2D, Long>()
    private val bestPath = mutableListOf<Position2D>()

    override fun part1(): String {
        val start = grid.positions.first { it.value == 'S' }
        val end = grid.positions.first { it.value == 'E' }
        start.find(end, Heading4.E, 0)
        return cache[end].toString()
    }

    override fun part2(): String {
        val start = grid.positions.first { it.value == 'S' }
        val end = grid.positions.first { it.value == 'E' }
        start.find(end, Heading4.E, 0, listOf(start))
        return bestPath.distinct().count().toString()
    }

    private fun Position2D.parsePosition(
        target: Position2D,
        heading: Heading4,
        score: Long,
        currentPositions: List<Position2D>
    ) {
        if (cache[target] != null && cache[target]!! < score) return

        val nextPosition = this.takeIf { (it.value == '.' || it == target) }
            .takeIf { cache[it] == null || (cache[it]!! + 1000 >= score) }

        if (target == nextPosition) {
            if (cache[target] == null || cache[target]!! >= score) {
                if (cache[target] != null && cache[target]!! > score) bestPath.clear()

                cache[target] = score
                bestPath.addAll(currentPositions.plus(nextPosition))
            }
        } else {
            nextPosition?.also { if (cache[it] == null || cache[it]!! > score) cache[it] = score }
                ?.find(target, heading, score, currentPositions.plus(nextPosition))
        }
    }

    private fun Position2D.find(
        target: Position2D,
        heading: Heading4,
        score: Long,
        currentPositions: List<Position2D> = emptyList()
    ) {
        this.getNeighbor(heading)?.takeIf { it.value != '#' }?.parsePosition(target, heading, score + 1L, currentPositions)

        this.getNeighbor(heading.rotateRight())?.takeIf { it.value != '#' }
            ?.parsePosition(target, heading.rotateRight(), score + 1001L, currentPositions)

        this.getNeighbor(heading.rotateLeft())?.takeIf { it.value != '#' }
            ?.parsePosition(target, heading.rotateLeft(), score + 1001L, currentPositions)

    }
}
