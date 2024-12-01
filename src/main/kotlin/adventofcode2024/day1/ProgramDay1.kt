package adventofcode2024.day1

import common.Program

class ProgramDay1(brutInputs: List<String>, private val debug: Boolean = false) : Program {
    private val inputs = brutInputs.twoList()

    override fun part1(): String = inputs.first.mapIndexed { index, value ->
        kotlin.math.abs(value - inputs.second[index])
    }.sum().toString()

    override fun part2(): String = inputs.first.sumOf { value ->
        value * inputs.second.count { it == value }
    }.toString()

    private fun List<String>.twoList(): Pair<List<Long>, List<Long>> {
        val lists = this.map { it.split("   ") }
        val firstList = lists.map { it.first().toLong() }.sorted()
        val secondList = lists.map { it.last().toLong() }.sorted()
        return Pair(firstList, secondList)
    }
}
