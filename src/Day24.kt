typealias LinearExp = Map<String?, Long>
typealias Constraint = Triple<Boolean, LinearExp, LinearExp>
typealias VarType = Pair<List<Constraint>, Map<String, LinearExp>>

fun main() {
    fun parseInput(input: List<String>): List<Pair<String, Any>> {
        return input.map { it.split(" ") }.map {
            when (it[0]) {
                "inp" -> "inp" to it[1]
                else -> it[0] to (it[1] to it[2])
            }
        }
    }

    fun singleVarExp(v: String): LinearExp {
        if (v[0].isLowerCase()) return mapOf(v to 1L)
        return mapOf(null to v.toLong())
    }

    fun ff(e1: LinearExp, op: String, e2: LinearExp): LinearExp {
        if (op == "add") {
            return (e1.keys + e2.keys).distinct().associateWith {
                e1.getOrDefault(it, 0L) + e2.getOrDefault(it, 0L)
            }.filter { (k, v) -> v != 0L }
        }
        if (op == "mul") {
            if (e1.isEmpty() || e2.isEmpty()) return mapOf()
            var (c, e) = 0L to e1
            if (e1.size == 1 && e1.containsKey(null)) {
                c = e1[null]!!
                e = e2
            }
            else {
                check(e2.size == 1 && e2.containsKey(null))
                c = e2[null]!!
                e = e1
            }
            return e.map { (k, v) -> k to v * c }.filter { it.second != 0L }.toMap()
        }
        if (op == "mod" || op == "div") {
            check(e2.isNotEmpty())
            if (e1.isEmpty()) return mapOf()
            check (e2.size == 1 && e2.containsKey(null))
            val (c, e) = e2[null]!! to e1
            check(c != 0L)
            if (op == "mod") check(c > 0)
            return e.map { (k, v) ->
                if (op == "mod") k to v % c
                else k to v / c
            }.filter { it.second != 0L }.toMap()
        }
        throw Exception()
    }

    fun f(v: VarType, idx: Int, m: Pair<String, Any>): VarType {
        if (m.first == "inp") {
            return v.first to v.second.map { (x, e) -> x to
                if (x == m.second) singleVarExp("${m.second}${idx/18}")
                else e
            }.toMap()
        }
        check(m.first != "eql")
        val (a, b) = m.second as Pair<String, String>
        return v.first to v.second.map { (x, e) -> x to
                if (x == a) ff(e, m.first, v.second.getOrDefault(b, singleVarExp(b)))
                else e
            }.toMap()
    }

    fun constraintIsValid(e1: LinearExp, e2: LinearExp): Boolean {
        // e1 should be b or aw+b, e2 should be w
        check(e1.size <= 2 && e1.containsKey(null))
        check(e2.size == 1 && !e2.containsKey(null))
        return -9 < e1[null]!! && e1[null]!! < 9
    }

    fun zeroableExp(e: LinearExp): Boolean {
        val minV = e.map { (k, v) ->
            if (k == null) v
            else if (v > 0L) v
            else v*9L
        }.sum()
        val maxV = e.map { (k, v) ->
            if (k == null) v
            else if (v > 0L) v*9L
            else v
        }.sum()
        return minV <= 0 && maxV >= 0
    }

    fun eToStr(e: LinearExp): String {
        if (e.isEmpty()) return "0"
        return e.map { (k, v) ->
            if (k == null) "$v"
            else if (v == 1L) "$k"
            else "$v$k"
        }.joinToString("+")
    }

    fun constraintToStr(c: Constraint): String {
        return "${eToStr(c.second)} ${if (c.first) "==" else "!="} ${eToStr(c.third)}"
    }

    fun printVars(vars: List<VarType>) {
        vars.forEach { v ->
            println(v.first.map { constraintToStr(it) })
            v.second.forEach { (k, e) ->
                println("$k: ${eToStr(e)}")
            }
        }
    }

    fun part1(input: List<String>): String {
        val monad = parseInput(input)
        /*
        Hacky solution for part 1
        Noticeable patterns
        - inp w followed by 17 expressions is repeated 14 times
        - eql operation shows 2 * 14 times
        - mul doesn't create higher dimension term
         */
        var vars: List<VarType> = listOf(emptyList<Constraint>() to mapOf(
            "w" to mapOf(), "x" to mapOf(), "y" to mapOf(), "z" to mapOf()))
        for ((idx, m) in monad.withIndex()) {
            if (idx % 18 == 6) {
                // handle 2 lines: eql x w, eql x 0
                check (m.first == "eql" && m.second == "x" to "w")
                vars = vars.map { v -> if (constraintIsValid(v.second["x"]!!, v.second["w"]!!)) {
                        listOf(true to "0", false to "1").map { (x, y) ->
                            v.first.plus(
                                Triple(x, v.second["x"]!!, v.second["w"]!!)
                            ) to v.second.map { (x, e) ->
                                x to if (x == "x") singleVarExp(y)
                                else e
                            }.toMap()
                        }
                    }
                    else { listOf(v.first to v.second.map { (x, e) -> x to if(x == "x") singleVarExp("1") else e }.toMap()) }
                }.flatten()
                continue
            }
            if (idx % 18 == 7) { check (m.first == "eql" && m.second == "x" to "0"); continue }
            vars = vars.map { v -> f(v, idx, m) }
        }
        vars = vars.filter { zeroableExp(it.second["z"]!!) }
        printVars(vars)
        println(vars.size)
        /*
        Yeeeeeaaaaah this gave only 1 result
        Constraint -> [-3+w2 == w3, 3+w5 == w6, 2+w4 == w7, -5+w8 == w9, w10+-1 == w11, 7+w1 == w12, w0+-8 == w13]
        for z = 0
        handmade maximum result is
         */
        return "92967699949891"
    }

    fun part2(input: List<String>): String {
        /*
        Minimum for the same constraint
         */
        return "91411143612181"
    }

    val input: List<String> = readInput("Day24")
    println(part1(input))
    println(part2(input))
}
