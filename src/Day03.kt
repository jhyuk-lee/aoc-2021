enum class LSR { O2, CO2 }

fun main() {
    fun convertBinToIndexSet(item: String): Set<Int> {
        return item.reversed().withIndex()
            .filter { it.value == '1' }
            .map { it.index }
            .toSet()
    }

    fun agg(m: Map<Int, Int>, s: Set<Int>): Map<Int, Int> {
        return (m + s.filter { !m.contains(it) }.associateWith { 0 }).mapValues {
            if (s.contains(it.key)) it.value + 1 else it.value
        }
    }

    fun part1(input: List<String>): Int {
        var n: Int = input.size
        var ones: Map<Int, Int> = input.fold(emptyMap<Int, Int>()) {
            acc, item -> agg(acc, convertBinToIndexSet(item))
        }
        var l = ones.maxByOrNull { it.key }!!.key
        var (gamma, epsilon) = 0 to 0
        for (i in 0..l) {
            var oneCount = ones.getOrDefault(i, 0)
            if (oneCount >= n - oneCount) gamma += 1 shl i
            else epsilon += 1 shl i
        }
        println("gamma: ${gamma}, epsilon: ${epsilon}")
        return gamma * epsilon
    }

    fun findVal(input: List<String>, index: Int, lsr: LSR): String {
        if (input.size == 1) {
            return input[0]
        }
        val oneCount = input.count{ it[index] == '1' }
        val (most, least) = if (oneCount >= input.size - oneCount) '1' to '0' else '0' to '1'
        var filterBy = when (lsr) {
            LSR.O2 -> most
            LSR.CO2 -> least
        }
        return findVal(input.filter { it[index] == filterBy }, index + 1, lsr)
    }

    fun binStrToInt(s: String): Int {
        return s.reversed().foldIndexed(0) {
            index, acc, c -> acc + (if (c == '1') 1 shl index else 0)
        }
    }

    fun part2(input: List<String>): Int {
        val v1 = binStrToInt(findVal(input, 0, LSR.O2))
        val v2 = binStrToInt(findVal(input, 0, LSR.CO2))
        println("O2: ${v1}, CO2: ${v2}")
        return v1*v2
    }

    val input: List<String> = readInput("Day03")
    input.forEach {
        check(it.length == input[0].length)
    }
    println(part1(input))
    println(part2(input))
}
