package adventofcode2024.day24

import common.Program
import common.parseGroupsList

class ProgramDay24(brutInputs: List<String>, private val debug: Boolean = false) : Program {
    private val computer = Computer.build(brutInputs)

    override fun part1(): String {
        while (computer.outputs.any { it.isActive == null }) {
            computer.operations.filter { it.left.isActive != null && it.right.isActive != null && it.output.isActive == null }
                .forEach {
                    it.output.isActive = it.gate.operate(it.left, it.right)
                }
        }
        return computer.outputs
            .filter { it.name.startsWith("z") }
            .toBinaryLong()
            .toString()
    }


    private fun Operation.print() : String {
        return "(" + (computer.operations.firstOrNull { it.output == this.left }?.print() ?: this.left.name) + " " + this.gate + " " + (computer.operations.firstOrNull { it.output == this.right }?.print()?: this.right.name) +")"
    }

    override fun part2(): String {

        val xs = computer.inputs.filter { it.name.startsWith("x") }.toBinaryLong()
        val ys = computer.inputs.filter { it.name.startsWith("y") }.toBinaryLong()

        val expectedResult = (xs + ys).toString(2)

        while (computer.outputs.any { it.isActive == null }) {
            computer.operations.filter { it.left.isActive != null && it.right.isActive != null && it.output.isActive == null }
                .forEach {
                    it.output.isActive = it.gate.operate(it.left, it.right)
                }
        }

        computer.operations.forEach {
            if(it.output.name.startsWith("z") && it.gate != Gate.XOR) println(it)
        }

//        computer.outputs.filter { it.name.startsWith("z") }.forEach { input ->
//            println()
//            println("========= $input ==========")
//            println(computer.operations.firstOrNull { it.output == input }?.print())
//        }

//        expectedResult.reversed().forEachIndexed { index, z ->
//            val zTarget = "z" + (if (index < 10) "0" else "") + index
//            val xTarget = "x" + (if (index < 10) "0" else "") + index
//            val yTarget = "y" + (if (index < 10) "0" else "") + index
//            computer.outputs.first { it.name == zTarget }.let {
//                if(it.isActive != (z == '1')) println("bad z $zTarget")
//            }
//                val ope = computer.operations.first { it.left.name == xTarget || it.right.name == xTarget }
//                if((z == '1') != ope.output.isActive) { println("${z == '1'} == ${ope.output.isActive}") }
//        }

//        val zOperations = computer.operations.filter { it.output.name.startsWith("z") }


//        while (computer.outputs.any { it.isActive == null }) {
//            computer.operations.filter { it.left.isActive != null && it.right.isActive != null && it.output.isActive == null }
//                .forEach {
//                    it.output.isActive = it.gate.operate(it.left, it.right)
//                }
//        }


        val result = emptyList<Input>()//searchZToSwape(0, expectedResult, zOperations, listOf())

//        println(zOperations.map { it.output }.toBinaryLong())
        print("$expectedResult : 101000 == $result")

        print("$expectedResult")


        return result.sortedBy { it.name }.joinToString(",") { it.name }
    }

    private fun searchZToSwape(
        index: Int,
        expectedZ: String,
//        zOperations: List<Operation>,
        currentSwapped: List<Operation>
    ): List<Operation> {
        // on a trop swap
        if(currentSwapped.size > 8) return emptyList()
        // resultat faux et pas swape 4 fois
        if(index == expectedZ.length && currentSwapped.size != 8) return emptyList()

        // victoire !
        if (index == expectedZ.length) return currentSwapped

        val expectedBit = expectedZ[index]
        val zTarget = "z" + (if (index < 10) "0" else "") + index

        val currentZ = computer.operations.first { it.output.name == zTarget }

        // Si le z est déjà ok on passe au suivent et on test
        if (currentZ.output.isActive == (expectedBit == '1')) {
            val testResult = searchZToSwape(
                index + 1,
                expectedZ,
                currentSwapped
            )
            // Victoire !!
            if(testResult.size == 8) return testResult
        }

        val possibleOthersZ = computer.operations.filter {
            it.output.name != zTarget
                    && it.output.name.removePrefix("z").toInt() > index
                    && it.output.isActive == (expectedBit == '1')
                    && it !in currentSwapped
        }

        // Si plus d'autre possibilité alors c'est perdu
        if(possibleOthersZ.isEmpty()) return emptyList()

        possibleOthersZ.forEach { otherOperation ->
            // On swap
            val previous = otherOperation.output
            otherOperation.output = currentZ.output
            currentZ.output = previous

            val resultTest = searchZToSwape(
                index + 1,
                expectedZ,
                currentSwapped.plus(listOf(currentZ, otherOperation))
            )

            // On inverse le swap
            val previousAgain = otherOperation.output
            otherOperation.output = currentZ.output
            currentZ.output = previousAgain

            // Victoire
            if(resultTest.size == 8) return resultTest
        }

        return emptyList()
    }

    private fun List<Input>.toBinaryLong(): Long {
        return this.sortedBy { it.name }
            .joinToString("") { if (it.isActive!!) "1" else "0" }
            .reversed()
            .let {
                it.toLong(2)
            }
    }

    data class Computer(val inputs: List<Input>, val operations: List<Operation>, val outputs: List<Input>) {
        companion object {
            fun build(brutInputs: List<String>): Computer {
                val groups = brutInputs.parseGroupsList()
                val inputs = groups[0].map { it.split(": ").let { Input(it[0], it[1] == "1") } }
                val outputs = groups[1].map { it.split(" ").last() }.map { Input(it) }
                val operations = groups[1].map {
                    it.split(" ").filterNot { it == "->" }.let { operationInfo ->
                        Operation(
                            inputs.firstOrNull { it.name == operationInfo[0] }
                                ?: outputs.firstOrNull { it.name == operationInfo[0] }!!,
                            inputs.firstOrNull { it.name == operationInfo[2] }
                                ?: outputs.firstOrNull { it.name == operationInfo[2] }!!,
                            Gate.values().first { it.name == operationInfo[1] },
                            outputs.firstOrNull { it.name == operationInfo[3] }!!,
                        )
                    }
                }
                return Computer(inputs, operations, outputs)
            }
        }
    }

    data class Operation(val left: Input, val right: Input, val gate: Gate, var output: Input)

    data class Input(val name: String, var isActive: Boolean? = null)

    enum class Gate {
        AND,
        OR,
        XOR;

        fun operate(left: Input, right: Input): Boolean {
            if (left.isActive == null || right.isActive == null) error("prout")

            return when (this) {
                AND -> left.isActive!! && right.isActive!!
                OR -> left.isActive!! || right.isActive!!
                XOR -> left.isActive != right.isActive
            }
        }
    }
}
