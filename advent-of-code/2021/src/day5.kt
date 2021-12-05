import java.io.File
import kotlin.math.abs

data class Point(val x: Int, val y: Int)
data class Line(val start: Point, val end: Point) {
    fun isHorizontal() = start.y == end.y
    fun isVertical() = start.x == end.x
    fun isDiagonal() = abs(start.x - end.x) == abs(start.y - end.y)

    operator fun contains(other: Point): Boolean {
        return when {
            isHorizontal() -> start.y == other.y && other.x in safeRange(start.x, end.x)
            isVertical() -> start.x == other.x && other.y in safeRange(start.y, end.y)
            isDiagonal() -> {
                if (other.x in safeRange(start.x, end.x) && other.y in safeRange(start.y, end.y)) {
                    abs(start.x - other.x) == abs(start.y - other.y)
                } else {
                    false
                }
            }
            else -> error("not implemented")
        }
    }

    fun points(): Sequence<Point> {
        return when {
            isHorizontal() -> safeRange(start.x, end.x).asSequence().map { x -> Point(x, start.y) }
            isVertical() -> safeRange(start.y, end.y).asSequence().map { y -> Point(start.x, y) }
            isDiagonal() -> {
                val deltaX = if (start.x < end.x) +1 else -1
                val deltaY = if (start.y < end.y) +1 else -1
                sequence {
                    var x = start.x
                    var y = start.y
                    while (true) {
                        yield(Point(x, y))
                        x += deltaX
                        y += deltaY
                        if (end.x == x && end.y == y) {
                            yield(Point(x, y))
                            break
                        }
                    }
                }
            }
            else -> error("not implemented")
        }
    }
}
fun safeRange(a: Int, b: Int) =
    if (a < b)
        a..b
    else
        b..a

fun parseLine(s: String): Line {
    val (start, end) = s.split("->").map { parsePoint(it) }
    return Line(start, end)
}
fun parsePoint(s: String): Point {
    val (x, y) = s.split(",").map { it.trim().toInt() }
    return Point(x, y)
}

// slow version
//fun countOverlappingPoints(lines: List<Line>): Int {
//    val minX = lines.minOf { minOf(it.start.x, it.end.x) }
//    val minY = lines.minOf { minOf(it.start.y, it.end.y) }
//    val maxX = lines.maxOf { maxOf(it.start.x, it.end.x) }
//    val maxY = lines.maxOf { maxOf(it.start.y, it.end.y) }
//    return (minX..maxX).toList().parallelStream().mapToInt { x ->
//        var answer = 0
//        for (y in minY..maxY) {
//            val point = Point(x, y)
//            var count = 0
//            for (line in lines) {
//                if (point in line) {
//                    count++
//                    if (count >= 2) {
//                        answer++
//                        break
//                    }
//                }
//            }
//        }
//        answer
//    }.sum()
//}
fun countOverlappingPoints(lines: List<Line>): Int {
    val minX = lines.minOf { minOf(it.start.x, it.end.x) }
    val minY = lines.minOf { minOf(it.start.y, it.end.y) }
    val maxX = lines.maxOf { maxOf(it.start.x, it.end.x) }
    val maxY = lines.maxOf { maxOf(it.start.y, it.end.y) }
    val grid = Array(maxY - minY + 1) {
        IntArray(maxX - minX + 1) { 0 }
    }
    for (line in lines) {
        for (point in line.points()) {
            grid[point.y - minY][point.x - minX]++
        }
    }
    return grid.sumOf { row -> row.count { it >= 2 } }
}

fun main() {
    val lines = File("input/day5.txt").readLines().map {
        parseLine(it)
    }

    // part 1
    val candidateLines = lines.filter { it.isHorizontal() || it.isVertical() }
    println(countOverlappingPoints(candidateLines))

    // part 2
    val candidateLines2 = lines.filter { it.isHorizontal() || it.isVertical() || it.isDiagonal() }
    println(countOverlappingPoints(candidateLines2))
}