import kotlin.math.abs

fun main() {
    fun parseInput(input: List<String>): List<Long> {
        check(input.size == 1)
        return input.first().split(",").map { it.toLong() }
    }

    fun part1(input: List<String>): Long {
        val crabs = parseInput(input)
        val median = crabs.sorted()[crabs.size / 2]
        return crabs.sumOf { abs(it - median) }
    }

    fun costFunc(d: Long): Long {
        return d * (d + 1) / 2
    }

    fun part2(input: List<String>): Long {
        val crabs = parseInput(input)
        return (crabs.minOrNull()!!..crabs.maxOrNull()!!).map {
            p -> crabs.sumOf {
                costFunc(abs(it - p))
            }
        }.minOrNull()!!
    }

    val input: List<String> = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
