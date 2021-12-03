import java.io.File

fun mergeBitPair(a: Map<Char, Int>, b: Map<Char, Int>): Map<Char, Int> {
    val merged = a.toMutableMap()
    b.forEach { (k, v) -> merged.compute(k) { _, v0 -> (v0 ?: 0) + v } }
    return merged
}

fun bitFreqPerPosition(lines: List<String>) =
    lines.map { line -> line.map { mapOf(it to 1) } }
        .reduce { a, b -> a.zip(b, ::mergeBitPair) }

fun gammaRate(lines: List<String>) =
    bitFreqPerPosition(lines)
        .mapNotNull { bit -> bit.maxByOrNull { it.value }?.key }
        .joinToString("")
        .toInt(2)

fun epsRate(lines: List<String>) =
    bitFreqPerPosition(lines)
        .mapNotNull { bit -> bit.minByOrNull { it.value }?.key }
        .joinToString("")
        .toInt(2)

fun part2(lines: List<String>, bitCriteria: (Map<Char, Int>) -> Char, pos: Int): List<String> =
    if (lines.size == 1) {
        lines
    } else {
        val keepBit = bitCriteria(bitFreqPerPosition(lines)[pos])
        val filtered = lines.filter { it[pos] == keepBit }
        part2(filtered, bitCriteria, pos + 1)
    }

fun part2(lines: List<String>, bitCriteria: (Map<Char, Int>) -> Char): Int =
    part2(lines, bitCriteria, 0).single().toInt(2)

fun o2Rating(lines: List<String>) =
    part2(lines) {
        if ((it['0'] ?: 0) <= (it['1'] ?: 0)) '1' else '0'
    }

fun co2Rating(lines: List<String>) =
    part2(lines) {
        if ((it['0'] ?: 0) <= (it['1'] ?: 0)) '0' else '1'
    }

fun main() {
    val inputLines = File("input/day3.txt").readLines()

    // part 1
    println(gammaRate(inputLines) * epsRate(inputLines))

    // part 2
    println(o2Rating(inputLines) * co2Rating(inputLines))
}
