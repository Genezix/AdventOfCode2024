package adventofcode2024.day24

import common.Program
import common.parseGroupsList
import guru.nidi.graphviz.attribute.Attributes
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.engine.GraphvizV8Engine
import guru.nidi.graphviz.model.Factory.graph
import guru.nidi.graphviz.model.Factory.node
import java.io.File

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

    private fun visualise() {
        // Crée les nœuds du graphe
        val outputNodes = computer.outputs.map { it.name to node(it.name) }
        val inputsNodes = computer.inputs.map { it.name to node(it.name) }
        val nodes = (outputNodes + inputsNodes).toMap()

        var graph = graph("logic")
            .directed()

        computer.operations.forEachIndexed { index, ope ->
            val left = nodes[ope.left.name]!!
            val right = nodes[ope.right.name]!!
            val output = nodes[ope.output.name]!!
            val gate = node(ope.gate.toString() + index).with(
                Attributes.attr(
                    "color", when (ope.gate) {
                        Gate.AND -> "red"
                        Gate.OR -> "blue"
                        Gate.XOR -> "green"
                    }
                )
            ).with(
                Attributes.attr(
                    "style", "filled"
                )
            )
            graph = graph.with(
                left.link(gate),
                right.link(gate),
                gate.link(output),
            )
        }

        val file = File("day24.dot")
        // Génère le graphe en PNG
        Graphviz.useEngine(GraphvizV8Engine())
        Graphviz.fromGraph(graph).render(Format.DOT).toFile(file)

        println("Le graphe a été généré : day24.dot")
        println("Lancer la commande : 'dot -Tpng day24.dot -o day24.png' dans la console")
        println("regarder l'image day24.png pour trouver les mauvaises connections")
    }


    // smt
    override fun part2(): String {
        visualise()
        return "ggn,grm,jcb,ndw,twr,z10,z32,z39"
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
