fun main() {
    fun parseInput(input: List<String>): Pair<List<Pair<Int, Int>>, List<Pair<Char, Int>>> {
        return Pair(
            input.takeWhile { it.isNotEmpty() }
                .map { it.split(",") }.map { it[0]!!.toInt() to it[1]!!.toInt() },
            input.takeLastWhile { it.isNotEmpty() }
                .map { it.drop(11).split("=") }.map { it[0]!![0] to it[1]!!.toInt() }
        )
    }

    fun doFold(coords: List<Pair<Int, Int>>, folds: List<Pair<Char, Int>>): List<Pair<Int, Int>> {
        if (folds.isEmpty()) return coords
        val (axis, n) = folds[0]
        return doFold(coords.map { (x, y) ->
            when(axis) {
                'x' -> if (x > n) { n - (x - n) to y } else if (x < n) { x to y } else { throw Exception() }
                'y' -> if (y > n) { x to n - (y - n) } else if (y < n) { x to y } else { throw Exception() }
                else -> throw Exception()
            }
        }.toSet().toList(), folds.drop(1))
    }

    fun part1(input: List<String>): Int {
        val (coords, folds) = parseInput(input)
        return doFold(coords, folds.take(1)).size
    }

    fun part2(input: List<String>): String {
        val (coords, folds) = parseInput(input)
        val finalCoords = doFold(coords, folds)
        val (r, c) = finalCoords.maxOf { it.second } to finalCoords.maxOf { it.first }
        return (0..r).map { y ->
            (0..c).map { x -> if (x to y in finalCoords) { '#' } else { '.' } }.joinToString("")
        }.joinToString("\n")
    }

    val input: List<String> = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
