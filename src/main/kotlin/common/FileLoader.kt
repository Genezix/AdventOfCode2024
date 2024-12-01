package common

import java.nio.file.Files
import java.nio.file.Paths

class FileLoader {
    companion object {
        fun readFile(url: String): List<String> = Files.readAllLines(Paths.get("src/main/resources/$url"))
    }
}

fun main() {
    val lines = FileLoader.readFile("2020/day1part1.txt")
    println(lines)
}
