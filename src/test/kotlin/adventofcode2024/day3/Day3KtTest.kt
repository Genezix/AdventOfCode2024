package adventofcode2024.day3

import kotlin.test.assertEquals
import org.junit.Test

internal class Day3KtTest {
    private val program = ProgramDay3(
        brutInputs = listOf(
            "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"
        ).map { it },
        debug = true
    )

    @Test
    fun part1() {
        assertEquals(
            expected = "161",
            actual = program.part1()
        )
    }

    @Test
    fun part2() {
        assertEquals(
            expected = "48",
            actual = program.part2()
        )
    }
}
