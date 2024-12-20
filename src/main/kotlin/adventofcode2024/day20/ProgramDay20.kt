package adventofcode2024.day20

import common.Grid2DWith4Neighbor
import common.Heading4
import common.Position2D
import common.Program

class ProgramDay20(val brutInputs: List<String>, private val debug: Boolean = false, val maxPicoSeconds: Long = 100) :
    Program {
    private val grid = Grid2DWith4Neighbor.build(brutInputs)

    override fun part1(): String {
        val start = grid.positions.first { it.value == 'S' }
        val end = grid.positions.first { it.value == 'E' }

        start.score = 0L

        var currentPosition: List<Position2D> = listOf(start)

        while (currentPosition.isNotEmpty()) {
            currentPosition = currentPosition.flatMap { it.calculateScoreAndGetNextPosition(end) }.distinct()
        }

        val shortcuts = grid.positions.filter { it.value == '.' || it == start }.flatMap { position ->
            Heading4.values().mapNotNull { heading ->
                position.getNeighbor(heading)?.takeIf { it.value == '#' }?.getNeighbor(heading)
                    ?.takeIf { (it.value == '.' || it == end) && (it.score!! > position.score!!) }
            }.map { it.score!! - position.score!! - 2 }
        }

        print(shortcuts.sorted().groupBy { it }.map { Pair(it.key, it.value.size) })
        return shortcuts.count { it >= maxPicoSeconds }.toString()
    }

    override fun part2(): String {
        val start = grid.positions.first { it.value == 'S' }
        val end = grid.positions.first { it.value == 'E' }

        start.score = 0L

        var currentPosition: List<Position2D> = listOf(start)

        while (currentPosition.isNotEmpty()) {
            currentPosition = currentPosition.flatMap { it.calculateScoreAndGetNextPosition(end) }.distinct()
        }

        val shortcuts = grid.positions.filter { it.value == '.' || it == start }.flatMap { position ->
            val shortcutDestinations = position.findShortcuts(listOf(position))

            shortcutDestinations.mapNotNull { dest ->
                var poss: List<Position2D> = listOf(position)
                val listToClear = mutableListOf<Position2D>()

                val previousDest = dest.score!!

                while (poss.isNotEmpty()) {
                    poss = poss.flatMap { it.calculateScoreAndGetNextPosition(dest, listOf('.'), listOf('#')) }.distinct()
                    listToClear.addAll(poss)
                }

                Shortcut(position, dest, previousDest - dest.score!!).also {
                    dest.score = previousDest
                    listToClear.forEach { it.score = null }
                }.takeIf { it.picoseconds > maxPicoSeconds }
            }
        }

        val all = shortcuts.groupBy { Pair(it.start, it.end) }.map { it.value.maxBy { it.picoseconds } }

        println(all.sortedBy { it.picoseconds }
            .groupBy { it.picoseconds }
            .map { Pair(it.key, it.value.size) })

        return all.count().toString()
    }


    private fun Position2D.findShortcuts(currentShortcut: List<Position2D>): List<Position2D> {
        if (currentShortcut.size > 21) return emptyList()

        val neighbors = Heading4.values().mapNotNull { heading ->
            this.getNeighbor(heading).takeIf { !currentShortcut.contains(it) }
        }

        val first = currentShortcut.first()

        return neighbors.flatMap { neighbor ->
            if (neighbor.value == '.' || neighbor.value == 'E') {
                if (first.score!! < neighbor.score!!) listOf(neighbor) else listOf()
            } else {
                neighbor.findShortcuts(currentShortcut.plus(neighbor))
            }
        }

//        if (currentShortcut.size > 21) return emptyList()
//
//        val origin = currentShortcut.first()
//
//        val neighbors = Heading4.values().mapNotNull { heading ->
//            this.getNeighbor(heading)?.takeIf { !currentShortcut.contains(it) }
//        }
//
//        return neighbors.flatMap { neighbor ->
//            if (neighbor.value == '.') {
//                listOfNotNull(Shortcut(
//                    origin,
//                    neighbor,
//                    currentShortcut.size.toLong() - 1
//                ).takeIf {
//                    neighbor.score!! > origin.score!!
//                            && it.picoseconds >= maxPicoSeconds
//                })
//            } else {
//                neighbor.findShortcuts(currentShortcut.plus(neighbor))
//            }
//        }
    }

    data class Shortcut(val start: Position2D, val end: Position2D, val picoseconds: Long) 
}
