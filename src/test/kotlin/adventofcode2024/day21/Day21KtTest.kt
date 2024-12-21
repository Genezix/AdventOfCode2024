package adventofcode2024.day21

import kotlin.test.assertEquals
import org.junit.Test

internal class Day21KtTest {
    private val program = ProgramDay21(
        brutInputs = listOf(
            "029A",
            "980A",
            "179A",
            "456A",
            "379A",
        ).map { it },
        debug = true
    )

    @Test
    fun part1() {
        assertEquals(
            expected = "126384",
            actual = program.part1()
        )
    }

    @Test
    fun part2() {
        assertEquals(
            expected = "",
            actual = program.part2()
        )
    }
}
