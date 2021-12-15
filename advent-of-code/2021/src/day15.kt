import java.io.File
import java.util.*

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    val grid = File("input/day15.txt").readLines()
        .map { line -> line.map { it.digitToInt() } }
    dijk(grid)
    dijk(powerUpGrid(grid))
}

//fun solve(grid: List<List<Int>>) {
//    val minCost = Array(grid.size) { Array(grid[0].size) { 0 } }
//    for (i in grid.indices) {
//        for (j in grid[i].indices) {
//            if (i == 0 && j == 0) continue
//            val left = if (j == 0) Int.MAX_VALUE else minCost[i][j - 1]
//            val up = if (i == 0) Int.MAX_VALUE else minCost[i - 1][j]
//            minCost[i][j] = minOf(left, up) + grid[i][j]
//        }
//    }
//    println(minCost.last().last())
//}

val DIRECTIONS = listOf(Pair(-1, 0), Pair(0, -1), Pair(1, 0), Pair(0, 1))

fun dijk(grid: List<List<Int>>) {
    val height = grid.size
    val width = grid[0].size

    val dist = Array(height) { Array(width) { Int.MAX_VALUE } }
    val pq = PriorityQueue<Pair<Int, Int>>(compareBy { dist[it.first][it.second] })
    pq.add(Pair(0, 0))
    dist[0][0] = 0
    val target = Pair(height - 1, width - 1)

    while (pq.isNotEmpty()) {
        val u = pq.remove()
        if (u == target) break

        for (d in DIRECTIONS) {
            val i = u.first + d.first
            val j = u.second + d.second
            if (i >= 0 && j >= 0 && i < height && j < width) {
                val alt = dist[u.first][u.second] + grid[i][j]
                if (alt < dist[i][j]) {
                    dist[i][j] = alt
                    pq.add(Pair(i, j))
                }
            }
        }
    }

    println(dist[height - 1][width - 1])
}

fun powerUpGrid(base: List<List<Int>>): List<List<Int>> {
    fun powerUpCell(n: Int) = if (n + 1 > 9) 1 else n + 1
    val horizontalRepeated = base.map { row ->
        generateSequence(row) { prev ->
            prev.map(::powerUpCell)
        }.take(5).flatten().toList()
    }
    return generateSequence(horizontalRepeated) { prev ->
        prev.map { row ->
            row.map(::powerUpCell)
        }
    }.take(5).flatten().toList()
}
