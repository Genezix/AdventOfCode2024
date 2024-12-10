package adventofcode2024.day10

import common.Program

class ProgramDay10(brutInputs: List<String>, private val debug: Boolean = false) : Program {
    private val grid = Grid.build(brutInputs)

    override fun part1(): String {
        val starts = grid.positions.filter { it.height == 0L }

        return starts.sumOf {
            grid.startToEnd[it] = mutableListOf()
            it.countNbTrailsToEnd(it)
        }.toString()
    }

    override fun part2(): String {
        val starts = grid.positions.filter { it.height == 0L }

        return starts.sumOf {
            grid.startToEnd[it] = mutableListOf()
            it.countNbTrailsToEnd(it, false)
        }.toString()
    }

    private fun Position.countNbTrailsToEnd(start: Position, useCache: Boolean = true): Long {
        if (this.height == 9L) {
            return if (useCache && grid.startToEnd[start]!!.contains(this))
                0
            else {
                grid.startToEnd[start]!!.add(this)
                return 1
            }
        }

        return this.neighbors.filter { it.height == this.height + 1 }.sumOf { it.countNbTrailsToEnd(start, useCache) }
    }

    data class Grid(
        val maxX: Long, val maxY: Long, val positions: List<Position>,
        val startToEnd: MutableMap<Position, MutableList<Position>> = mutableMapOf()
    ) {
        companion object {
            fun build(inputs: List<String>): Grid {
                val positions = inputs.flatMapIndexed { y, line ->
                    line.mapIndexed { x, height ->
                        Position(height.toString().toLong(), x.toLong(), y.toLong())
                    }
                }

                val maxX = (inputs.first().length - 1).toLong()
                val maxY = (inputs.size - 1).toLong()

                positions.forEach { position ->
                    positions.firstOrNull { it.x == position.x - 1 && it.y == position.y }
                        ?.let { position.neighbors.add(it) }
                    positions.firstOrNull { it.x == position.x + 1 && it.y == position.y }
                        ?.let { position.neighbors.add(it) }

                    positions.firstOrNull { it.x == position.x && it.y == position.y - 1 }
                        ?.let { position.neighbors.add(it) }
                    positions.firstOrNull { it.x == position.x && it.y == position.y + 1 }
                        ?.let { position.neighbors.add(it) }
                }

                return Grid(maxX, maxY, positions)
            }
        }
    }

    data class Position(
        val height: Long,
        val x: Long,
        val y: Long,
    ) {
        val neighbors: MutableList<Position> = mutableListOf()
    }
}
