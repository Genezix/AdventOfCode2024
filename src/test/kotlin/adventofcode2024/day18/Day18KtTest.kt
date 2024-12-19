package adventofcode2024.day18

import kotlin.test.assertEquals
import org.junit.Test

internal class Day18KtTest {
    private val program = ProgramDay18(
        brutInputs = listOf(
            "5,4",
            "4,2",
            "4,5",
            "3,0",
            "2,1",
            "6,3",
            "2,4",
            "1,5",
            "0,6",
            "3,3",
            "2,6",
            "5,1",
            "1,2",
            "5,5",
            "2,5",
            "6,5",
            "1,4",
            "0,4",
            "6,4",
            "1,1",
            "6,1",
            "1,0",
            "0,5",
            "1,6",
            "2,0",
        ).map { it },
        debug = true,
        maxX = 6,
        maxY = 6,
        nbBytes = 12
    )

    @Test
    fun part1() {
        assertEquals(
            expected = "22",
            actual = program.part1()
        )
    }

    @Test
    fun part2() {
        assertEquals(
            expected = "6,1",
            actual = program.part2()
        )
    }
}
