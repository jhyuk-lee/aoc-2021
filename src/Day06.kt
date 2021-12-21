fun main() {
    fun parseInput(input: List<String>): List<Int> {
        check(input.size == 1)
        return input.first().split(",").map { it.toInt() }
    }

    fun part1(input: List<String>): Int {
        val n = 80
        val fishes: List<Int> = parseInput(input)
        var counts: Map<Int, Int> = (0..8).associateWith { f -> fishes.count { it == f } }
        for (i in 1..n) {
            counts = (0..8).associateWith { f: Int ->
                when (f) {
                    8 -> counts[0]!!
                    6 -> counts[7]!! + counts[0]!!
                    else -> counts[f + 1]!!
                }
            }
        }
        return counts.values.sum()
    }

    fun part2(input: List<String>): ULong {
        val n = 256
        val fishes: List<Int> = parseInput(input)
        var counts: Map<Int, ULong> = (0..8).associateWith { f -> fishes.count { it == f }.toULong() }
        for (i in 1..n) {
            counts = (0..8).associateWith { f: Int ->
                when (f) {
                    8 -> counts[0]!!
                    6 -> counts[7]!! + counts[0]!!
                    else -> counts[f + 1]!!
                }
            }
        }
        return counts.values.sum()
    }

    val input: List<String> = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
