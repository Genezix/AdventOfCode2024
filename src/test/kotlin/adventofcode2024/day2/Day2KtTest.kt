package adventofcode2024.day2

import kotlin.test.assertEquals
import org.junit.Test

internal class Day2KtTest {
    private val program = ProgramDay2(
        brutInputs = listOf(
            "7 6 4 2 1",
            "1 2 7 8 9",
            "9 7 6 2 1",
            "1 3 2 4 5",
            "8 6 4 4 1",
            "1 3 6 7 9",
        ).map { it },
        debug = true
    )

    @Test
    fun part1() {
        assertEquals(
            expected = "2",
            actual = program.part1()
        )
    }

    @Test
    fun part2() {
        assertEquals(
            expected = "4",
            actual = program.part2()
        )
    }
}
