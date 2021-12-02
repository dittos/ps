import java.io.File

fun part1() {
    val commands = File("input/day2.txt").readLines().map {
        val (direction, amountString) = it.split(" ")
        val amount = amountString.toInt()
        when (direction) {
            "forward" -> Pair(amount, 0)
            "down" -> Pair(0, +amount)
            "up" -> Pair(0, -amount)
            else -> Pair(0, 0)
        }
    }
    val finalPosition = commands.fold(Pair(0, 0)) { position, command ->
        Pair(position.first + command.first, position.second + command.second)
    }
    val answer1 = finalPosition.first * finalPosition.second
    println(answer1)
}

data class Command(val type: Type, val amount: Int) {
    enum class Type { FORWARD, DOWN, UP, NOTHING }
}

data class State2(val horizontal: Int, val depth: Int, val aim: Int)

fun part2() {
    val commands = File("input/day2.txt").readLines().map {
        val (direction, amountString) = it.split(" ")
        val amount = amountString.toInt()
        val type = when (direction) {
            "forward" -> Command.Type.FORWARD
            "down" -> Command.Type.DOWN
            "up" -> Command.Type.UP
            else -> Command.Type.NOTHING
        }
        Command(type, amount)
    }
    val finalState = commands.fold(State2(0, 0, 0)) { state, command ->
        when (command.type) {
            Command.Type.FORWARD -> state.copy(
                horizontal = state.horizontal + command.amount,
                depth = state.depth + state.aim * command.amount,
            )
            Command.Type.DOWN -> state.copy(aim = state.aim + command.amount)
            Command.Type.UP -> state.copy(aim = state.aim - command.amount)
            else -> state
        }
    }
    val answer = finalState.horizontal * finalState.depth
    println(answer)
}

fun main() {
    part1()
    part2()
}