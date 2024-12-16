package adventofcode2024.day15

import common.Grid2DWith4Neighbor
import common.Heading4
import common.Position2D
import common.Program
import common.parseGroupsList

class ProgramDay15(brutInputs: List<String>, private val debug: Boolean = false) : Program {
    private val inputs = brutInputs.parseGroupsList()

    override fun part1(): String {
        val grid = Grid2DWith4Neighbor.build(inputs.first())
        val directions = inputs[1].joinToString("").parsDirection()
        return directions.move(grid).positions.filter { it.value == 'O' }.sumOf { it.x + it.y * 100L }.toString()
    }

    override fun part2(): String {
        val grid = Grid2DWith4Neighbor.build(inputs.first().map {
            it
                .replace("#", "##")
                .replace(".", "..")
                .replace("O", "[]")
                .replace("@", "@.")
        }

        )
        val directions = inputs[1].joinToString("").parsDirection()
        return directions.move(grid).positions.filter { it.value == '[' }.sumOf { it.x + it.y * 100L }.toString()
    }

    private fun List<Heading4>.move(grid: Grid2DWith4Neighbor): Grid2DWith4Neighbor {
        if (debug) grid.print()
        this.forEach { heading ->
            if (debug) println("Next move: $heading")
            val me = grid.positions.first { it.value == '@' }

            val nextPosition = when (heading) {
                Heading4.N -> me.neighbors.firstOrNull { it.x == me.x && it.y == me.y - 1 }
                Heading4.S -> me.neighbors.firstOrNull { it.x == me.x && it.y == me.y + 1 }
                Heading4.W -> me.neighbors.firstOrNull { it.x == me.x - 1 && it.y == me.y }
                Heading4.E -> me.neighbors.firstOrNull { it.x == me.x + 1 && it.y == me.y }
            }

            when (nextPosition?.value) {
                '.' -> {
                    me.value = '.'
                    nextPosition.value = '@'
                }

                '[', ']' -> {
                    val boxes = grid.getAllBoxes()
                    val nextBoxPosition = boxes.first { it.left == nextPosition || it.right == nextPosition }

                    if (nextBoxPosition.canMove(heading, boxes)) {
                        nextBoxPosition.tryToMoveBigBox(heading, boxes)
                        if (nextPosition.value == '.') {
                            me.value = '.'
                            nextPosition.value = '@'
                        }
                    }
                }

                'O' -> {
                    nextPosition.tryToMove(heading)
                    if (nextPosition.value == '.') {
                        me.value = '.'
                        nextPosition.value = '@'
                    }
                }
            }

            if (debug) grid.print()
        }



        return grid
    }

    private fun Grid2DWith4Neighbor.getAllBoxes(): List<Boxe> {
        return this.positions.filter { it.value == '[' }.map { left ->
            val right = left.neighbors.first { it.x == left.x + 1 && it.y == left.y }
            Boxe(left, right)
        }
    }


    data class Boxe(var left: Position2D, var right: Position2D) {
        fun positions() = listOf(left, right)
    }

    private fun Boxe.canMove(heading: Heading4, boxes: List<Boxe>): Boolean {
        val targetPositions = getTargetPositions(heading)
        val canDirectlyMove = canDirectlyMove(heading, targetPositions)
        val isStuck = isStuck(heading, targetPositions)

        return when {
            canDirectlyMove -> true
            isStuck -> false
            else -> getNextBoxes(heading, boxes, targetPositions).all { it.canMove(heading, boxes) }
        }
    }

    private fun Boxe.getTargetPositions(heading: Heading4): List<Position2D> = when (heading) {
        Heading4.N -> this.positions()
            .map { position -> position.neighbors.first { it.x == position.x && it.y == position.y - 1 } }

        Heading4.S -> this.positions()
            .map { position -> position.neighbors.first { it.x == position.x && it.y == position.y + 1 } }

        Heading4.W -> listOf(
            this.left.neighbors.first { it.x == this.left.x - 1 && it.y == this.left.y },
            this.left
        )

        Heading4.E -> listOf(
            this.right,
            this.right.neighbors.first { it.x == this.right.x + 1 && it.y == this.right.y })
    }

    private fun Boxe.tryToMoveBigBox(heading: Heading4, boxes: List<Boxe>) {
        val targetPositions = getTargetPositions(heading)
        val canDirectlyMove = canDirectlyMove(heading, targetPositions)
        val isStuck = isStuck(heading, targetPositions)

        when {
            canDirectlyMove -> {
                this.positions().forEach { it.value = '.' }
                targetPositions.first().value = '['
                targetPositions.last().value = ']'
                this.left = targetPositions.first()
                this.right = targetPositions.last()
            }

            isStuck -> return
            else -> {
                getNextBoxes(heading, boxes, targetPositions).forEach { it.tryToMoveBigBox(heading, boxes) }

                val canMoveAfterBoxeMove = canDirectlyMove(heading, targetPositions)

                if (canMoveAfterBoxeMove) {
                    this.positions().forEach { it.value = '.' }
                    targetPositions.first().value = '['
                    targetPositions.last().value = ']'
                    this.left = targetPositions.first()
                    this.right = targetPositions.last()
                }
            }
        }
    }

    private fun getNextBoxes(
        heading: Heading4,
        boxes: List<Boxe>,
        targetPositions: List<Position2D>
    ) = when (heading) {
        Heading4.N, Heading4.S -> boxes.filter {
            targetPositions.contains(it.left) || targetPositions.contains(
                it.right
            )
        }

        Heading4.E -> boxes.filter { it.positions().contains(targetPositions.last()) }
        Heading4.W -> boxes.filter { it.positions().contains(targetPositions.first()) }
    }

    private fun isStuck(heading: Heading4, targetPositions: List<Position2D>) = when (heading) {
        Heading4.N, Heading4.S -> targetPositions.any { it.value == '#' }
        Heading4.W -> targetPositions.first().value == '#'
        Heading4.E -> targetPositions.last().value == '#'
    }

    private fun canDirectlyMove(heading: Heading4, targetPositions: List<Position2D>) = when (heading) {
        Heading4.N, Heading4.S -> targetPositions.all { it.value == '.' }
        Heading4.W -> targetPositions.first().value == '.'
        Heading4.E -> targetPositions.last().value == '.'
    }

    private fun Position2D.tryToMove(heading: Heading4) {
        val nextBoxPosition = when (heading) {
            Heading4.N -> this.neighbors.firstOrNull { it.x == this.x && it.y == this.y - 1 }
            Heading4.S -> this.neighbors.firstOrNull { it.x == this.x && it.y == this.y + 1 }
            Heading4.W -> this.neighbors.firstOrNull { it.x == this.x - 1 && it.y == this.y }
            Heading4.E -> this.neighbors.firstOrNull { it.x == this.x + 1 && it.y == this.y }
        }

        when (nextBoxPosition?.value) {
            '.' -> {
                this.value = '.'
                nextBoxPosition.value = 'O'
            }

            'O' -> {
                nextBoxPosition.tryToMove(heading)
                if (nextBoxPosition.value == '.') {
                    this.value = '.'
                    nextBoxPosition.value = 'O'
                }
            }
        }
    }


    private fun String.parsDirection(): List<Heading4> {
        return this.map {
            when (it) {
                '^' -> Heading4.N
                'v' -> Heading4.S
                '<' -> Heading4.W
                '>' -> Heading4.E
                else -> throw IllegalArgumentException("Invalid direction")
            }
        }
    }
}
