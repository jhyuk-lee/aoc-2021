import kotlin.math.max
import kotlin.math.min

fun main() {
    fun parseInput(input: List<String>): List<Pair<List<Pair<Int, Int>>, Boolean>> {
        return input.map { line ->
            val spl = line.split(" ")
            val (on, coords) = spl[0]!! to spl[1]!!
            val c = coords.split(",").map { it.drop(2).split("..") }.map { it[0].toInt() to it[1].toInt() }
            c to (on == "on")
        }
    }

    fun clip(cuboids: List<Pair<List<Pair<Int, Int>>, Boolean>>, minX: Int, maxX: Int): List<Pair<List<Pair<Int, Int>>, Boolean>> {
        return cuboids.mapNotNull { c ->
            if (c.first.any { it.first > maxX || it.second < minX }) null
            else c.first.map { max(it.first, minX) to min(it.second, maxX) } to c.second
        }
    }

    fun f(cuboids: List<Pair<List<Pair<Int, Int>>, Boolean>>): Long {
        val segments = (0..2).map { d ->
            val xs = cuboids.asSequence().map { listOf(it.first[d].first, it.first[d].second + 1) }.flatten()
                .toSet().toList().sorted().toList()
            (0 until xs.size - 1).map { xs[it] to xs[it + 1] }
        }
        val rCuboids = cuboids.asReversed()
        var res = 0L
        for (xs in segments[0]) {
            for (ys in segments[1]) {
                val zCuboids = rCuboids.mapNotNull { c ->
                    if (c.first[0].first <= xs.first && xs.second <= c.first[0].second + 1
                        && c.first[1].first <= ys.first && ys.second <= c.first[1].second + 1) c.first[2] to c.second
                    else null
                }
                for (zs in segments[2]) {
                    var s = false
                    for (c in zCuboids) {
                        if (c.first.first <= zs.first && zs.second <= c.first.second + 1) {
                            s = c.second
                            break
                        }
                    }
                    if (s) {
                        res += (xs.second - xs.first).toLong() * (ys.second - ys.first) * (zs.second - zs.first)
                    }
                }
            }
        }
        return res
    }

    fun part1(input: List<String>): Long {
        val cuboids = clip(parseInput(input), -50, 50)
        return f(cuboids)
    }

    fun part2(input: List<String>): Long {
        val cuboids = parseInput(input)
        return f(cuboids)
    }

    val input: List<String> = readInput("Day22")
    println(part1(input))
    println(part2(input))
}
