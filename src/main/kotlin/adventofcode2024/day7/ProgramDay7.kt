package adventofcode2024.day7

import common.Program

class ProgramDay7(brutInputs: List<String>, private val debug: Boolean = false) : Program {
    private val calculs = brutInputs.map { Calcul.build(it) }

    override fun part1(): String {
        val operators = listOf("+", "*")
        return calculs.mapNotNull { isValid(it, operators) }.sum().toString()
    }

    override fun part2(): String {
        val operators = listOf("+", "*", "||")
        return calculs.mapNotNull { isValid(it, operators) }.sum().toString()
    }

    private fun isValid(calcul: Calcul, operators: List<String>): Long? {
        val isValid = calcul.numbers.flatMapIndexed { index, number ->
            operation(number, calcul.numbers.drop(index + 1), operators)
        }.any { it == calcul.result }

        return calcul.result.takeIf { isValid }
    }

    private fun operation(init: Long, numbers : List<Long>, operators: List<String>) : List<Long> {
        if(numbers.isEmpty()) return listOf(init)

        val number = numbers.first()

        return operators.flatMap { operator ->
            operation(
                init = when(operator) {
                    "+" -> init + number
                    "*" -> init * number
                    "||" -> "$init$number".toLong()
                    else -> throw IllegalArgumentException("Unknown operator $operator")
                },
                numbers = numbers.drop(1),
                operators = operators
            )
        }
    }

    data class Calcul(val result: Long, val numbers : List<Long>) {
        companion object {
            fun build(string: String): Calcul {
                val split = string.split(": ")
                return Calcul(
                    result = split.first().toLong(),
                    numbers = split.last().split(" ").map { it.toLong() }
                )
            }
        }
    }
}
