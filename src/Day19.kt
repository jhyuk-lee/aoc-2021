fun main() {
    fun parseInput(input: List<String>): List<List<IntArray>> {
        val idx = input.indexOf("")
        val scanned = if (idx > -1) { input.subList(1, idx) } else { input.drop(1) }
        return listOf(scanned.map { it.split(",") }
            .map { intArrayOf(it[0].toInt(), it[1].toInt(), it[2].toInt()) }) +
                if (idx > -1) { parseInput(input.drop(idx + 1)) } else { emptyList() }
    }

    fun det(r: Array<IntArray>): Int {
        return r[0][0]*(r[1][1]*r[2][2]-r[1][2]*r[2][1]) -
                r[0][1]*(r[1][0]*r[2][2]-r[1][2]*r[2][0]) +
                r[0][2]*(r[1][0]*r[2][1]-r[1][1]*r[2][0])
    }

    fun perms(n: Int): List<List<Int>> {
        if (n == 1) return listOf(listOf(0))
        return perms(n - 1).map { (0 until n).map { idx ->
            it.take(it.size - idx) + listOf(n - 1) + it.takeLast(idx) } }.flatten()
    }

    fun getRList(): List<Array<IntArray>> {
        val rows = listOf(intArrayOf(1, 0, 0), intArrayOf(0, 1, 0), intArrayOf(0, 0, 1))
        val flips = listOf(1, -1)
        return perms(3).map{ p -> p.map { rows[it] }.toTypedArray() }
            .map { r -> flips.map { b1 -> flips.map { b2 -> flips.map { b3 -> intArrayOf(b1, b2, b3) } } }.flatten().flatten()
                .map { f -> r.map { rRow -> rRow.indices.map { rRow[it] * f[it] }.toIntArray() }.toTypedArray() } }.flatten()
            .filter { det(it) == 1 }
    }
    val rList = getRList()

    fun add(t1: IntArray, t2: IntArray): IntArray {
        check(t1.size == 3 && t2.size == 3)
        return t1.indices.map { t1[it] + t2[it] }.toIntArray()
    }

    fun sub(t1: IntArray, t2: IntArray): IntArray {
        check(t1.size == 3 && t2.size == 3)
        return t1.indices.map { t1[it] - t2[it] }.toIntArray()
    }

    fun mul(r: Array<IntArray>, x: IntArray): IntArray {
        check(r.size == 3 && r[0].size == 3 && x.size == 3)
        return r.map { rRow -> rRow.indices.sumOf { rRow[it] * x[it] } }.toIntArray()
    }

    fun matMul(r1: Array<IntArray>, r2: Array<IntArray>): Array<IntArray> {
        val (n, m) = r1.size to r1[0].size
        check(m == r2.size)
        val k = r2[0].size
        return (0 until n).map { i -> (0 until k).map { j ->
            (0 until m).sumOf { k ->r1[i][k] * r2[k][j] } }.toIntArray() }.toTypedArray()
    }

    fun match(s1: List<IntArray>, s2: List<IntArray>): Pair<Array<IntArray>, IntArray>? {
        // Formation, x1 = r * x2 + t
        val s1Set = s1.map { Triple(it[0], it[1], it[2]) }.toSet()
        rList.map { r ->
            s1.indices.map { idx1 -> s2.indices.map { idx2 -> idx1 to idx2 } }.flatten()
                .forEach { (idx1, idx2) ->
                    val (x1, x2) = s1[idx1] to s2[idx2]
                    val t = sub(x1, mul(r, x2))
                    val s2Set = s2.map { add(mul(r, it), t) }.map { Triple(it[0], it[1], it[2]) }.toSet()
                    val intersectionCount = (s1Set intersect s2Set).size
                    if (intersectionCount >= 12) return r to t
                }
        }
        return null
    }

    fun f(s: List<List<IntArray>>): List<Pair<Array<IntArray>, IntArray>> {
        val rtij = s.indices.map { i -> s.indices.mapNotNull { j -> if (i != j ) { i to j } else { null } } }.flatten()
            .mapNotNull { (i, j) ->
                val res = match(s[i], s[j])
                if (res != null) (i to j) to res
                else null
            }.toMap()

        val rt0 = (listOf(rList[0] to intArrayOf(0, 0, 0)) + (0 until s.size - 1).map { null }).toMutableList()
        for (round in 1 until s.size) {
            for((idx, rt) in rtij) {
                val (i, j) = idx
                if (rt0[i] != null && rt0[j] == null) {
                    val (rij, tij) = rt
                    val (r0i, t0i) = rt0[i]!!
                    rt0[j] = matMul(r0i, rij) to add(mul(r0i, tij), t0i)
                    break
                }
            }
        }
        check(rt0.all { it != null })
        return rt0.toList() as List<Pair<Array<IntArray>, IntArray>>
    }

    fun part1(s: List<List<IntArray>>, rt0: List<Pair<Array<IntArray>, IntArray>>): Int {
        val b = mutableSetOf<Triple<Int, Int, Int>>()
        for (i in s.indices) {
            for (xi in s[i]) {
                val (r0i, t0i) = rt0[i]
                val b0 = add(mul(r0i, xi), t0i)
                b.add(Triple(b0[0], b0[1], b0[2]))
            }
        }
        return b.size
    }

    fun distance(x1: IntArray, x2: IntArray): Int {
        check(x1.size == 3 && x2.size == 3)
        return x1.indices.sumOf { kotlin.math.abs(x1[it] - x2[it]) }
    }

    fun part2(rt0: List<Pair<Array<IntArray>, IntArray>>): Int {
        return rt0.maxOf { (_, t0i) -> rt0.maxOf { (_, t0j) -> distance(t0i, t0j) } }
    }

    val input: List<String> = readInput("Day19")
    val s = parseInput(input)
    val res = f(s)
    println(part1(s, res))
    println(part2(res))
}
