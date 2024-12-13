package adventofcode2024.day12

import common.Grid2DWith4Neighbor
import common.Heading4
import common.Position2D
import common.Program

//870164 toHigh
//864221 toHigh
//863633 toHigh
class ProgramDay12(brutInputs: List<String>, private val debug: Boolean = false) : Program {
    private val grid = Grid2DWith4Neighbor.build(brutInputs, true)

    override fun part1(): String {
        val alreadyCountedPositions = mutableSetOf<Position2D>()

        return grid.positions.sumOf { position ->
            if (alreadyCountedPositions.contains(position)) {
                0
            } else {
                val positions = position.getAllPositions(listOf(position))
                alreadyCountedPositions.addAll(positions)
                positions.size * positions.sumOf { 4 - it.neighbors.size }
            }
        }.toString()
    }

    override fun part2(): String {
        val alreadyCountedPositions = mutableSetOf<Position2D>()

        return grid.positions.sumOf { position ->
            if (alreadyCountedPositions.contains(position)) {
                0
            } else {
                val positions = position.getAllPositions(listOf(position))
                alreadyCountedPositions.addAll(positions)
                positions.size * positions.calculatePerimeter()
            }
        }.toString()
    }

    data class Border(
        val position: Position2D,
        val heading: Heading4,
    ) {
        val neighbors: MutableList<Border> = mutableListOf()
    }

    private fun List<Position2D>.calculatePerimeter(): Int {
        val borders = this.mapNotNull { position ->
            if (position.neighbors.size < 4) {
                listOfNotNull(
                    Heading4.N.takeIf { position.neighbors.none { neig -> position.y - 1 == neig.y } },
                    Heading4.S.takeIf { position.neighbors.none { neig -> position.y + 1 == neig.y } },
                    Heading4.E.takeIf { position.neighbors.none { neig -> position.x + 1 == neig.x } },
                    Heading4.W.takeIf { position.neighbors.none { neig -> position.x - 1 == neig.x } },
                ).map { Border(position, it) }
            } else {
                null
            }
        }.flatten()

        borders.forEach { border ->
            borders.filter {
                it.position == border.position &&
                        (it.heading == border.heading.rotateRight() || it.heading == border.heading.rotateLeft())
            }.forEach { border.neighbors.add(it) }

            borders.filter {
                border.position.neighbors.contains(it.position) && (it.heading == border.heading)
            }.forEach { border.neighbors.add(it) }

            borders.filter {
                border.isCrossNeighbor(it)
                        && it.position.neighbors.intersect(border.position.neighbors.toSet()).isNotEmpty()
            }.forEach { border.neighbors.add(it) }
        }

        return borders.first().let { it.moveToNext(it, borders.minus(it), 0) }
    }

    private fun Border.isCrossNeighbor(other: Border): Boolean {
        return when (this.heading) {
            Heading4.N -> (other.position.y == this.position.y - 1 && other.position.x == this.position.x - 1 && other.heading == Heading4.E) ||
                    (other.position.y == this.position.y - 1 && other.position.x == this.position.x + 1 && other.heading == Heading4.W)

            Heading4.E -> (other.position.y == this.position.y - 1 && other.position.x == this.position.x + 1 && other.heading == Heading4.S) ||
                    (other.position.y == this.position.y + 1 && other.position.x == this.position.x + 1 && other.heading == Heading4.N)

            Heading4.S -> (other.position.y == this.position.y + 1 && other.position.x == this.position.x - 1 && other.heading == Heading4.E) ||
                    (other.position.y == this.position.y + 1 && other.position.x == this.position.x + 1 && other.heading == Heading4.W)

            Heading4.W -> (other.position.y == this.position.y - 1 && other.position.x == this.position.x - 1 && other.heading == Heading4.S) ||
                    (other.position.y == this.position.y + 1 && other.position.x == this.position.x - 1 && other.heading == Heading4.N)
        }
    }

    private fun Border.moveToNext(first: Border, remaining: List<Border>, count: Int): Int {
        if (remaining.isEmpty()) {
            return (if (this.heading == first.heading) {
                count
            } else count + 1).also { println("${first.position.value} result=$it") }
        }

        val next = this.neighbors.firstOrNull { remaining.contains(it) }

        if (next == null) {
            val restart = remaining.first()
            return restart.moveToNext(
                restart, remaining.minus(restart), if (this.heading == first.heading) {
                    count
                } else count + 1
            )
        }

        println("${first.position.value} : ${this.position.x}-${this.position.y} -> ${next.position.x}-${next.position.y} : ${this.heading} -> ${next.heading} : $count")

        val newCount = if (this.heading == next.heading) {
            count
        } else count + 1

        return next.moveToNext(first, remaining.minus(next), newCount)
    }
}

private fun Position2D.getAllPositions(currentList: List<Position2D>): List<Position2D> {
    val result = currentList.toMutableList()

    val neighborsToSearch = this.neighbors.filter { !result.contains(it) }

    neighborsToSearch.forEach { neighbor ->
        result.addAll(
            neighbor.getAllPositions(
                result.plus(
                    neighbor
                )
            )
        )
    }

    return result.distinct()
}
