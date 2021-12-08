import java.io.File

//  sources: a, b, c, d, e, f, g
//           |  |  |  |  |  |  |
//           ?????  wiring ?????
//           |  |  |  |  |  |  |
// segments: a, b, c, d, e, f, g

fun <T> permutations(all: List<T>): List<List<T>> {
    if (all.size == 1) return listOf(all)
    val result = mutableListOf<List<T>>()
    for (i in all.indices) {
        val head = all[i]
        for (p in permutations(all.filter { it != head })) {
            result.add(listOf(head) + p)
        }
    }
    return result
}

val possibleWirings: List<Map<Char, Char>> = permutations("abcdefg".toList()).map {
    it.mapIndexed { index, c -> c to "abcdefg"[index] }.toMap()
}

val digits = mapOf(
    "abcefg".toSet() to "0",
    "cf".toSet() to "1",
    "acdeg".toSet() to "2",
    "acdfg".toSet() to "3",
    "bcdf".toSet() to "4",
    "abdfg".toSet() to "5",
    "abdefg".toSet() to "6",
    "acf".toSet() to "7",
    "abcdefg".toSet() to "8",
    "abcdfg".toSet() to "9",
)

fun guess(patterns: List<Set<Char>>, inputs: List<Set<Char>>): String {
    val validWirings = possibleWirings.filter { wiring ->
        for (pattern in patterns) {
            val sourcePattern = pattern.map { wiring[it] }.toSet()
            if (sourcePattern !in digits)
                return@filter false
        }
        return@filter true
    }
    for (wiring in validWirings) {
        return inputs.joinToString("") { input ->
            val sourcePattern = input.map { wiring[it] }.toSet()
            digits[sourcePattern] ?: error("???")
        }
    }
    error("???")
}

fun main() {
    val decoded = File("input/day8.txt")
        .readLines()
        .map { line ->
            val (patterns, inputs) = line.split("|")
            guess(
                patterns.trim().split(" ").map { it.toSet() },
                inputs.trim().split(" ").map { it.toSet() }
            )
        }

    // part 1
    println(decoded.joinToString("").count { it in "1478" })
    // part 2
    println(decoded.sumOf { it.toInt() })
}