import java.io.File

val directions = listOf(
    Pair(-1, -1), Pair(-1, 0), Pair(-1, 1),
    Pair(0, -1), Pair(0, 1),
    Pair(1, -1), Pair(1, 0), Pair(1, 1),
)
const val FLASHED = -1

fun flash(energy: List<MutableList<Int>>, i: Int, j: Int) {
    if (energy[i][j] > 9) {
        energy[i][j] = FLASHED
        for ((di, dj) in directions) {
            val i2 = i + di
            val j2 = j + dj
            if (i2 !in energy.indices || j2 !in energy[i2].indices)
                continue
            if (energy[i2][j2] != FLASHED) energy[i2][j2]++
            flash(energy, i2, j2)
        }
    }
}

fun step(energy: List<MutableList<Int>>): Int {
    // increase by 1
    for (i in energy.indices) {
        for (j in energy[i].indices) {
            energy[i][j]++
        }
    }

    // "flashes" - greater than 9
    for (i in energy.indices) {
        for (j in energy[i].indices) {
            flash(energy, i, j)
        }
    }

    // count & reset
    var flashCount = 0
    for (i in energy.indices) {
        for (j in energy[i].indices) {
            if (energy[i][j] == FLASHED) {
                flashCount++
                energy[i][j] = 0
            }
        }
    }
    return flashCount
}

fun main() {
    val energy = File("input/day11.txt").readLines()
        .map { line -> line.map { it.toString().toInt() }.toMutableList() }

    val seqCache = mutableListOf<Int>()
    val seq = sequence {
        yieldAll(seqCache)
        while (true) {
            val count = step(energy)
            seqCache += count
            yield(count)
        }
    }

    // part 1
    println(seq.take(100).sum())

    // part 2
    val size = energy.sumOf { it.size }
    println(seq.indexOfFirst { it == size } + 1)
}