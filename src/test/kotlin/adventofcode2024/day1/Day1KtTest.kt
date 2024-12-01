package adventofcode2024.day1

import org.junit.Test
import kotlin.test.assertEquals

internal class Day1KtTest {
    private val program = ProgramDay1(
        brutInputs = listOf(
            "3   4",
            "4   3",
            "2   5",
            "1   3",
            "3   9",
            "3   3",
        ).map { it },
        debug = true
    )

    @Test
    fun part1() {
        assertEquals(
            expected = "11",
            actual = program.part1()
        )
    }

    @Test
    fun part2() {
        assertEquals(
            expected = "31",
            actual = program.part2()
        )
    }
}
