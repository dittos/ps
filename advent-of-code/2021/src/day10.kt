import java.io.File

data class CorruptedException(val unexpected: Char, val expected: Char) : Exception()
data class IncompleteException(val remainingStack: List<Char>) : Exception()

fun parse(str: String) {
    val stack = mutableListOf<Char>()
    fun pop(c: Char) {
        val expected = stack.removeLast()
        if (c != expected) {
            throw CorruptedException(unexpected = c, expected = expected)
        }
    }
    for (c in str) {
        when (c) {
            '(' -> stack.add(')')
            '[' -> stack.add(']')
            '{' -> stack.add('}')
            '<' -> stack.add('>')
            ')', ']', '}', '>' -> pop(c)
        }
    }
    if (stack.isNotEmpty()) {
        throw IncompleteException(stack)
    }
}

fun main() {
    val lines = File("input/day10.txt").readLines()
    val results = lines.map { line -> runCatching { parse(line) } }

    val answer1 = results.sumOf { result ->
        when (val ex = result.exceptionOrNull()) {
            is CorruptedException ->
                when (ex.unexpected) {
                    ')' -> 3
                    ']' -> 57
                    '}' -> 1197
                    '>' -> 25137
                    else -> 0
                }
            else -> 0
        }.toInt()
    }
    println(answer1)

    val answer2 = results.mapNotNull { result ->
        when (val ex = result.exceptionOrNull()) {
            is IncompleteException ->
                ex.remainingStack.foldRight(0L) { c, acc ->
                    val score = when (c) {
                        ')' -> 1
                        ']' -> 2
                        '}' -> 3
                        '>' -> 4
                        else -> error("unexpected")
                    }
                    acc * 5L + score
                }
            else -> null
        }
    }.sorted().let {
        it[it.size / 2]
    }
    println(answer2)
}