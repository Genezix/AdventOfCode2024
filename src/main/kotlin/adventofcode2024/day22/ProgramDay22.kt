package adventofcode2024.day22

import common.Program

class ProgramDay22(brutInputs: List<String>, private val debug: Boolean = false) : Program {
    private val secrets = brutInputs.map { it.toLong() }

    private fun Long.calculateNextSecret(): Long {
        return this.let {
            ((it * 64) xor it) % 16777216
        }.let {
            ((it / 32) xor it) % 16777216
        }.let {
            ((it * 2048) xor it) % 16777216
        }
    }


    override fun part1(): String {
        return secrets.sumOf {
            var result = it
            repeat(2000) { result = result.calculateNextSecret() }
            result
        }.toString()
    }

    override fun part2(): String {
        val patternsScores = mutableMapOf<List<Int>, MutableMap<Long, Int>>()

        secrets.forEach { secret ->
            val fours = mutableListOf<Int>()
            var result = secret
            repeat(2000) {
                val previous = result.toString().last().toString().toInt()
                result = result.calculateNextSecret()
                val next = result.toString().last().toString().toInt()
                if (fours.size == 4) {
                    fours.removeFirst()
                }

                fours.add(next - previous)

                if (fours.size == 4) {
                    patternsScores[fours.toList()] = patternsScores.getOrDefault(fours.toList(), mutableMapOf()).apply {
                        if(this[secret] == null) {
                            this[secret] = next
                        }
                    }
                }
            }

        }

        return patternsScores.maxOfOrNull { it.value.values.sum() }.toString()
    }
}

// 2024 toHigh
