import java.io.File

fun main() {
    val graph = mutableMapOf<String, MutableMap<String, Boolean>>()
    File("input/day12.txt").readLines()
        .map { line ->
            val (a, b) = line.split("-")
            Pair(a, b)
        }
        .forEach { (a, b) ->
            if (a !in graph) graph[a] = mutableMapOf()
            graph[a]!![b] = true
            if (b !in graph) graph[b] = mutableMapOf()
            graph[b]!![a] = true
        }

    fun isSmall(node: String) =
        !Character.isUpperCase(node[0])

    val paths = mutableSetOf<List<String>>()
    fun visit(node: String, path: List<String> = emptyList()) {
        if (node == "end") {
            paths += path
            return
        }
        if (isSmall(node) && node in path) {
            return
        }
        val p = path + node
        for (n in graph[node]!!.keys) {
            visit(n, p)
        }
    }
    visit("start")
    println(paths.size)
    paths.clear()

    fun visit2(node: String, path: List<String> = emptyList()) {
        if (node == "end") {
            paths += path
            return
        }
        if (isSmall(node)) {
            if (node in path) {
                if (path.filter { isSmall(it) }.groupBy { it }.any { it.value.size == 2 }) {
                    return
                }
            }
        }
        if (node == "start" && node in path) {
            return
        }
        val p = path + node
        for (n in graph[node]!!.keys) {
            visit2(n, p)
        }
    }
    visit2("start")
    println(paths.size)
}