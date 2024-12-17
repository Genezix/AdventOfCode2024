package adventofcode2024.day17

import common.Program
import common.parseGroupsList
import kotlin.math.pow

class ProgramDay17(brutInputs: List<String>, private val debug: Boolean = false) : Program {
    private val computer = brutInputs.parseGroupsList().let {
        Computer(
            it.first()[0].split(": ")[1].toLong(),
            it.first()[1].split(": ")[1].toLong(),
            it.first()[2].split(": ")[1].toLong(),
            it.last().first().split(": ")[1].split(",").map { it.toLong() },
        )
    }

    override fun part1(): String {
        var instructionPointer = 0L
        while (instructionPointer < computer.instructions.size - 1) {
            instructionPointer = computer.calculateWithPrint(instructionPointer)
        }

        return computer.outputs.joinToString(",")
    }

    override fun part2(): String {
        var instructionPointer = 0L
        while (computer.operations.none { it.resultFunction != null }) {
            instructionPointer = computer.calculateWithPrint(instructionPointer)
        }

        return (0 until 8).flatMap { a ->
            computer.find(a.toLong())
        }.min().toString()
    }

    private fun Computer.find(a: Long, index: Int = 0): List<Long> {
        val result = calculateOperations(a)

        if (result != this.instructions.reversed()[index]) return emptyList()

        return if (index == this.instructions.size - 1) {
            listOf(a)
        } else {
            (0 until 8).flatMap { b -> find(a * 8 + b, index + 1) }
        }
    }

    data class Computer(var registerA: Long, var registerB: Long, var registerC: Long, val instructions: List<Long>) {
        val outputs = mutableListOf<Long>()
        val operations = mutableListOf<Operation>()

        fun calculateOperations(initA: Long): Long {
            var a = initA
            var b = 0L
            var c = 0L

            operations.forEach {
                it.aFunction?.let { a = it.invoke(a, b, c) }
                it.bFunction?.let { b = it.invoke(a, b, c) }
                it.cFunction?.let { c = it.invoke(a, b, c) }
            }

            return operations.first { it.resultFunction != null }.resultFunction!!.invoke(a, b, c)
        }

        fun calculateWithPrint(opCodePointer: Long): Long {
            val opCode = instructions[opCodePointer.toInt()]

            // Get operations
            val operand = instructions[opCodePointer.toInt() + 1]
            val comboFunction: (Long, Long, Long) -> Long = when (operand.toInt()) {
                0, 1, 2, 3 -> { _, _, _ -> operand }
                4 -> { a, _, _ -> a }
                5 -> { _, b, _ -> b }
                6 -> { _, _, c -> c }
                else -> error("combo prout")
            }

            if (operations.none { it.resultFunction != null }) {
                when (opCode.toInt()) {
                    0 -> operations.add(
                        Operation(
                            aFunction = { a, b, c -> (a / 2.0.pow(comboFunction(a, b, c).toDouble())).toLong() }
                        )
                    )
                    1 -> operations.add(
                        Operation(
                            bFunction = { _, b, _ -> b xor operand }
                        )
                    )
                    2 -> operations.add(
                        Operation(
                            bFunction = { a, b, c -> comboFunction(a, b, c) % 8 }
                        )
                    )
                    4 -> operations.add(
                        Operation(
                            bFunction = { _, b, c -> b xor c }
                        )
                    )
                    5 -> operations.add(
                        Operation(
                            resultFunction = { a, b, c -> comboFunction(a, b, c) % 8 }
                        )
                    )
                    6 -> operations.add(
                        Operation(
                            bFunction = { a, b, c -> (a / 2.0.pow(comboFunction(a, b, c).toDouble()).toLong()) }
                        )
                    )
                    7 -> operations.add(
                        Operation(
                            cFunction = { a, b, c -> (a / 2.0.pow(comboFunction(a, b, c).toDouble()).toLong()) }
                        )
                    )
                    else -> error("prout")
                }
            }

            // Calculate

            val combo = when (operand.toInt()) {
                0, 1, 2, 3 -> operand
                4 -> registerA
                5 -> registerB
                6 -> registerC
                else -> error("combo error")
            }

            when (opCode.toInt()) {
                0 -> registerA = (registerA / 2.0.pow(combo.toDouble())).toLong()
                1 -> registerB = registerB xor operand
                2 -> registerB = combo % 8
                3 -> if (registerA != 0L) {
                    return operand
                }
                4 -> registerB = registerB xor registerC
                5 -> outputs.add(combo % 8)
                6 -> registerB = (registerA / 2.0.pow(combo.toDouble())).toLong()
                7 -> registerC = (registerA / 2.0.pow(combo.toDouble())).toLong()
                else -> error("prout")
            }

            return opCodePointer + 2
        }
    }

    data class Operation(
        val aFunction: ((Long, Long, Long) -> Long)? = null,
        val bFunction: ((Long, Long, Long) -> Long)? = null,
        val cFunction: ((Long, Long, Long) -> Long)? = null,
        val resultFunction: ((Long, Long, Long) -> Long)? = null,
    )
}
