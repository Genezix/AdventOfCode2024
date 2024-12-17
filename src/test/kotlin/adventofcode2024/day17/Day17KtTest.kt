package adventofcode2024.day17

import org.junit.Test
import kotlin.test.assertEquals

internal class Day17KtTest {
    private val program = ProgramDay17(
        brutInputs = listOf(
            "Register A: 729",
            "Register B: 0",
            "Register C: 0",
            "",
            "Program: 0,1,5,4,3,0",
        ).map { it },
        debug = true
    )

    @Test
    fun part1() {
        assertEquals(
            expected = "4,6,3,5,6,3,5,2,1,0",
            actual = program.part1()
        )
    }

    @Test
    fun part2() {
        assertEquals(
            expected = "117440",
            actual = ProgramDay17(
                brutInputs = listOf(
                    "Register A: 2024",
                    "Register B: 0",
                    "Register C: 0",
                    "",
                    "Program: 0,3,5,4,3,0",
                ).map { it },
                debug = true
            ).part2()
        )
    }
}
