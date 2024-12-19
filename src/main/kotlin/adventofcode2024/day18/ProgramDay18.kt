package adventofcode2024.day18

import common.Grid2DWith4Neighbor
import common.Position2D
import common.Program

class ProgramDay18(
    brutInputs: List<String>,
    private val debug: Boolean = false,
    val maxX: Int = 70,
    val maxY: Int = 70,
    val nbBytes: Int = 1024
) : Program {
    private val memories = brutInputs.map { line -> Position2D(line.split(",")[0].toInt(), line.split(",")[1].toInt(), '#') }

    override fun part1(): String = calculate(memories.take(nbBytes)).toString()

    override fun part2(): String {
        (nbBytes + 1..memories.size)
            .forEach { index ->
                calculate(memories.take(index)) ?: return memories.take(index).last().let { "${it.x},${it.y}" }
            }

        return "prout"
    }

    private fun calculate( memories: List<Position2D>) : Long? {
        val grid = Grid2DWith4Neighbor.build(
            memories, maxX, maxY
        )
        val start = grid.positions.first { it.x == 0 && it.y == 0 }
        val end = grid.positions.first { it.x == maxX && it.y == maxY }
        start.score = 0
        start.find(end, 0)
        return end.score
    }

    private fun Position2D.find(
        target: Position2D,
        score: Long,
    ) {
        this.neighbors.filter { it.value != '#' }.forEach { neighbor ->
            neighbor.parsePosition(target, score + 1L)
        }
    }

    private fun Position2D.parsePosition(
        target: Position2D,
        score: Long,
    ) {
        if (target.score != null && target.score!! < score) return

        val nextPosition = this
            .takeIf { (it.value == '.' || it == target) }
            ?.takeIf { it.score == null || (it.score!! > score) }

        if (target == nextPosition) {
            if (target.score == null || target.score!! > score) {
                target.score = score
            }
        } else {
            nextPosition?.also { if (it.score == null || it.score!! > score) it.score = score }
                ?.find(target, score)
        }
    }
}
