package adventofcode2024.day4

import common.Program

class ProgramDay4(brutInputs: List<String>, private val debug: Boolean = false) : Program {
    private val inputs = brutInputs
    private val nbX = inputs.first().length
    private val nbY = inputs.size

    private fun List<String>.isCharAt(x: Int, y: Int, char: Char): Boolean {
        return this.getOrNull(y)?.getOrNull(x) == char
    }

    override fun part1(): String {
        val xNb = inputs.first().length
        val yNb = inputs.size

        return 0.until(xNb).flatMap { x ->
            0.until(yNb).map { y ->
                if (inputs.isCharAt(x, y, 'X'))
                    listOf(
                        inputs.isCharAt(x, y + 1, 'M')
                                && inputs.isCharAt(x, y + 2, 'A')
                                && inputs.isCharAt(x, y + 3, 'S'),

                        inputs.isCharAt(x + 1, y + 1, 'M')
                                && inputs.isCharAt(x + 2, y + 2, 'A')
                                && inputs.isCharAt(x + 3, y + 3, 'S'),

                        inputs.isCharAt(x - 1, y + 1, 'M')
                                && inputs.isCharAt(x - 2, y + 2, 'A')
                                && inputs.isCharAt(x - 3, y + 3, 'S'),

                        inputs.isCharAt(x, y - 1, 'M')
                                && inputs.isCharAt(x, y - 2, 'A')
                                && inputs.isCharAt(x, y - 3, 'S'),

                        inputs.isCharAt(x + 1, y - 1, 'M')
                                && inputs.isCharAt(x + 2, y - 2, 'A')
                                && inputs.isCharAt(x + 3, y - 3, 'S'),

                        inputs.isCharAt(x - 1, y - 1, 'M')
                                && inputs.isCharAt(x - 2, y - 2, 'A')
                                && inputs.isCharAt(x - 3, y - 3, 'S'),

                        inputs.isCharAt(x + 1, y, 'M')
                                && inputs.isCharAt(x + 2, y, 'A')
                                && inputs.isCharAt(x + 3, y, 'S'),

                        inputs.isCharAt(x - 1, y, 'M')
                                && inputs.isCharAt(x - 2, y, 'A')
                                && inputs.isCharAt(x - 3, y, 'S'),
                    ).count { it }
                else 0
            }
        }.sum().toString()
    }

    override fun part2(): String {
        val xNb = inputs.first().length
        val yNb = inputs.size

        return 0.until(xNb).sumOf { x ->
            0.until(yNb).count { y ->
                inputs.isCharAt(x, y, 'A')
                        && (
                        (inputs.isCharAt(x - 1, y - 1, 'M') && inputs.isCharAt(x + 1, y + 1, 'S')
                                || inputs.isCharAt(x - 1, y - 1, 'S') && inputs.isCharAt(x + 1, y + 1, 'M'))
                                &&
                                (inputs.isCharAt(x - 1, y + 1, 'M') && inputs.isCharAt(x + 1, y - 1, 'S') ||
                                        inputs.isCharAt(x - 1, y + 1, 'S') && inputs.isCharAt(x + 1, y - 1, 'M')))
            }
        }.toString()
    }
}
