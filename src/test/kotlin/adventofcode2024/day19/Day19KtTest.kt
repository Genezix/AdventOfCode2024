package adventofcode2024.day19

import kotlin.test.assertEquals
import org.junit.Test

internal class Day19KtTest {
    private val program = ProgramDay19(
        brutInputs = listOf(
            "r, wr, b, g, bwu, rb, gb, br",
            "",
            "brwrr",
            "bggr",
            "gbbr",
            "rrbgbr",
            "ubwu",
            "bwurrg",
            "brgr",
            "bbrgwb",
        ).map { it },
        debug = true
    )

    @Test
    fun part1() {
        assertEquals(
            expected = "6",
            actual = program.part1()
        )
    }

    @Test
    fun part2() {
        assertEquals(
            expected = "16",
            actual = program.part2()
        )
    }
}
