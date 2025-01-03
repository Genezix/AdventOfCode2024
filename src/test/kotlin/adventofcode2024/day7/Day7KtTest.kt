package adventofcode2024.day7

import kotlin.test.assertEquals
import org.junit.Test

internal class Day7KtTest {
    private val program = ProgramDay7(
        brutInputs = listOf(
            "190: 10 19",
            "3267: 81 40 27",
            "83: 17 5",
            "156: 15 6",
            "7290: 6 8 6 15",
            "161011: 16 10 13",
            "192: 17 8 14",
            "21037: 9 7 18 13",
            "292: 11 6 16 20",
        ).map { it },
        debug = true
    )

    @Test
    fun part1() {
        assertEquals(
            expected = "3749",
            actual = program.part1()
        )
    }

    @Test
    fun part2() {
        assertEquals(
            expected = "11387",
            actual = program.part2()
        )
    }
}
