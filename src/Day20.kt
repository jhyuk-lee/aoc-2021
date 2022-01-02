fun main() {
    fun f(board: Pair<List<String>, Char>, algo: String): Pair<List<String>, Char> {
        val (b, c) = board
        val padded = listOf(c.toString().repeat(b[0].length + 4), c.toString().repeat(b[0].length + 4)) +
                b.map { c.toString().repeat(2) + it + c.toString().repeat(2) } +
                listOf(c.toString().repeat(b[0].length + 4), c.toString().repeat(b[0].length + 4))
        return (1 until padded.size - 1).map { i ->
            (1 until padded[0].length - 1).map { j ->
                val bStr = (-1..1).map { di -> (-1..1).map { dj ->
                    if (padded[i+di][j+dj] == '.') '0' else '1' } }.flatten().joinToString("")
                algo[Integer.parseInt(bStr, 2)]
            }.joinToString("")
        } to algo[if (c == '.') 0 else 511]
    }

    fun printBoard(board: Pair<List<String>, Char>) {
        println(board.second)
        println(board.first.joinToString("\n"))
    }

    fun g(board: Pair<List<String>, Char>, algo: String, n: Int): Pair<List<String>, Char> {
//        printBoard(board)
        if (n == 0) return board
        return g(f(board, algo), algo, n - 1)
    }

    fun part1(input: List<String>): Int {
        val (algo, board) = input[0] to (input.drop(2) to '.')
        val res = g(board, algo, 2)
        return res.first.sumOf { row -> row.count { it == '#' } }
    }

    fun part2(input: List<String>): Int {
        val (algo, board) = input[0] to (input.drop(2) to '.')
        val res = g(board, algo, 50)
        return res.first.sumOf { row -> row.count { it == '#' } }
    }

    val input: List<String> = readInput("Day20")
    println(part1(input))
    println(part2(input))
}
