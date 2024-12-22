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

    override fun part1(): String = typeCode(2)

    override fun part2(): String = typeCode(25)

    private fun String.toNbMoves(): Map<String, Long> {
        return removeSuffix("A")
            .split("A")
            .map { it + 'A' }
            .groupingBy { it }
            .eachCount()
            .mapValues { it.value.toLong() }
    }

    private fun String.reverse(): String {
        val c1 = this.first()
        val c2 = this.dropLast(1).last()
        return this.map {
            when (it) {
                c1 -> c2
                c2 -> c1
                else -> it
            }
        }.joinToString("")
    }

    private fun Map<String, Long>.toNewNbMoves(reference: Map<String, Map<String, Long>>): Map<String, Long> {
        val result = mutableMapOf<String, Long>()

        this.forEach {
            val nbInput = it.value
            val mappingOutPut = reference[it.key] ?: reference[it.key.reverse()]!!
            mappingOutPut.forEach { (outPutKey, outPutValue) ->
                result[outPutKey] = result.getOrDefault(outPutKey, 0) + outPutValue * nbInput
            }
        }
        return result
    }

    fun typeCode(nbRobots: Int): String {
        val digitRobot = Robot(digicode.positions.first { it.value == 'A' })
        val controllerRobot = Robot(controller.positions.first { it.value == 'A' })
        val digitPaths = digicode.getAllPaths("<^v>")
        val controllerPaths = controller.getAllPaths("<^v>")

        return codes.sumOf { code ->
            var sequence = digicode.moveRobot(code, digitRobot, digitPaths).toNbMoves()

            repeat(nbRobots) {
                sequence = buildMap {
                    for ((segment, count) in sequence) {
                        for ((seg, times) in controller.moveRobot(segment, controllerRobot, controllerPaths).toNbMoves()) {
                            compute(seg) { _, old -> (old ?: 0) + count * times }
                        }
                    }
                }
            }

            sequence.map{
                it.key.length * it.value
            }.sum() * code.removeSuffix("A").toInt()
        }.toString()
    }


    private fun Grid2DWith4Neighbor.getPath(p1: Position2D, p2: Position2D, order: String): String {
        val deltaY = p2.y - p1.y
        val deltaX = p2.x - p1.x

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

        if (p1.x == empty.x && p2.y == empty.y) {
            return xMoves + yMoves + 'A'
        }

        if (p1.y == empty.y && p2.x == empty.x) {
            return yMoves + xMoves + 'A'
        }

        order.forEach { c ->
            if (yMoves.contains(c)) {
                return yMoves + xMoves + 'A'
            }
            if (xMoves.contains(c)) {
                return xMoves + yMoves + 'A'
            }
        }

        return "A"
    }

    private fun Grid2DWith4Neighbor.getAllPaths(order: String): Map<Pair<Char, Char>, String> {
        return positions.flatMap { pos1 ->
            positions.mapNotNull { pos2 ->
                if (pos1.value == ' ' || pos2.value == ' ') {
                    null
                } else {
                    Pair(pos1.value, pos2.value) to getPath(pos1, pos2, order)
                }
            }
        }.toMap()
    }


    data class Robot(var position: Position2D)

    private fun Grid2DWith4Neighbor.moveRobot(
        sequence: String,
        robot: Robot,
        paths: Map<Pair<Char, Char>, String>,
    ): String {
        return sequence.map { action ->
            val robotPosition = robot.position
            val next = this.positions.first { it.value == action }
            robot.position = next
            paths[Pair(robotPosition.value, next.value)]!!
        }.joinToString("")
    }
}
