package adventofcode2024.day8

import kotlin.test.assertEquals
import org.junit.Test

internal class Day8KtTest {
    private val program = ProgramDay8(
        brutInputs = listOf(
            "............",
            "........0...",
            ".....0......",
            ".......0....",
            "....0.......",
            "......A.....",
            "............",
            "............",
            "........A...",
            ".........A..",
            "............",
            "............",
        ).map { it },
        debug = true
    )

    @Test
    fun part1() {
        assertEquals(
            expected = "14",
            actual = program.part1()
        )
    }

    @Test
    fun part2() {
        assertEquals(
            expected = "34",
            actual = program.part2()
        )
    }
}
