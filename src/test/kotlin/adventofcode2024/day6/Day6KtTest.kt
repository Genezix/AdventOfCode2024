package adventofcode2024.day6

import org.junit.Test
import kotlin.test.assertEquals

internal class Day6KtTest {
    private val program = ProgramDay6(
        brutInputs = listOf(
            "....#.....",
            ".........#",
            "..........",
            "..#.......",
            ".......#..",
            "..........",
            ".#..^.....",
            "........#.",
            "#.........",
            "......#...",
        ).map { it },
        debug = true
    )

    @Test
    fun part1() {
        assertEquals(
            expected = "41",
            actual = program.part1()
        )
    }

    @Test
    fun part2() {
        assertEquals(
            expected = "6",
            actual = program.part2()
        )
    }
}
