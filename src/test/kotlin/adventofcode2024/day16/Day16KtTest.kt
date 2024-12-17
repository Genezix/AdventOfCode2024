package adventofcode2024.day16

import org.junit.Test
import kotlin.test.assertEquals

internal class Day16KtTest {
    private val program = ProgramDay16(
        brutInputs = listOf(
            "###############",
            "#.......#....E#",
            "#.#.###.#.###.#",
            "#.....#.#...#.#",
            "#.###.#####.#.#",
            "#.#.#.......#.#",
            "#.#.#####.###.#",
            "#...........#.#",
            "###.#.#####.#.#",
            "#...#.....#.#.#",
            "#.#.#.###.#.#.#",
            "#.....#...#.#.#",
            "#.###.#.#.#.#.#",
            "#S..#.....#...#",
            "###############",
        ).map { it },
        debug = true
    )

    @Test
    fun part1() {
        assertEquals(
            expected = "7036",
            actual = program.part1()
        )
    }

    @Test
    fun part2() {
        assertEquals(
            expected = "45",
            actual = program.part2()
        )
    }

    @Test
    fun part2_1() {
        assertEquals(
            expected = "64",
            actual = ProgramDay16(
                brutInputs = listOf(
                    "#################",
                    "#...#...#...#..E#",
                    "#.#.#.#.#.#.#.#.#",
                    "#.#.#.#...#...#.#",
                    "#.#.#.#.###.#.#.#",
                    "#...#.#.#.....#.#",
                    "#.#.#.#.#.#####.#",
                    "#.#...#.#.#.....#",
                    "#.#.#####.#.###.#",
                    "#.#.#.......#...#",
                    "#.#.###.#####.###",
                    "#.#.#...#.....#.#",
                    "#.#.#.#####.###.#",
                    "#.#.#.........#.#",
                    "#.#.#.#########.#",
                    "#S#.............#",
                    "#################",
                ).map { it },
                debug = true
            ).part2()
        )
    }
}
