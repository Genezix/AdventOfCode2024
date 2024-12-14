package adventofcode2024.day14

import kotlin.test.assertEquals
import org.junit.Test

internal class Day14KtTest {
    private val program = ProgramDay14(
        brutInputs = listOf(
            "p=0,4 v=3,-3",
            "p=6,3 v=-1,-3",
            "p=10,3 v=-1,2",
            "p=2,0 v=2,-1",
            "p=0,0 v=1,3",
            "p=3,0 v=-2,-2",
            "p=7,6 v=-1,-3",
            "p=3,0 v=-1,-2",
            "p=9,3 v=2,3",
            "p=7,3 v=-1,2",
            "p=2,4 v=2,-3",
            "p=9,5 v=-3,-3",
        ).map { it },
        debug = true,
        nbX = 11,
        nbY = 7,
    )

    @Test
    fun part1() {
        assertEquals(
            expected = "12",
            actual = program.part1()
        )
    }

    @Test
    fun part2() {
        // Pas de part 2 testable
        assertEquals(
            expected = "",
            actual = ""
        )
    }
}
