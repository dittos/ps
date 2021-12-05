import java.io.File

data class Board(
    val rows: List<List<Cell>>
) {
    data class Cell(
        val number: Int,
        var marked: Boolean = false,
    )

    fun mark(number: Int) {
        for (row in rows) {
            for (cell in row) {
                if (cell.number == number) {
                    cell.marked = true
                }
            }
        }
    }

    fun reset() {
        for (row in rows) {
            for (cell in row) {
                cell.marked = false
            }
        }
    }

    fun isWin(): Boolean {
        if (rows.any { row -> row.all { it.marked } })
            return true

        if (rows[0].mapIndexed { col, _ -> rows.all { it[col].marked } }.any { it })
            return true

        return false
    }

    fun score() =
        rows.sumOf { row ->
            row.filter { !it.marked }.sumOf { it.number }
        }
}

fun part1(numbers: List<Int>, boards: List<Board>): Int {
    for (number in numbers) {
        for (board in boards) {
            board.mark(number)
            if (board.isWin()) {
                println(board)
                return board.score() * number
            }
        }
    }
    return 0
}

fun part2(numbers: List<Int>, boards: List<Board>): Int {
    var lastWinBoard: Board? = null
    var lastWinNumber: Int? = null
    for (number in numbers) {
        for (board in boards) {
            if (board.isWin()) {
                continue
            }
            board.mark(number)
            if (board.isWin()) {
                lastWinBoard = board
                lastWinNumber = number
            }
        }
    }
    return lastWinBoard!!.score() * lastWinNumber!!
}

fun main() {
    val data = File("input/day4.txt").readText().replace("\r\n", "\n")
    val sections = data.split("\n\n")
    val numbers = sections[0].split(",").map { it.toInt() }
    val boards = sections.drop(1).map { section ->
        section.lines().filter { it.isNotBlank() }.map { row ->
            row.trim().split("\\s+".toRegex()).map { Board.Cell(it.toInt()) }
        }.let {
            Board(it)
        }
    }

    println(part1(numbers, boards))
    boards.forEach { it.reset() }
    println(part2(numbers, boards))
}