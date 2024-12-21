package adventofcode2024.day21

import common.Grid2DWith4Neighbor
import common.Position2D
import common.Program

class ProgramDay21(brutInputs: List<String>, private val debug: Boolean = false) : Program {
    private val codes = brutInputs

    private val digicode = Grid2DWith4Neighbor.build(
        listOf(
            "789",
            "456",
            "123",
            " 0A",
        )
    )

    private val controller = Grid2DWith4Neighbor.build(
        listOf(
            " ^A",
            "<v>",
        )
    )

    data class Robot(var position: Position2D)

    private val cache = mutableMapOf<String, List<String>>()

    private fun Grid2DWith4Neighbor.moveRobot(
        sequence: String,
        robot: Robot,
        hasToCalculateVariants: Boolean = true,
    ): List<String> {
        if (cache.containsKey(sequence)) {
            return cache[sequence]!!
        }

        val moves = sequence.map { action ->
            val robotPosition = robot.position
            val next = this.positions.first { it.value == action }
            val deltaY = next.y - robotPosition.y
            val deltaX = next.x - robotPosition.x

            robot.position = next

            val yMoves = when {
                deltaY > 0 -> "v".repeat(deltaY)
                deltaY < 0 -> "^".repeat(-deltaY)
                else -> ""
            }

            val xMoves = when {
                deltaX > 0 -> ">".repeat(deltaX)
                deltaX < 0 -> "<".repeat(-deltaX)
                else -> ""
            }

            val empty = this.positions.first { it.value == ' ' }

            if (!hasToCalculateVariants) {
                return@map listOf(xMoves + yMoves + 'A')
            }

            if(robotPosition.x == empty.x && next.y == empty.y) {
                return@map listOf(xMoves + yMoves + 'A')
            }

            if(robotPosition.y == empty.y && next.x == empty.x) {
                return@map  listOf(yMoves + xMoves + 'A')
            }

            if(xMoves.contains('<')) {
                return@map listOf(xMoves + yMoves + 'A')
            }

            if (xMoves.contains('^')) {
                return@map listOf(yMoves + xMoves + 'A')
            }

            listOf(xMoves + yMoves + 'A', yMoves + xMoves + 'A')
        }
        val possibilities = moves.searchPossibility().distinct()
        val minLength = possibilities.minOf { it.length }
        val result = possibilities.distinct().filter { it.length == minLength }
        cache[sequence] = result
        return result
    }

    private fun List<List<String>>.searchPossibility(): List<String> {
        return this.first().let { string ->
            string.flatMap { value ->
                if (this.size == 1) {
                    listOf(value)
                } else {
                    this.drop(1).searchPossibility().map { next ->
                        value + next
                    }
                }
            }
        }
    }

    override fun part1(): String = typeCode(2)

    override fun part2(): String = typeCode(25)

    fun typeCode(nbRobots: Int): String {
        val digitRobot = Robot(digicode.positions.first { it.value == 'A' })
        val controllerRobot = Robot(controller.positions.first { it.value == 'A' })
        val digitCombo = digicode.positions.flatMap { pos1 ->
            digicode.positions.mapNotNull { pos2 ->
                if (pos1 == pos2) {
                    null
                } else {
                    pos1.value.toString() + pos2.value
                }
            }

        }

        return codes.sumOf { code ->
            var sequence = digicode.moveRobot(code, digitRobot)
            println(sequence)

            (0 until nbRobots).map { index ->
                sequence = sequence.flatMap { controller.moveRobot(it, controllerRobot, index != nbRobots - 1) }
//                println(sequence.minOf { it.length })
            }

            val result = sequence.minBy { it.length }
            println("${result.length} ${code.removeSuffix("A").toInt()} ")
            result.length * code.removeSuffix("A").toInt()
        }.toString()
    }
}
