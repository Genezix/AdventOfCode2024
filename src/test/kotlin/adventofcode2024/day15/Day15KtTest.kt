package adventofcode2024.day15

import kotlin.test.assertEquals
import org.junit.Test

internal class Day15KtTest {
    private val program = ProgramDay15(
        brutInputs = listOf(
            "##########",
            "#..O..O.O#",
            "#......O.#",
            "#.OO..O.O#",
            "#..O@..O.#",
            "#O#..O...#",
            "#O..O..O.#",
            "#.OO.O.OO#",
            "#....O...#",
            "##########",
            "",
            "<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^",
            "vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v",
            "><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<",
            "<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^",
            "^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><",
            "^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^",
            ">^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^",
            "<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>",
            "^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>",
            "v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^",
        ).map { it },
        debug = true
    )

    @Test
    fun part1() {
        assertEquals(
            expected = "10092",
            actual = program.part1()
        )
    }

    @Test
    fun part1_1() {
        assertEquals(
            expected = "2028",
            actual = ProgramDay15(
                brutInputs = listOf(
                    "########",
                    "#..O.O.#",
                    "##@.O..#",
                    "#...O..#",
                    "#.#.O..#",
                    "#...O..#",
                    "#......#",
                    "########",
                    "",
                    "<^^>>>vv<v>>v<<",
                ).map { it },
                debug = true
            ).part1()
        )
    }

    @Test
    fun part2_1() {
        assertEquals(
            expected = "618",
            actual = ProgramDay15(
                brutInputs = listOf(
                    "#######",
                    "#...#.#",
                    "#.....#",
                    "#..OO@#",
                    "#..O..#",
                    "#.....#",
                    "#######",
                    "",
                    "<vv<<^^<<^^",
                ).map { it },
                debug = true
            ).part2()
        )
    }

    @Test
    fun part2() {
        assertEquals(
            expected = "9021",
            actual = program.part2()
        )
    }
}
