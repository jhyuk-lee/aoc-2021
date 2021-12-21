fun main() {
    fun parseInput(input: List<String>): List<List<Int>> {
        return input.map { line -> line.map { it.code - '0'.code } }
    }

    fun isLow(m: List<List<Int>>, x: Int, y: Int): Boolean {
        val (lx, ly) = m.size to m[0].size
        val ds = listOf(0 to 1, 1 to 0, -1 to 0, 0 to -1)
        for (d in ds) {
            val (nx, ny) = x + d.first to y + d.second
            if (nx < 0 || nx >= lx) continue
            if (ny < 0 || ny >= ly) continue
            if (m[nx][ny] <= m[x][y]) return false
        }
        return true
    }

    fun part1(input: List<String>): Int {
        val m = parseInput(input)
        val (lx, ly) = m.size to m[0].size
        return (0 until lx).map { x -> (0 until ly).map { y -> x to y } }.flatten().sumOf {
            (x, y) -> if (isLow(m, x, y)) { m[x][y] + 1 } else { 0 }
        }
    }

    fun part2(input: List<String>): Int {
        val m: List<List<Int>> = parseInput(input)
        val (lx: Int, ly: Int) = m.size to m[0].size
        val ds = listOf(0 to 1, 1 to 0, -1 to 0, 0 to -1)
        val coordCheck: MutableSet<Pair<Int, Int>> = mutableSetOf()
        var res: MutableList<Int> = mutableListOf()
        for (x: Int in 0 until lx) {
            for (y: Int in 0 until ly) {
                if (m[x][y] == 9) continue
                if (coordCheck.contains(x to y)) continue
                var count = 1
                coordCheck.add(x to y)
                val q: MutableList<Pair<Int, Int>> = mutableListOf(x to y)
                while (q.isNotEmpty()) {
                    val (qx, qy) = q.removeFirst()
                    for (d in ds) {
                        val (nx, ny) = qx + d.first to qy + d.second
                        if (nx < 0 || nx >= lx) continue
                        if (ny < 0 || ny >= ly) continue
                        if (m[nx][ny] == 9) continue
                        if (coordCheck.contains(nx to ny)) continue
                        count += 1
                        q.add(nx to ny)
                        coordCheck.add(nx to ny)
                    }
                }
                res.add(count)
            }
        }
        val finalRes = res.sorted().asReversed()
        return finalRes[0] * finalRes[1] * finalRes[2]
    }

    val input: List<String> = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
