package adventofcode2024.day20

import org.junit.Test
import kotlin.test.assertEquals

internal class Day20KtTest {
    private val program = ProgramDay20(
        brutInputs = listOf(
            "###############",
            "#...#...#.....#",
            "#.#.#.#.#.###.#",
            "#S#...#.#.#...#",
            "#######.#.#.###",
            "#######.#.#...#",
            "#######.#.###.#",
            "###..E#...#...#",
            "###.#######.###",
            "#...###...#...#",
            "#.#####.#.###.#",
            "#.#...#.#.#...#",
            "#.#.#.#.#.#.###",
            "#...#...#...###",
            "###############",
        ).map { it },
        debug = true,
        maxPicoSeconds = 0,
    )

    @Test
    fun part1() {
        assertEquals(
            expected = "44",
            actual = program.part1()
        )
    }

    @Test
    fun part2() {
        assertEquals(
            expected = "285",
            actual = ProgramDay20(
                brutInputs = program.brutInputs,
                debug = true,
                maxPicoSeconds = 50,
            ).part2()
        )
    }
}
