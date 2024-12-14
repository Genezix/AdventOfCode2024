package adventofcode2024.day14

import common.Position2D
import common.Program
import common.multiply

class ProgramDay14(
    brutInputs: List<String>,
    private val debug: Boolean = false,
    val nbX: Int = 101,
    val nbY: Int = 103,
) : Program {
    private val robots = brutInputs.map { Robot.build(it) }

    override fun part1(): String = robots.moveRobots(100).getQuadrantResult().toString()

    override fun part2(): String {
        (1..Int.MAX_VALUE).forEach { second ->
            if (second % 1000000 == 0) println(second)
            robots.moveRobots(second).let {
                if (it.allAlone()) {
                    it.map { it.position }.print()
                    return second.toString()
                }
            }
        }
        error("prout")
    }

    private fun List<Robot>.allAlone(): Boolean {
        return this.all { robot ->
            this.count { it.position == robot.position } == 1
        }
    }

    private fun List<Robot>.moveRobots(nbSeconds: Int): List<Robot> {
        return this.map { robot ->
            val newX = (robot.position.x + nbX * nbSeconds + robot.vx * nbSeconds) % (nbX)
            val newY = (robot.position.y + nbY * nbSeconds + robot.vy * nbSeconds) % (nbY)
            robot.copy(position = Position2D(newX, newY))
        }
    }

    private fun List<Position2D>.print() {
        if (true) {
            (0 until nbY).forEach { y ->
                (0 until nbX).forEach { x ->
                    print(
                        this.count { it.x == x && it.y == y }.let { if (it > 0) it else "." }
                    )
                }
                println()
            }
            println()
        }
    }

    private fun List<Position2D>.printWithoutMiddle() {

        val middleX = nbX / 2
        val middleY = nbY / 2
        if (debug) {
            (0 until nbY).forEach { y ->
                (0 until nbX).forEach { x ->
                    print(
                        if (x == middleX || y == middleY) " "
                        else
                            this.count { it.x == x && it.y == y }.let { if (it > 0) it else "." }
                    )
                }
                println()
            }
            println()
        }
    }

    private fun List<Robot>.getQuadrantResult(): Long {
        val middleX = nbX / 2
        val middleY = nbY / 2


        return this
            .filter { it.position.x != middleX && it.position.y != middleY }
            .also { it.map { it.position }.printWithoutMiddle() }
            .groupBy {
                val x = it.position.x
                val y = it.position.y
                if (x < middleX && y < middleY) {
                    1
                } else if (x > middleX && y < middleY) {
                    2
                } else if (x < middleX && y > middleY) {
                    3
                } else {
                    4
                }
            }.map {
                it.value.count()
            }.multiply().toLong()
    }

    data class Robot(
        var position: Position2D,
        val vx: Int,
        val vy: Int,
    ) {
        companion object {
            val regex = "p=(\\d*),(\\d*) v=([0-9-]*),([0-9-]*)".toRegex()
            fun build(input: String): Robot {
                val parts = regex.find(input)!!.groupValues
                val px = parts[1].toInt()
                val py = parts[2].toInt()
                val vx = parts[3].toInt()
                val vy = parts[4].toInt()
                return Robot(Position2D(px, py), vx, vy)
            }
        }
    }
}
