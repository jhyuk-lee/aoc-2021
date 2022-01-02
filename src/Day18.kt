fun main() {
    fun findComma(s: String, idx: Int = 0, depth: Int = 0): Int {
        return when (s[idx]) {
            '[' -> findComma(s, idx + 1, depth + 1)
            ']' -> findComma(s, idx + 1, depth - 1)
            ',' -> {
                if (depth == 1) idx
                else findComma(s, idx + 1, depth)
            }
            else -> findComma(s, idx + 1, depth)
        }
    }

    fun isRegular(s: String): Boolean { return s[0] != '[' }

    fun numCount(s: String): Int { return s.count { it == ',' } + 1 }

    fun parseNum(s: String): Pair<Int, Int> {
        check(numCount(s) == 2)
        val spl = s.substring(1, s.length - 1).split(",")
        return spl[0].toInt() to spl[1].toInt()
    }

    fun parseLR(s: String): Pair<String, String> {
        val commaIdx = findComma(s)
        return s.substring(1, commaIdx) to s.substring(commaIdx + 1, s.length - 1)
    }

    fun findAndExplode(s: String, depth: Int = 0): Triple<String, Int, Pair<Int, Int>>? {
        if (isRegular(s)) return null
        if (depth == 4) return Triple("0", 0, parseNum(s))
        val (left, right) = parseLR(s)
        val leftRes = findAndExplode(left, depth + 1)
        if (leftRes != null) return Triple("[${leftRes.first},$right]", leftRes.second, leftRes.third)
        val rightRes = findAndExplode(right, depth + 1)
        if (rightRes != null) return Triple("[$left,${rightRes.first}]", numCount(left) + rightRes.second, rightRes.third)
        return null
    }

    fun addNum(s: String, idx: Int, v: Int): String {
        if (idx < 0) return s
        if (isRegular(s)) {
            if (idx > 0) return s
            return (s.toInt() + v).toString()
        }
        val (left, right) = parseLR(s)
        val leftNumCount = numCount(left)
        if (idx < leftNumCount) return "[${addNum(left, idx, v)},$right]"
        return "[$left,${addNum(right, idx - leftNumCount, v)}]"
    }

    fun findAndSplit(s: String): String? {
        if (isRegular(s)) {
            if (s.toInt() < 10) return null
            return "[${s.toInt()/2},${(s.toInt() + 1) / 2}]"
        }
        val (left, right) = parseLR(s)
        val leftRes = findAndSplit(left)
        if (leftRes != null) return "[$leftRes,$right]"
        val rightRes = findAndSplit(right)
        if (rightRes != null) return "[$left,$rightRes]"
        return null
    }

    fun addSnailfish(a: String, b: String): String {
        var s = "[$a,$b]"
        while (true) {
            val explodeRes = findAndExplode(s)
            if (explodeRes != null) {
                s = addNum(addNum(
                    explodeRes.first, explodeRes.second - 1, explodeRes.third.first),
                                      explodeRes.second + 1, explodeRes.third.second)
                continue
            }
            val splitRes = findAndSplit(s)
            if (splitRes != null) {
                s = splitRes
                continue
            }
            break
        }
        return s
    }

    fun magnitude(s: String): Long {
        if (isRegular(s)) return s.toLong()
        val (left, right) = parseLR(s)
        return magnitude(left) * 3 + magnitude(right) * 2
    }

    fun part1(input: List<String>): Long {
        val res = input.reduce { acc, it -> addSnailfish(acc, it) }
        return magnitude(res)
    }

    fun part2(input: List<String>): Long {
        return (input.indices).map { i ->
            (input.indices).mapNotNull { j -> if (i == j) { null } else { i to j } } }.flatten()
            .maxOf { (i, j) -> magnitude(addSnailfish(input[i], input[j])) }
    }

    val input: List<String> = readInput("Day18")
    println(part1(input))
    println(part2(input))
}
