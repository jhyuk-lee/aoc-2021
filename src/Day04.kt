fun main() {
    fun parseBoards(input: List<String>): List<List<Int>> {
        if (input.size < 5) return emptyList<List<Int>>()
        return parseBoards(input.drop(6)).plusElement(
            input.take(5).map { line -> line.split(" ").filter { it.isNotEmpty() }.map { it.toInt() } }.flatten()
        )
    }

    fun bingo(c: List<Int>): Boolean {
        for ( i in 0..4 ) {
            if (c.slice(i*5..i*5+4).sum() == 5) return true
            if (c.slice(i..i+20 step 5).sum() == 5) return true
        }
        return false
    }

    class Cell (val v: Int, var check: Int)
    fun runGame(calls: List<Int>, board: List<Int>): Pair<Int, Int> {
        val cells = board.map { Cell(v=it, check=0) }
        for ((idx, c) in calls.withIndex()) {
            cells.find{ it.v == c }?.check = 1
            if (bingo(cells.map { it.check })) {
                return idx to cells.filter { it.check == 0 }.sumOf { it.v } * c
            }
        }
        return calls.size to 0
    }

    fun part1(input: List<String>): Int {
        val calls: List<Int> = input[0].split(",").map { it.toInt() }
        val boards: List<List<Int>> = parseBoards(input.drop(2))
        val results: List<Pair<Int, Int>> = boards.map { runGame(calls, it) }.sortedBy { it.first }
        check(results[0].first < results[1].first)
        return results[0].second
    }

    fun part2(input: List<String>): Int {
        val calls: List<Int> = input[0].split(",").map { it.toInt() }
        val boards: List<List<Int>> = parseBoards(input.drop(2))
        val results: List<Pair<Int, Int>> = boards.map { runGame(calls, it) }.sortedBy { -it.first }
        check(results[0].first > results[1].first)
        return results[0].second
    }

    val input: List<String> = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
