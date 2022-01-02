fun main() {
    fun <T>listToPair(l: List<T>): Pair<T, T> { return l[0]!! to l[1]!! }
    fun parseInput(input: List<String>): Pair<Pair<Long, Long>, Pair<Long, Long>> {
        return listToPair(input.first().substring("target area: ".length).split(", ").map {
            listToPair(it.substring(2).split("..").map { v -> v.toLong() })
        })
    }

    fun calcResX(x: Long, s: Long): Long {
        if (x < s) return (x + 1) * x / 2
        return (x + (x - s + 1)) * s / 2
    }

    fun calcResY(y: Long, s: Long): Long {
        return (y + (y - s + 1)) * s / 2
    }

    fun calcMaxY(y: Long, s: Long): Long {
        if (y < s) return (y + 1) * y / 2
        return (y + (y - s + 1)) * s / 2
    }

    fun intersects(xRange: Pair<Long, Long?>, yRange: Pair<Long, Long>): Boolean {
        if (xRange.second == null) return yRange.second >= xRange.first
        return (xRange.first <= yRange.first && yRange.first <= xRange.second!!) ||
                (xRange.first <= yRange.second && yRange.second <= xRange.second!!) ||
                (yRange.first <= xRange.first && xRange.first <= yRange.second) ||
                (yRange.first <= xRange.second!! && xRange.second!! <= yRange.second)
    }

    fun f(xRange: Pair<Long, Long>, yRange: Pair<Long, Long>): Pair<Long, Long> {
        // Assumptions: 0 < x.first, x.second && y.first, y.second < 0
        val xStepRange = mutableMapOf<Long, Pair<Long, Long?>>()
        for (x in 1..xRange.second) {
            var minS: Long? = null
            var maxS: Long? = null
            for (s in 1L..x) {
                val res = calcResX(x, s)
                if (xRange.first <= res && res <= xRange.second) {
                    if (minS == null || s < minS!!) minS = s
                    if (maxS == null || s > maxS!!) maxS = s
                }
            }
            if (minS != null) {
                if (maxS == x) maxS = null
                xStepRange[x] = minS to maxS
            }
        }

        val yStepRange = mutableMapOf<Long, Pair<Long, Long>>()
        for (y in yRange.first..-yRange.first) {
            var minS: Long? = null
            var maxS: Long? = null
            for (s in 1L..2*(-yRange.first)+1) {
                val res = calcResY(y, s)
                if (yRange.first <= res && res <= yRange.second) {
                    if (minS == null || s < minS!!) minS = s
                    if (maxS == null || s > maxS!!) maxS = s
                }
            }
            if (minS != null) {
                yStepRange[y] = minS to maxS!!
            }
        }

        var count = 0L
        var maxY = 0L
        for ((x, xSteps) in xStepRange) {
            for ((y, ySteps) in yStepRange) {
                if (intersects(xSteps, ySteps)) {
                    count += 1
                    val maxYCandidate = calcMaxY(y, ySteps.second)
                    if (maxYCandidate > maxY) {
                        maxY = maxYCandidate
                    }
                }
            }
        }
        return maxY to count
    }

    fun part1(input: List<String>): Long {
        val (xRange, yRange) = parseInput(input)
        val (maxY, _) = f(xRange, yRange)
        return maxY
    }

    fun part2(input: List<String>): Long {
        val (xRange, yRange) = parseInput(input)
        val (_, count) = f(xRange, yRange)
        return count
    }

    val input: List<String> = readInput("Day17")
    println(part1(input))
    println(part2(input))
}
