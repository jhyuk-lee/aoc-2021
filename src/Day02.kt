fun main() {
    fun convertInputLine(inputLine: String): Pair<String, Int> {
        val inputSpl = inputLine.split(" ")
        return inputSpl[0] to inputSpl[1].toInt()
    }

    fun addCoords(p1: Pair<Int, Int>, p2: Pair<Int, Int>): Pair<Int, Int> {
        return p1.first+p2.first to p1.second+p2.second
    }
    tailrec fun navigate(input: List<Pair<String, Int>>): Pair<Int, Int> {
        val (command, amount) = input.first()
        val p = when (command) {
            "forward" -> amount to 0
            "down" -> 0 to amount
            "up" -> 0 to -amount
            else -> throw Exception("Wrong Input")
        }
        if (input.size == 1) return p
        return addCoords(p, navigate(input.drop(1)))
    }
    fun part1(input: List<String>): Int {
        val (x, y) = navigate(input.map { convertInputLine(it) })
        return x * y
    }

    tailrec fun navigate2(input: List<Pair<String, Int>>, aim: Int): Pair<Int, Int> {
        val (command, amount) = input.first()
        val (p, nextAim) = when (command) {
            "forward" -> amount to amount*aim to aim
            "down" -> 0 to 0 to aim+amount
            "up" -> 0 to 0 to aim-amount
            else -> throw Exception("Wrong Input")
        }
        if (input.size == 1) return p
        return addCoords(p, navigate2(input.drop(1), nextAim))
    }
    fun part2(input: List<String>): Int {
        val (x, y) = navigate2(input.map { convertInputLine(it) }, 0)
        return x * y
    }

    val input: List<String> = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
