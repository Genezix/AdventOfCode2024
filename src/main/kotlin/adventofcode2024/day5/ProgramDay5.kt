package adventofcode2024.day5

import common.Program
import common.parseGroupsList

class ProgramDay5(brutInputs: List<String>, private val debug: Boolean = false) : Program {
    private val inputs = brutInputs

    override fun part1(): String {
        val pages = Pages.from(inputs)

        return pages.updates.mapNotNull { pageUpdates ->
            pageUpdates.takeIf { it.isRespectingRules(pages.rules) }
        }.sumOf { it[it.size / 2] }.toString()
    }

    override fun part2(): String {
        val pages = Pages.from(inputs)

        return pages.updates.mapNotNull { pageUpdates ->
            pageUpdates.takeIf { !it.isRespectingRules(pages.rules) }
        }.map { it.reorder(pages.rules) }
            .sumOf { it[it.size / 2] }.toString()
    }

    private fun List<Long>.reorder(rules: List<Rule>): List<Long> = this.sortedWith { previous, next ->
        if (listOf(previous, next).isRespectingRules(rules)) 1 else -1
    }

    private fun List<Long>.isRespectingRules(rules: List<Rule>): Boolean {
        forEachIndexed { index, page ->
            if (index < this.size - 1) {
                if (rules.any { it.left == page && it.right == this[index + 1] }) {
                    return@forEachIndexed
                }

                if (rules.any { it.left == this[index + 1] && it.right == page }) {
                    return false
                }
            }
        }
        return true
    }

    data class Pages(val rules: List<Rule>, val updates: List<List<Long>>) {
        companion object {
            fun from(inputs: List<String>): Pages {
                val lists = inputs.parseGroupsList()
                val pagesMoreLess = lists.first()

                val moreLess =
                    pagesMoreLess.map { it.split('|').map { it.toLong() }.let { Rule(it.first(), it.last()) } }

                val updates = lists.last().map { it.split(',').map { it.toLong() } }
                return Pages(moreLess, updates)
            }
        }
    }

    data class Rule(val left: Long, val right: Long)
}
