package adventofcode2024.day6

import adventofcode2024.day6.ProgramDay6.Position.Gard
import adventofcode2024.day6.ProgramDay6.Position.Obstacle
import adventofcode2024.day6.ProgramDay6.Position.Visited
import adventofcode2024.day6.ProgramDay6.Position.VisitedRange
import common.Grid2D
import common.Heading4
import common.Program
import kotlin.math.abs

class ProgramDay6(brutInputs: List<String>, private val debug: Boolean = false) : Program {
    private val grid = Grid.from(brutInputs)

    override fun part1(): String =
        moveToNextPosition(grid.obstacle, grid.gard, grid.maxX, grid.maxY, emptyList())!!.toVisitedList()
            .distinctByPosition().count()
            .toString()

    override fun part2(): String = grid.addObstaclesToLoop().toString()

    private fun List<Visited>.distinctByPosition() = this.distinctBy { visited ->
        Pair(
            visited.x,
            visited.y
        )
    }

    private fun List<VisitedRange>.toVisitedList() = this.flatMap { visited ->
        visited.x.flatMap { x ->
            visited.y.map { y -> Visited(x, y, visited.heading) }
        }
    }

    private fun Grid.addObstaclesToLoop(): Int {
        val visitedPositions = moveToNextPosition(obstacle, gard, maxX, maxY, emptyList())!!.distinct()

        val nbLoop =
            visitedPositions.toVisitedList().distinctByPosition().count { visited ->
                moveToNextPosition(obstacle.plus(Obstacle(visited.x, visited.y)), gard, maxX, maxY, emptyList()) == null
            }

        return nbLoop
    }

    private fun moveToNextPosition(
        obstacle: List<Obstacle>,
        gard: Gard,
        maxX: Int,
        maxY: Int,
        path: List<VisitedRange>
    ): List<VisitedRange>? {
        val x = gard.x
        val y = gard.y
        val heading = gard.heading

        val nearestObstacle = when (heading) {
            Heading4.N -> obstacle.filter { it.x == x && it.y < y }.minByOrNull { abs(it.y - y) }
            Heading4.E -> obstacle.filter { it.x > x && it.y == y }.minByOrNull { abs(it.x - x) }
            Heading4.S -> obstacle.filter { it.x == x && it.y > y }.minByOrNull { abs(it.y - y) }
            Heading4.W -> obstacle.filter { it.x < x && it.y == y }.minByOrNull { abs(it.x - x) }
        }
            ?: return path.plus(
                VisitedRange(
                    when (heading) {
                        Heading4.E -> IntRange(x, maxX)
                        Heading4.W -> IntRange(0, x)
                        else -> IntRange(x, x)
                    }, when (heading) {
                        Heading4.N -> IntRange(0, y)
                        Heading4.S -> IntRange(y, maxX)
                        else -> IntRange(y, y)
                    }, heading
                )
            )

        val nextX = when (heading) {
            Heading4.E -> nearestObstacle.x - 1
            Heading4.W -> nearestObstacle.x + 1
            else -> gard.x
        }

        val nextY = when (heading) {
            Heading4.N -> nearestObstacle.y + 1
            Heading4.S -> nearestObstacle.y - 1
            else -> gard.y
        }

        val newVisited = VisitedRange(
            when (heading) {
                Heading4.E -> IntRange(x, nextX)
                Heading4.W -> IntRange(nextX, x)
                else -> IntRange(x, x)
            }, when (heading) {
                Heading4.N -> IntRange(nextY, y)
                Heading4.S -> IntRange(y, nextY)
                else -> IntRange(y, y)
            }, heading
        )

        // ça veut dir qu'on boucle
        if (path.any {
                it.heading == newVisited.heading
                        && it.x.intersect(newVisited.x).isNotEmpty()
                        && it.y.intersect(newVisited.y).isNotEmpty()
            }) return null

        return moveToNextPosition(
            obstacle,
            Gard(nextX, nextY, gard.heading.rotateRight()),
            maxX,
            maxY,
            path.plus(newVisited)
        )
    }

    data class Grid(val obstacle: List<Obstacle>, val maxX: Int, val maxY: Int, val gard: Gard) {
        companion object {
            fun from(inputs: List<String>): Grid {
                val grid = Grid2D.build(inputs)

                return Grid(
                    obstacle = grid.positions.filter { it.value == '#' }.map { Obstacle(it.x, it.y) },
                    maxX = grid.maxX,
                    maxY = grid.maxY,
                    gard = grid.positions.first { it.value == '^' }.let { Gard(it.x, it.y, Heading4.N) },
                )
            }
        }
    }

    sealed class Position {
        data class Obstacle(val x: Int, val y: Int)
        data class VisitedRange(val x: IntRange, val y: IntRange, val heading: Heading4)
        data class Visited(val x: Int, val y: Int, val heading: Heading4)
        data class Gard(val x: Int, val y: Int, val heading: Heading4)
    }
}
