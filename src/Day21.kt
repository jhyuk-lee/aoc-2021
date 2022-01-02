fun main() {
    fun parseInput(input: List<String>): Pair<Int, Int> {
        return input[0].split(": ")[1].toInt() to
                input[1].split(": ")[1].toInt()
    }

    abstract class Dice { abstract fun roll(): Int }

    class Dice1(var counter: Int = 1): Dice() {
        override fun roll(): Int {
            val r = this.counter
            this.counter = (this.counter % 100) + 1
            return r
        }
    }

    fun f(p1Init: Int, p2Init: Int, dice: Dice): Pair<Pair<Int, Int>, Pair<Int, Int>> {
        var (p1Idx, p2Idx) = p1Init to p2Init
        var (p1, p2) = (0 to 0) to (0 to 0)
        while (true) {
            p1Idx = (p1Idx + dice.roll() + dice.roll() + dice.roll() - 1) % 10 + 1
            p1 = p1.first + p1Idx to p1.second + 3
            if (p1.first >= 1000) return p1 to p2
            p2Idx = (p2Idx + dice.roll() + dice.roll() + dice.roll() - 1) % 10 + 1
            p2 = p2.first + p2Idx to p2.second + 3
            if (p2.first >= 1000) return p1 to p2
        }
    }

    fun part1(input: List<String>): Int {
        val (p1Init, p2Init) = parseInput(input)
        val (p1, p2) = f(p1Init, p2Init, Dice1())
        return p2.first * (p1.second + p2.second)
    }

    fun part2(input: List<String>): Long {
        val (p1Init, p2Init) = parseInput(input)
        var r = mapOf(((p1Init to 0) to (p2Init to 0)) to 1L)
        val rolls = listOf(3 to 1, 4 to 3, 5 to 6, 6 to 7, 7 to 6, 8 to 3, 9 to 1)
        var (p1Count, p2Count) = 0L to 0L

        while (r.isNotEmpty()) {
            val newR = mutableMapOf<Pair<Pair<Int, Int>, Pair<Int, Int>>, Long>()
            for ((k, v) in r) {
                val (p1, p2) = k
                for (roll1 in rolls) {
                    val v1 = v * roll1.second
                    val p1Idx = (p1.first + roll1.first - 1) % 10 + 1
                    val p1Score = p1.second + p1Idx
                    if (p1Score >= 21) {
                        p1Count += v1
                        continue
                    }
                    for (roll2 in rolls) {
                        val v2 = v * roll1.second * roll2.second
                        val p2Idx = (p2.first + roll2.first - 1) % 10 + 1
                        val p2Score = p2.second + p2Idx
                        if (p2Score >= 21) {
                            p2Count += v2
                            continue
                        }
                        val next = (p1Idx to p1Score) to (p2Idx to p2Score)
                        newR[next] = newR.getOrDefault(next, 0L) + v2
                    }
                }
            }
            r = newR
        }
        return maxOf(p1Count, p2Count)
    }

    val input: List<String> = readInput("Day21")
    println(part1(input))
    println(part2(input))
}
