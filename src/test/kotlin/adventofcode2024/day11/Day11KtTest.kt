package adventofcode2024.day11

import kotlin.test.assertEquals
import org.junit.Test

internal class Day11KtTest {
    private val program = ProgramDay11(
        brutInputs = listOf(
            "125 17",
        ).map { it },
        debug = true
    )

    @Test
    fun part1() {
        assertEquals(
            expected = "55312",
            actual = program.part1()
        )
    }

    @Test
    fun part2() {
        assertEquals(
            expected = "65601038650482",
            actual = program.part2()
        )
    }
}
