package adventofcode2024.day11

import common.Program

class ProgramDay11(brutInputs: List<String>, private val debug: Boolean = false) : Program {
    private val inputs = brutInputs.first().split(" ").map { it.toLong() }

    private val map = mutableMapOf<Long, MutableMap<Int, Long>>()

    override fun part1(): String = inputs.sumBlink(24)

    override fun part2(): String = inputs.sumBlink(74)

    private fun List<Long>.sumBlink(maxIndex: Int): String = this.sumOf {
        it.blink(0, maxIndex)
    }.toString()

    private fun Long.blink(index: Int, maxIndex: Int): Long {
        if (map[this]?.get(index) != null) {
            return map[this]?.get(index)!!
        }

        val newList = when {
            this == 0L -> listOf(1L)
            this.toString().length % 2 == 0 -> listOf(
                this.toString().substring(0, (this.toString().length / 2)).toLong(),
                this.toString().substring((this.toString().length / 2)).toLong()
            )

            else -> listOf(this * 2024)
        }

        if (index == maxIndex) {
            return newList.count().toLong()
        }

        return newList.sumOf { it.blink(index + 1, maxIndex) }.also {
            val element = map[this]
            if (element == null) {
                map[this] = mutableMapOf(index to it)
            } else {
                element[index] = it
            }
        }
    }
}
