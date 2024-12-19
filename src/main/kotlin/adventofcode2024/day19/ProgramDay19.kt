package adventofcode2024.day19

import common.Program
import common.parseGroupsList

class ProgramDay19(brutInputs: List<String>, private val debug: Boolean = false) : Program {
    private val inputs = brutInputs.parseGroupsList()

    private val colors = inputs.first().first().split(", ").map { Color(it) }
    private val patterns = inputs.last()

    override fun part1(): String = patterns.count { pattern -> pattern.canBeMade() }.toString()

    override fun part2(): String = patterns.sumOf { pattern -> pattern.countNbMadePossibility() }.toString()

    val cache = mutableMapOf<String, Long>()

    private fun String.countNbMadePossibility(): Long {
        cache[this]?.let { return it }

        return colors
            .filter { this.startsWith(it.name) }
            .sumOf { color ->
                val rest = this.removePrefix(color.name)
                when {
                    rest.isEmpty() -> 1
                    else -> rest.countNbMadePossibility()
                }
            }.also { cache[this] = it }
    }

    private fun String.canBeMade(): Boolean = colors
        .filter { this.startsWith(it.name) }
        .any { color ->
            val rest = this.removePrefix(color.name)
            rest.isEmpty() || rest.canBeMade()
        }

    data class Color(val name: String)
}
