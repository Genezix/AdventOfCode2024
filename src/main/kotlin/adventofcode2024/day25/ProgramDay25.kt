package adventofcode2024.day25

import common.Grid2D
import common.Program
import common.parseGroupsList

class ProgramDay25(brutInputs: List<String>, private val debug: Boolean = false) : Program {
    private val grids =
        brutInputs.parseGroupsList().map { Grid2D.build(it) }.map { it.positions.filter { it.value == '#' } }

    override fun part1(): String {
        val tops = grids.filter { it.count { it.y == 0 } == 5 }
        val bottoms = grids.filter { it.count { it.y == 6 } == 5 }
        return tops.sumOf { sharp -> bottoms.count { it.intersect(sharp).isEmpty() } }.toString()
    }

    override fun part2(): String = ""
}
