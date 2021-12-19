fun main() {
    // Does tailrec help this?
    tailrec fun countIncrease(input: List<Int>): Int {
        if (input.size < 2) return 0
        val incr: Int = if (input[input.size - 2] < input[input.size - 1]) 1 else 0
        // Is dropLast really better than drop in this case?
        return countIncrease(input.dropLast(1)) + incr
    }
    fun part1(input: List<String>): Int {
        return countIncrease(input.map { it.toInt() })
    }

    tailrec fun countIncrease2(input: List<Int>): Int {
        if (input.size < 4) return 0
        val prev: Int = input.takeLast(4).dropLast(1).sum()
        val next: Int = input.takeLast(3).sum()
        val incr: Int = if (prev < next) 1 else 0
        return countIncrease2(input.dropLast(1)) + incr
    }
    fun part2(input: List<String>): Int {
        return countIncrease2(input.map { it.toInt() })
    }

    val input: List<String> = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
