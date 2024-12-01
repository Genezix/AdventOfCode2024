package common

interface Program {
    fun part1(): String
    fun part2(): String
}

fun execute(day: String, program: (input: List<String>) -> Program) {
    executePart(day, "1") { input -> program(input).part1() }
    executePart(day, "2") { input -> program(input).part2() }
}

fun executePart(day: String, part: String, partFunction: (input: List<String>) -> String) {
    val input = FileLoader.readFile("adventofcode2024/day${day}.txt")

    val startTime = System.currentTimeMillis()
    val resultPart = partFunction(input)
    val resultTime = (System.currentTimeMillis() - startTime) / 1000

    println("=======================")
    println("Result part $part => $resultPart")
    println("Time $resultTime seconds")
    println("=======================")
}
