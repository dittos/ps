import java.io.File

fun main() {
    val measurements = File("input/day1.txt").readLines().map { it.toInt() }

    // How many measurements are larger than the previous measurement?
    val answer1 = measurements.zipWithNext { a, b -> b > a }.count { it }
    println(answer1)

    // the number of times the sum of measurements in this sliding window increases from the previous sum
    val threeSlidingWindowSums = measurements.windowed(3).map { it.sum() }
    val answer2 = threeSlidingWindowSums.zipWithNext { a, b -> b > a }.count { it }
    println(answer2)
}