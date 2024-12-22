package adventofcode2024.day22

import kotlin.test.assertEquals
import org.junit.Test

internal class Day22KtTest {
    private val program = ProgramDay22(
        brutInputs = listOf(
            "1",
            "10",
            "100",
            "2024",
        ).map { it },
        debug = true
    )

    @Test
    fun part1() {
        assertEquals(
            expected = "37327623",
            actual = program.part1()
        )
    }

    @Test
    fun part2() {
        assertEquals(
            expected = "23",
            actual = ProgramDay22(
                brutInputs = listOf(
                    "1",
                    "2",
                    "3",
                    "2024",
                ).map { it },
                debug = true
            ).part2()
        )
    }
}
