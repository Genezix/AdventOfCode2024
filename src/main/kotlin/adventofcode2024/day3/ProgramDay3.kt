package adventofcode2024.day3

import common.Program

class ProgramDay3(brutInputs: List<String>, private val debug: Boolean = false) : Program {
    private val inputs = brutInputs

    override fun part1(): String {
        val regex = "mul\\(([0-9][0-9]?[0-9]?),([0-9][0-9]?[0-9]?)\\)".toRegex()
        return inputs.sumOf {
            regex.findAll(it).sumOf { it.groups[1]!!.value.toLong() * it.groups[2]!!.value.toLong() }
        }.toString()
    }

    override fun part2(): String {
        val regex = "(mul\\(([0-9][0-9]?[0-9]?),([0-9][0-9]?[0-9]?)\\))|(do\\(\\))|(don't\\(\\))".toRegex()
        var isActive = true
        return inputs.fold(0L) { acc, it ->
            regex.findAll(it).fold(acc) { res, match ->
                val value = match.value
                res + when {
                    value.startsWith("mul") -> if(isActive) match.groups[2]!!.value.toLong() * match.groups[3]!!.value.toLong() else 0
                    value.startsWith("don") -> {
                        isActive = false
                        0
                    }
                    value.startsWith("do") -> {
                        isActive = true
                        0
                    }
                    else -> error("prout")
                }
            }
        }.toString()
    }
}
