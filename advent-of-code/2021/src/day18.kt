import java.io.File
import java.util.*

sealed class S {
    data class Pair(val left: S, val right: S) : S() {
        override fun toString(): String {
            return "[$left,$right]"
        }
    }

    data class Number(val value: Int) : S() {
        override fun toString(): String {
            return value.toString()
        }
    }

    companion object {
        fun parse(str: String): S {
            return Parser(str).parse()
        }
    }
}

class Parser(str: String) {
    private val scanner = Scanner(str).useDelimiter("")

    fun parse(): S {
        return readPairOrNumber()
    }

    private fun readPairOrNumber(): S {
        return if (scanner.hasNext("\\[")) {
            readPair()
        } else {
            readNumber()
        }
    }

    private fun readPair(): S.Pair {
        scanner.next("\\[")
        val left = readPairOrNumber()
        scanner.next(",")
        val right = readPairOrNumber()
        scanner.next("]")
        return S.Pair(left, right)
    }

    private fun readNumber(): S.Number {
        return S.Number(scanner.nextInt())
    }
}

///

enum class PairPosition { LEFT, RIGHT }

sealed class Node(
    var parent: Node? = null,
    var positionInParent: PairPosition? = null,
) {
    class Pair(var left: Node, var right: Node) : Node() {
        init {
            left.parent = this
            left.positionInParent = PairPosition.LEFT
            right.parent = this
            right.positionInParent = PairPosition.RIGHT
        }
    }
    class Number(var value: Int) : Node()

    fun replaceSelf(newNode: Node) {
        newNode.parent = parent
        newNode.positionInParent = positionInParent
        val p = parent
        if (p != null && p is Pair) {
            when (positionInParent) {
                PairPosition.LEFT -> p.left = newNode
                PairPosition.RIGHT -> p.right = newNode
            }
        }
    }
}

fun toNode(root: S): Node {
    fun visit(s: S): Node {
        return when (s) {
            is S.Pair -> Node.Pair(visit(s.left), visit(s.right))
            is S.Number -> Node.Number(s.value)
        }
    }
    return visit(root)
}

fun fromNode(node: Node): S {
    return when (node) {
        is Node.Pair -> S.Pair(fromNode(node.left), fromNode(node.right))
        is Node.Number -> S.Number(node.value)
    }
}

///

fun collectNumbers(root: Node): List<Node.Number> {
    val numbers = mutableListOf<Node.Number>()
    fun visit(node: Node) {
        when (node) {
            is Node.Pair -> {
                visit(node.left)
                visit(node.right)
            }
            is Node.Number -> {
                numbers.add(node)
            }
        }
    }
    visit(root)
    return numbers
}

fun findExplodeNode(root: Node): Node.Pair? {
    fun visit(node: Node, depth: Int): Node.Pair? {
        if (node is Node.Pair) {
            if (depth == 4) {
                return node
            }
            return visit(node.left, depth + 1) ?: visit(node.right, depth + 1)
        }
        return null
    }
    return visit(root, 0)
}

fun explode(root: Node): Boolean {
    val node = findExplodeNode(root) ?: return false
    val numbers = collectNumbers(root)
    val left = node.left as Node.Number
    val leftIndex = numbers.indexOf(left)
    if (leftIndex > 0) {
        val leftLeft = numbers[leftIndex - 1]
        leftLeft.value += left.value
    }
    val right = node.right as Node.Number
    val rightIndex = numbers.indexOf(right)
    if (rightIndex < numbers.indices.last) {
        val rightRight = numbers[rightIndex + 1]
        rightRight.value += right.value
    }
    node.replaceSelf(Node.Number(0))
    return true
}

fun findSplitNode(node: Node): Node.Number? {
    return when (node) {
        is Node.Pair ->
            findSplitNode(node.left) ?: findSplitNode(node.right)
        is Node.Number ->
            if (node.value >= 10) {
                node
            } else {
                null
            }
    }
}

fun split(root: Node): Boolean {
    val node = findSplitNode(root) ?: return false
    val left = Node.Number(node.value / 2)
    val right = Node.Number(node.value / 2 + node.value % 2)
    node.replaceSelf(Node.Pair(left, right))
    return true
}

fun plus(left: Node, right: Node): Node {
    val node = Node.Pair(left, right)
    reduce(node)
    return node
}

fun reduce(root: Node) {
    while (true) {
        if (explode(root)) continue
        if (split(root)) continue
        break
    }
}

fun magnitude(s: S): Int {
    return when (s) {
        is S.Pair -> magnitude(s.left) * 3 + magnitude(s.right) * 2
        is S.Number -> s.value
    }
}

fun main() {
    val ss = File("input/day18.txt").readLines().map { S.parse(it) }

    // part 1
    println(magnitude(ss.map(::toNode).reduce(::plus).let(::fromNode)))

    // part 2
    println(ss.asSequence().flatMap { s1 ->
        ss.asSequence().map { s2 ->
            Pair(s1, s2)
        }
    }.flatMap { (s1, s2) ->
        sequenceOf(
            fromNode(plus(toNode(s1), toNode(s2))),
            fromNode(plus(toNode(s2), toNode(s1))),
        )
    }.maxOf(::magnitude))
}