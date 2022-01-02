import java.util.PriorityQueue

fun main() {
    fun parseInput(input: List<String>): Array<IntArray> {
        return input.map { line -> line.toCharArray().map { it.code - '0'.code }.toIntArray() }.toTypedArray()
    }

    fun searchPath(board: Array<IntArray>): Int {
        val (r, c) = board.size to board[0].size
        val visited = mutableSetOf<Pair<Int, Int>>()
        val q = PriorityQueue<Pair<Int, Pair<Int, Int>>>(Comparator<Pair<Int, Pair<Int, Int>>>{ a, b ->
            if (a.first == b.first) {
                if (a.second.first == b.second.first) b.second.second - a.second.second
                else b.second.first - a.second.first
            }
            else a.first - b.first
        })
        val ds = listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1)
        q.add(0 to (0 to 0))
        while (q.isNotEmpty()) {
            val (risk, p) = q.poll()
            if (visited.contains(p)) continue
            visited.add(p)
            val (x, y) = p
            if (x == r - 1 && y == c - 1) return risk
            for ((dx, dy) in ds) {
                val (nx, ny) = x + dx to y + dy
                if (nx < 0 || nx >= r || ny < 0 || ny >= c) continue
                if (visited.contains(nx to ny)) continue
                q.add(risk + board[nx][ny] to (nx to ny))
            }
        }
        throw Exception()
    }

    fun part1(input: List<String>): Int {
        val board = parseInput(input)
        return searchPath(board)
    }

    fun dup(board: Array<IntArray>, n: Int): Array<IntArray> {
        val (r, c) = board.size to board[0].size
        return (0 until r * n).map { x ->
            (0 until c*n).map { y ->
                ((board[x % r][y % c] - 1) + (x / r + y / c)) % 9 + 1
            }.toIntArray()
        }.toTypedArray()
    }

    fun part2(input: List<String>): Int {
        val board = parseInput(input)
        return searchPath(dup(board, 5))
    }

    val input: List<String> = readInput("Day15")
    println(part1(input))
    println(part2(input))
}
