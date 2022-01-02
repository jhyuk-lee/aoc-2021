fun main() {
    fun parseInput(input: List<String>): Array<IntArray> {
        return input.map { line -> line.toCharArray().map { it.code - '0'.code }.toIntArray() }.toTypedArray()
    }

    fun part1(input: List<String>): Int {
        val numSteps = 100
        val board = parseInput(input)
        val (r, c) = board.size to board[0].size
        return (1..numSteps).sumOf {
            val flash = mutableSetOf<Pair<Int, Int>>()
            val flashed = mutableSetOf<Pair<Int, Int>>()
            for (x in 0 until r) {
                for (y in 0 until c) {
                    board[x][y] += 1
                    if (board[x][y] == 10) {
                        board[x][y] = 0
                        flash.add(x to y)
                    }
                }
            }
            while (flash.isNotEmpty()) {
                val (x, y) = flash.first()
                flash.remove(x to y)
                flashed.add(x to y)
                for (dx in -1..1) {
                    for (dy in -1..1) {
                        if (dx == 0 && dy == 0) continue
                        val (nx, ny) = x + dx to y + dy
                        if (nx < 0 || nx >= r || ny < 0 || ny >= c) continue
                        if (flash.contains(nx to ny) || flashed.contains(nx to ny)) continue
                        board[nx][ny] += 1
                        if (board[nx][ny] == 10) {
                            board[nx][ny] = 0
                            flash.add(nx to ny)
                        }
                    }
                }
            }
            flashed.size
        }
    }

    fun part2(input: List<String>): Int {
        val board = parseInput(input)
        val (r, c) = board.size to board[0].size
        var numRound = 0
        while (true) {
            numRound += 1
            val flash = mutableSetOf<Pair<Int, Int>>()
            val flashed = mutableSetOf<Pair<Int, Int>>()
            for (x in 0 until r) {
                for (y in 0 until c) {
                    board[x][y] += 1
                    if (board[x][y] == 10) {
                        board[x][y] = 0
                        flash.add(x to y)
                    }
                }
            }
            while (flash.isNotEmpty()) {
                val (x, y) = flash.first()
                flash.remove(x to y)
                flashed.add(x to y)
                for (dx in -1..1) {
                    for (dy in -1..1) {
                        if (dx == 0 && dy == 0) continue
                        val (nx, ny) = x + dx to y + dy
                        if (nx < 0 || nx >= r || ny < 0 || ny >= c) continue
                        if (flash.contains(nx to ny) || flashed.contains(nx to ny)) continue
                        board[nx][ny] += 1
                        if (board[nx][ny] == 10) {
                            board[nx][ny] = 0
                            flash.add(nx to ny)
                        }
                    }
                }
            }
            if (board.sumOf { row -> row.sum() } == 0) return numRound
        }
    }

    val input: List<String> = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
