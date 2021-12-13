import java.io.File

fun main() {
    val points = mutableListOf<Pair<Int, Int>>()
    val folds = mutableListOf<Pair<String, Int>>()
    for (line in File("input/day13.txt").readLines()) {
        if (line.isEmpty()) continue
        if ("," in line) {
            val (x, y) = line.split(",").map { it.toInt() }
            points.add(Pair(x, y))
        } else {
            val (header, n) = line.split("=")
            val axis = header.takeLast(1)
            folds.add(Pair(axis, n.toInt()))
        }
    }

    println(applyFold(points, folds.first()).size)
    drawPoints(folds.fold(points, ::applyFold))
}

fun applyFold(points: List<Pair<Int, Int>>, fold: Pair<String, Int>): List<Pair<Int, Int>> {
    val (axis, n) = fold
    return when (axis) {
        "x" -> {
            points.map {
                val (x, y) = it
                if (x >= n)
                    Pair(n - (x - n), y)
                else
                    it
            }.distinct()
        }
        "y" -> {
            points.map {
                val (x, y) = it
                if (y >= n)
                    Pair(x, n - (y - n))
                else
                    it
            }.distinct()
        }
        else -> points
    }
}

fun drawPoints(points: List<Pair<Int, Int>>) {
    val maxX = points.maxOf { (x, y) -> x }
    val maxY = points.maxOf { (x, y) -> y }
    val rows = Array<Array<String>>(maxY + 1) { Array(maxX + 1) { "   " } }
    for ((x, y) in points) {
        rows[y][x] = "###"
    }
    for (row in rows) {
        println(row.joinToString(""))
    }
}