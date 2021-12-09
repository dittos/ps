import java.io.File

fun main() {
    val input = File("input/day9.txt").readLines()
    val heightmap = input.map { line -> line.map { it.toString().toInt() } }
    val width = heightmap[0].size
    val height = heightmap.size
    var answer1 = 0
    val basinSizes = mutableListOf<Int>()
    for (y in (0 until height)) {
        for (x in (0 until width)) {
            val h = heightmap[y][x]
            val left = runCatching { heightmap[y][x - 1] }
            val right = runCatching { heightmap[y][x + 1] }
            val up = runCatching { heightmap[y - 1][x] }
            val down = runCatching { heightmap[y + 1][x] }
            val isLowPoint = listOf(left, right, up, down)
                .mapNotNull { it.getOrNull() }
                .all { it > h }
            if (isLowPoint) {
                val riskLevel = 1 + h
                answer1 += riskLevel
                basinSizes += basinSize(heightmap, x, y)
            }
        }
    }
    println(answer1)

    val (a, b, c) = basinSizes.sortedDescending().take(3)
    println(a * b * c)
}

fun basinSize(heightmap: List<List<Int>>, lowPointX: Int, lowPointY: Int): Int {
    val width = heightmap[0].size
    val height = heightmap.size
    val visited = List(height) {
        MutableList(width) { false }
    }
    var size = 0
    fun visit(x: Int, y: Int) {
        if (heightmap[y][x] == 9) return
        if (visited[y][x]) return
        visited[y][x] = true
        size++
        if (x >= 1) visit(x - 1, y)
        if (x < width - 1) visit(x + 1, y)
        if (y >= 1) visit(x, y - 1)
        if (y < height - 1) visit(x, y + 1)
    }
    visit(lowPointX, lowPointY)
    return size
}
