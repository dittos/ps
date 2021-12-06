import java.io.File

fun next(prev: Map<Int, Long>): Map<Int, Long> {
    val result = mutableMapOf<Int, Long>()
    for ((timer, count) in prev) {
        if (timer == 0) {
            result[6] = (result[6] ?: 0) + count
            result[8] = (result[8] ?: 0) + count
        } else {
            result[timer - 1] = (result[timer - 1] ?: 0) + count
        }
    }
    return result
}

fun main() {
    val fishCountByTimer = File("input/day6.txt")
        .readText()
        .trim()
        .split(",")
        .map { it.toInt() }
        .groupBy { it }
        .mapValues { it.value.size.toLong() }
    val seq = generateSequence(fishCountByTimer, ::next)
    println(seq.elementAt(80).values.sum())
    println(seq.elementAt(256).values.sum())
}

// naive
//fun main() {
//    val fishes = File("input/day6.txt")
//        .readText()
//        .trim()
//        .split(",")
//        .map { it.toInt() }
//        .toMutableList()
//    repeat(80) {
//        repeat(fishes.size) { i ->
//            if (fishes[i] == 0) {
//                fishes[i] = 7
//                fishes.add(9 - 1)
//            }
//            fishes[i]--
//        }
//        println(fishes.size)
//    }
//}