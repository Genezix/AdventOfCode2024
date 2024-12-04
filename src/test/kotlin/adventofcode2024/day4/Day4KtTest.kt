package adventofcode2024.day4

import kotlin.test.assertEquals
import org.junit.Test

internal class Day4KtTest {
    private val program = ProgramDay4(
        brutInputs = listOf(
            "MMMSXXMASM",
            "MSAMXMSMSA",
            "AMXSXMAAMM",
            "MSAMASMSMX",
            "XMASAMXAMM",
            "XXAMMXXAMA",
            "SMSMSASXSS",
            "SAXAMASAAA",
            "MAMMMXMMMM",
            "MXMXAXMASX",
        ).map { it },
        debug = true
    )

    @Test
    fun part1() {
        assertEquals(
            expected = "18",
            actual = program.part1()
        )
    }

    @Test
    fun part2() {
        assertEquals(
            expected = "9",
            actual = program.part2()
        )
    }
}
