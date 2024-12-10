package adventofcode2024.day10

import kotlin.test.assertEquals
import org.junit.Test

internal class Day10KtTest {
    private val program = ProgramDay10(
        brutInputs = listOf(
            "89010123",
            "78121874",
            "87430965",
            "96549874",
            "45678903",
            "32019012",
            "01329801",
            "10456732",
        ).map { it },
        debug = true
    )

    @Test
    fun part1() {
        assertEquals(
            expected = "36",
            actual = program.part1()
        )
    }

    @Test
    fun part2() {
        assertEquals(
            expected = "81",
            actual = program.part2()
        )
    }
}
