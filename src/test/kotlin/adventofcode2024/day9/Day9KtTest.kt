package adventofcode2024.day9

import kotlin.test.assertEquals
import org.junit.Test

internal class Day9KtTest {
    private val program = ProgramDay9(
        brutInputs = listOf(
            "2333133121414131402"
        ).map { it },
        debug = true
    )

    @Test
    fun part1() {
        assertEquals(
            expected = "1928",
            actual = program.part1()
        )
    }

    @Test
    fun part2() {
        assertEquals(
            expected = "2858",
            actual = program.part2()
        )
    }
}
