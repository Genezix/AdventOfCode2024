package adventofcode2024.day5

import kotlin.test.assertEquals
import org.junit.Test

internal class Day5KtTest {
    private val program = ProgramDay5(
        brutInputs = listOf(
            "47|53",
            "97|13",
            "97|61",
            "97|47",
            "75|29",
            "61|13",
            "75|53",
            "29|13",
            "97|29",
            "53|29",
            "61|53",
            "97|53",
            "61|29",
            "47|13",
            "75|47",
            "97|75",
            "47|61",
            "75|61",
            "47|29",
            "75|13",
            "53|13",
            "",
            "75,47,61,53,29",
            "97,61,53,29,13",
            "75,29,13",
            "75,97,47,61,53",
            "61,13,29",
            "97,13,75,29,47",
        ).map { it },
        debug = true
    )

    @Test
    fun part1() {
        assertEquals(
            expected = "143",
            actual = program.part1()
        )
    }

    @Test
    fun part2() {
        assertEquals(
            expected = "123",
            actual = program.part2()
        )
    }
}
