package adventofcode2024.day6

import adventofcode2024.day6.ProgramDay6.Position.*
import common.Program
import kotlin.math.abs

class ProgramDay6(brutInputs: List<String>, private val debug: Boolean = false) : Program {
    private val grid = Grid.from(brutInputs)

    override fun part1(): String =
        moveToNextPosition(grid.obstacle, grid.gard, grid.maxX, grid.maxY, emptyList())!!.distinctByPosition().count()
            .toString()

    override fun part2(): String = grid.addObstaclesToLoop().toString()

    private fun List<Visited>.distinctByPosition() = this.distinctBy { visited ->
        Pair(
            visited.x,
            visited.y
        )
    }

    fun Grid.addObstaclesToLoop(): Int {
        val visitedPositions = moveToNextPosition(obstacle, gard, maxX, maxY, emptyList())!!.distinct()

        val nbLoop =
            visitedPositions.distinctByPosition().count { visited ->
                moveToNextPosition(obstacle.plus(Obstacle(visited.x, visited.y)), gard, maxX, maxY, emptyList()) == null
            }

        return nbLoop
    }

    fun moveToNextPosition(
        obstacle: List<Obstacle>,
        gard: Gard,
        maxX: Int,
        maxY: Int,
        path: List<Visited>
    ): List<Visited>? {
        val x = gard.x
        val y = gard.y
        val heading = gard.heading

        val nearestObstacle = when (heading) {
            Heading.N -> obstacle.filter { it.x == x && it.y < y }.minByOrNull { abs(it.y - y) }
            Heading.E -> obstacle.filter { it.x > x && it.y == y }.minByOrNull { abs(it.x - x) }
            Heading.S -> obstacle.filter { it.x == x && it.y > y }.minByOrNull { abs(it.y - y) }
            Heading.W -> obstacle.filter { it.x < x && it.y == y }.minByOrNull { abs(it.x - x) }
        }

        if (nearestObstacle == null) {
            val lastPath = when (heading) {
                Heading.N -> (0..y).map { Visited(x, it, heading) }
                Heading.E -> (x..maxX).map { Visited(it, y, heading) }
                Heading.S -> (y..maxY).map { Visited(x, it, heading) }
                Heading.W -> (0..x).map { Visited(it, y, heading) }
            }

            return path.plus(lastPath)
        }

        val nextX = when (heading) {
            Heading.E -> nearestObstacle.x - 1
            Heading.W -> nearestObstacle.x + 1
            else -> gard.x
        }

        val nextY = when (heading) {
            Heading.N -> nearestObstacle.y + 1
            Heading.S -> nearestObstacle.y - 1
            else -> gard.y
        }

        val lastPath = when (heading) {
            Heading.N -> (nextY..y).map { Visited(x, it, heading) }
            Heading.E -> (x..nextX).map { Visited(it, y, heading) }
            Heading.S -> (y..nextY).map { Visited(x, it, heading) }
            Heading.W -> (nextX..x).map { Visited(it, y, heading) }
        }

        // ça veut dir qu'on boucle
        if (path.any { lastPath.contains(it) }) return null

        return moveToNextPosition(
            obstacle,
            Gard(nextX, nextY, gard.heading.rotateRight()),
            maxX,
            maxY,
            path.plus(lastPath)
        )
    }

    data class Grid(val obstacle: List<Obstacle>, val maxX: Int, val maxY: Int, val gard: Gard) {
        companion object {
            fun from(inputs: List<String>): Grid {
                val positions = inputs.flatMapIndexed { y, line ->
                    line.mapIndexed { x, c ->
                        when {
                            c == '#' -> Obstacle(x, y)
                            c != '.' -> Gard(x, y, Heading.from(c))
                            else -> null
                        }
                    }
                }

                return Grid(
                    obstacle = positions.mapNotNull { it as? Obstacle },
                    maxX = inputs.first().length - 1,
                    maxY = inputs.size - 1,
                    gard = positions.firstNotNullOf { it as? Gard },
                )
            }
        }
    }

    sealed class Position {
        data class Obstacle(val x: Int, val y: Int)
        data class Visited(val x: Int, val y: Int, val heading: Heading)
        data class Gard(val x: Int, val y: Int, val heading: Heading)
    }

    enum class Heading {
        N, E, S, W;

        fun rotateRight() = when (this) {
            N -> E
            E -> S
            S -> W
            W -> N
        }

        companion object {
            fun from(c: Char): Heading = when (c) {
                '<' -> W
                '^' -> N
                '>' -> E
                'v' -> S
                else -> error("prout")
            }
        }
    }
}
