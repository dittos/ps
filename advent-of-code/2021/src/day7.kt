import java.io.File
import kotlin.math.abs

fun alignCost(crabPositions: List<Int>, alignPosition: Int): Int {
    return crabPositions.sumOf { abs(it - alignPosition) }
}

fun alignCost2(crabPositions: List<Int>, alignPosition: Int): Int {
    return crabPositions.sumOf {
        val steps = abs(it - alignPosition)
        // 1 + 2 + ... + steps
        steps * (steps + 1) / 2
    }
}

fun main() {
    val crabPositions = File("input/day7.txt").readText().trim().split(",").map { it.toInt() }
    val minPosition = crabPositions.minOrNull()!!
    val maxPosition = crabPositions.maxOrNull()!!

    // part 1
    println((minPosition..maxPosition).minOf { alignCost(crabPositions, it) })
    // part 2
    println((minPosition..maxPosition).minOf { alignCost2(crabPositions, it) })
}