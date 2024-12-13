package adventofcode2024.day13

import common.Program
import common.parseGroupsList
import kotlin.math.roundToLong

class ProgramDay13(brutInputs: List<String>, private val debug: Boolean = false) : Program {
    private val machines = brutInputs.parseGroupsList().map { Machine.build(it) }

    override fun part1(): String {
        return machines.newSum().toString()
    }

    override fun part2(): String {
        return machines.map {
            it.copy(
                prize = it.prize.copy(
                    x = it.prize.x + 10000000000000,
                    y = it.prize.y + 10000000000000,
                )
            )
        }.newSum(false).toString()
    }

    private fun List<Machine>.newSum(hasLimit: Boolean = true): Long {
        return this.sumOf { machine ->
            val pX = machine.prize.x.toDouble()
            val pY = machine.prize.y.toDouble()

            val aX = machine.buttonA.x.toDouble()
            val aY = machine.buttonA.y.toDouble()

            val bX = machine.buttonB.x.toDouble()
            val bY = machine.buttonB.y.toDouble()

            val nbA = (((pY / bY) - (pX / bX)) / ((aY / bY) - (aX / bX))).roundToLong()
            val nbB = (((pY / aY) - (pX / aX)) / ((bY / aY) - (bX / aX))).roundToLong()

            val verifyX = pX == nbA * aX + nbB * bX
            val verifyY = pY == nbA * aY + nbB * bY

            (if (!verifyX || !verifyY) 0
            else if (hasLimit && (nbA > 100 || nbB > 100)) 0
            else nbA * machine.buttonA.tokens + nbB * machine.buttonB.tokens)
        }
    }

    private fun List<Machine>.mySum(): Long {
        return this.sumOf { machine ->
            val divAX = (machine.prize.x / machine.buttonA.x).let { if (it > 100) 100 else it }
            val divBX = (machine.prize.x / machine.buttonB.x).let { if (it > 100) 100 else it }
            val divAY = (machine.prize.y / machine.buttonA.y).let { if (it > 100) 100 else it }
            val divBY = (machine.prize.y / machine.buttonB.y).let { if (it > 100) 100 else it }

            val maxNbPushA = minOf(divAX, divAY)
            val maxNbPushB = minOf(divBX, divBY)

            ((0..maxNbPushA).flatMap { a ->
                val ax = machine.buttonA.x * a
                val ay = machine.buttonA.y * a

                val remainingX = machine.prize.x - ax
                val remainingY = machine.prize.y - ay

                val maxPushB = minOf(remainingY / machine.buttonB.y, remainingX / machine.buttonB.x, maxNbPushB)

                (0..maxPushB).mapNotNull { b ->
                    val resultX = machine.buttonA.x * a + machine.buttonB.x * b
                    val resultY = machine.buttonA.y * a + machine.buttonB.y * b

                    if (resultY == machine.prize.y && resultX == machine.prize.x) {

                        machine.buttonA.tokens * a + machine.buttonB.tokens * b
                    } else null
                }
            }.minOrNull() ?: 0).also { println(it) }
        }
    }

    data class Prize(val x: Long, val y: Long)

    data class Machine(val buttonA: Button, val buttonB: Button, val prize: Prize) {
        companion object {
            private val prizeRegex = "Prize: X=(\\d*), Y=(\\d*)".toRegex()

            fun build(inputs: List<String>): Machine {
                val buttonA = Button.build(inputs.first(), 3)
                val buttonB = Button.build(inputs[1], 1)
                val prize = prizeRegex.find(inputs[2])!!.groupValues.let {
                    Prize(it[1].toLong(), it[2].toLong())
                }
                return Machine(buttonA, buttonB, prize)
            }
        }
    }

    data class Button(val name: String, val tokens: Int, val x: Long, val y: Long) {
        companion object {
            private val buttonRegex = "Button (.*): X\\+(\\d*), Y\\+(\\d*)".toRegex()
            fun build(input: String, tokens: Int): Button {
                return buttonRegex.find(input)!!.groupValues.let {
                    Button(it[1], tokens, it[2].toLong(), it[3].toLong())
                }
            }
        }
    }
}
