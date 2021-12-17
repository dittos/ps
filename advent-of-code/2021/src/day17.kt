fun part1(initialVelocity: Pair<Int, Int>, targetAreaX: ClosedRange<Int>, targetAreaY: ClosedRange<Int>): Int? {
    var vx = initialVelocity.first
    var vy = initialVelocity.second
    var x = 0
    var y = 0
    var maxY = 0

    while (true) {
//        println("x=$x y=$y")

        if (x in targetAreaX && y in targetAreaY) {
//            println("hit")
            return maxY
        }
        if (x > targetAreaX.endInclusive || y < targetAreaY.start) {
            return null
        }

        x += vx
        y += vy

        if (y > maxY)
            maxY = y

        if (vx > 0)
            vx -= 1
        else if (vx < 0)
            vx += 1
        vy -= 1
    }
}

fun main() {
    val targetAreaX = 241..273
    val targetAreaY = -97..-63

    var maxY = 0
    var count = 0
    for (vx in 1..1000) {
        for (vy in -1000..1000) {
            val y = part1(Pair(vx, vy), targetAreaX, targetAreaY)
            if (y != null) {
                println("vx=$vx vy=$vy -> $maxY")
                if (y > maxY) maxY = y
                count++
            }
        }
    }
    println(maxY)
    println(count)
}