package common

import java.math.BigInteger

fun String.containsAll(chars: String) = chars.all { this.contains(it) }

fun String.hexToBinary(): String = Integer.toBinaryString(toInt(16))

fun List<Long>.multiply(): Long {
    var multiply = 1L
    for (element in this) {
        multiply *= element
    }
    return multiply
}

fun List<Int>.multiply(): Int {
    var multiply = 1
    for (element in this) {
        multiply *= element
    }
    return multiply
}

fun List<BigInteger>.multiply(): BigInteger {
    var multiply = BigInteger.ONE
    for (element in this) {
        multiply = multiply.multiply(element)
    }
    return multiply
}


fun List<String>.parseGroups(separator: String = " "): List<String> {
    var previousLine = ""
    return this.mapIndexedNotNull { index, line ->
        if (line.isEmpty() || index == this.size - 1) {
            ("$previousLine$separator$line").apply { previousLine = "" }
        } else {
            previousLine += "$separator$line"
            null
        }
    }.map { it.trim() }
}

fun List<String>.parseGroupsList(): List<List<String>> {
    val previousLine = mutableListOf<String>()
    return this.mapIndexedNotNull { index, line ->
        if (line.isEmpty() || index == this.size - 1) {
            if (index == this.size - 1) previousLine.add(line)
            previousLine.toList().apply { previousLine.clear() }
        } else {
            previousLine.add(line)
            null
        }
    }
}
