import java.io.File

fun main() {
    File("input/day14.txt").useLines { lines ->
        val iter = lines.iterator()
        val template = iter.next()
        val rules = mutableMapOf<String, String>()
        for (line in iter) {
            if (line.isEmpty()) continue
            val (pair, insert) = line.split("->").map { it.trim() }
            rules[pair] = insert
        }

        part1(template, rules)

        part2(template, rules)
    }
}

fun part1(template: String, rules: Map<String, String>) {
    fun next(prev: String, rules: Map<String, String>): String {
        return buildString {
            for (i in 0..prev.length - 2) {
                val pair = "${prev[i]}${prev[i + 1]}"
                val insert = rules[pair]
                if (insert == null) {
                    append(pair)
                } else {
                    append(prev[i])
                    append(insert)
                }
            }
            append(prev.last())
        }
    }
    val seq = generateSequence(template) { next(it, rules) }
    val after10 = seq.elementAt(10)
    val freq = after10.groupBy { it }.mapValues { it.value.size }
    println(freq.maxOf { it.value } - freq.minOf { it.value })
}

fun part2(template: String, rules: Map<String, String>) {
    // AB
    //  BC
    //   CA
    //    AB
    // => {AB=2, BC=1, CA=1}

    // AB -> X

    // AX
    //  XB
    //   BC
    //    CA
    //     AX
    //      XB
    // => {AX=2, XB=2, BC=1, CA=1}

    fun next(freq: Map<String, Long>): Map<String, Long> {
        val newFreq = Counter<String>()
        for ((pair, count) in freq) {
            val insert = rules[pair]
            if (insert != null) {
                newFreq.increment(pair[0] + insert, count)
                newFreq.increment(insert + pair[1], count)
            } else {
                newFreq.increment(pair, count)
            }
        }
        return newFreq.asMap()
    }

    val x = template.windowed(2).groupBy { it }.mapValues { it.value.size.toLong() }
    val seq = generateSequence(x, ::next)
    val pairFreq = seq.elementAt(40)
    val freq = Counter<Char>()
    for ((pair, count) in pairFreq) {
        freq.increment(pair[0], count)
        freq.increment(pair[1], count)
    }
    freq.increment(template.first(), 1)
    freq.increment(template.last(), 1)
    println(freq.maxOf { it.second } / 2 - freq.minOf { it.second } / 2)
}

class Counter<K> : Iterable<Pair<K, Long>> {
    private val data = mutableMapOf<K, Long>()

    fun increment(key: K, count: Long) {
        data.compute(key) { _, v -> (v ?: 0L) + count }
    }

    operator fun get(key: K): Long {
        return data[key] ?: 0L
    }

    override operator fun iterator(): Iterator<Pair<K, Long>> {
        return iterator {
            for ((key, count) in data)
                yield(key to count)
        }
    }

    fun asMap(): Map<K, Long> = data
}