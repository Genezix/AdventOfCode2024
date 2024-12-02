package adventofcode2024.day2

import common.Program
import kotlin.math.abs

class ProgramDay2(brutInputs: List<String>, private val debug: Boolean = false) : Program {
    private val inputs = brutInputs.map { it.split(' ').map { it.toLong() } }

    override fun part1(): String = inputs.count { it.isValid() }.toString()

    override fun part2(): String = inputs.count { it.isValid() || it.isValidWithChance() }.toString()

    private fun List<Long>.isValidWithChance() = List(this.size) { removedIndex ->
        this.filterIndexed { index, _ -> index != removedIndex }
    }.any { it.isValid() }

    private fun List<Long>.isValid() =
        this.zipWithNext { a, b ->
            abs(a - b)
        }.all { it in 1..3 } && (this == this.sorted() || this == this.sortedDescending())
}
