fun main() {
    tailrec fun hToB(h: String): String {
        if (h.isEmpty()) return ""
        return Integer.toBinaryString(Integer.parseInt(h.take(1), 16)).padStart(4, '0') + hToB(h.drop(1))
    }

    tailrec fun binToLong(b: String): Long {
        if (b.isEmpty()) return 0
        return binToLong(b.dropLast(1)) * 2 + (b.last().code - '0'.code)
    }

    fun type4(b: String): Pair<Long, Int> {
        fun search(b: String): Pair<String, Int> {
            if (b.isEmpty()) return "" to 0
            return when (b[0]) {
                '0' -> b.substring(1, 5) to 5
                else -> {
                    val (bin, idx) = search(b.substring(5))
                    (b.substring(1, 5) + bin) to (idx + 5)
                }
            }
        }
        val (bin, idx) = search(b)
        return binToLong(bin) to idx
    }

    fun f1(b: String, n: Int?): Pair<List<Map<String, Any>>, String> {
        if (n == 0) return emptyList<Map<String, Any>>() to b
        if (b.length < 7) return emptyList<Map<String, Any>>() to ""
        val (v, t) = binToLong(b.substring(0, 3)).toInt() to binToLong(b.substring(3, 6)).toInt()
        val nNext = if (n == null) { null } else { n - 1 }
        return when (t) {
            4 -> {
                val (d, idx) = type4(b.substring(6))
                val (res, remaining) = f1(b.substring(6 + idx), nNext)
                listOf(
                    mapOf("v" to v, "t" to t, "d" to d)
                ) + res to remaining
            }
            else -> {
                val i = binToLong(b.substring(6, 7)).toInt()
                when (i) {
                    0 -> {
                        val l = binToLong(b.substring(7, 22)).toInt()
                        val (child, _) = f1(b.substring(22, 22 + l), null)
                        val (res, remaining) = f1(b.substring(22 + l), nNext)
                        listOf(
                            mapOf("v" to v, "t" to t, "i" to i, "l" to l, "child" to child)
                        ) + res to remaining
                    }
                    else -> {
                        val l = binToLong(b.substring(7, 18)).toInt()
                        val (res, remaining) = f1(b.substring(18), l)
                        val (resAfter, remainingAfter) = f1(remaining, nNext)
                        listOf(
                            mapOf("v" to v, "t" to t, "i" to i, "l" to l, "child" to res)
                        ) + resAfter to remainingAfter
                    }
                }
            }
        }
    }

    fun sumVersion(res: List<Map<String, Any>>): Int {
        if (res.isEmpty()) return 0
        val firstRes = res.first()
        val (t, v) = firstRes["t"] to firstRes["v"]
        val child = firstRes.getOrDefault("child", listOf<Map<String, Any>>())
        return v as Int + sumVersion(child as List<Map<String, Any>>) + sumVersion(res.drop(1))
    }

    fun part1(input: List<String>): Int {
        val b = hToB(input.first())
        val (res, remaining) = f1(b, null)
        return sumVersion(res)
    }

    fun eval(res: Map<String, Any>): Long {
        val child = res.getOrDefault("child", emptyList<Map<String, Any>>()) as List<Map<String, Any>>
        return when(res["t"] as Int) {
            0 -> child.sumOf { eval(it) }
            1 -> child.map { eval(it) }.reduce { acc, e -> acc * e }
            2 -> child.minOf { eval(it) }
            3 -> child.maxOf { eval(it) }
            5 -> {
                val childEval = child.map { eval(it) }
                if (childEval[0]!! > childEval[1]) 1
                else 0
            }
            6 -> {
                val childEval = child.map { eval(it) }
                if (childEval[0]!! < childEval[1]) 1
                else 0
            }
            7 -> {
                val childEval = child.map { eval(it) }
                if (childEval[0]!! == childEval[1]) 1
                else 0
            }
            else -> res["d"]!! as Long
        }
    }

    fun part2(input: List<String>): Long {
        val b = hToB(input.first())
        val (res, remaining) = f1(b, null)
        check(res.size == 1)
        return eval(res.first())
    }

    val input: List<String> = readInput("Day16")
    println(part1(input))
    println(part2(input))
}
