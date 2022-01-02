import java.util.PriorityQueue

val costs = mapOf('A' to 1, 'B' to 10, 'C' to 100, 'D' to 1000)
val hallways = listOf(0 to 0, 0 to 1, 0 to 3, 0 to 5, 0 to 7, 0 to 9, 0 to 10)
typealias placeType = Map<Char, List<Pair<Int, Int>>>
typealias graphType = Map<Pair<Int, Int>, List<Pair<Int, Int>>>

fun main() {
    fun parseInput(input: List<String>, part: Int): List<String> {
        if (part == 1) {
            return listOf(
                input[1].drop(1).dropLast(1),
                input[2].drop(1).dropLast(1).replace('#', ' ').trimEnd(),
                input[3].drop(1).dropLast(1).replace('#', ' ').trimEnd(),
            )
        }
        return listOf(
            input[1].drop(1).dropLast(1),
            input[2].drop(1).dropLast(1).replace('#', ' ').trimEnd(),
            "  D C B A",
            "  D B A C",
            input[3].drop(1).dropLast(1).replace('#', ' ').trimEnd(),
        )
    }

    fun generatePlaces(part: Int): placeType {
        return "ABCD".map { x ->
            x to (1..part*2).map { it to (x.code - 'A'.code + 1) * 2 }
        }.toMap()
    }

    fun generateGraph(part: Int): graphType {
        val places = generatePlaces(part)
        val g = ((0..10).map { 0 to it } + places.values.flatten()).associateWith { mutableListOf<Pair<Int, Int>>() }
        fun addToG(v: Pair<Int, Int>, w: Pair<Int, Int>) {
            g[v]!!.add(w)
            g[w]!!.add(v)
        }
        for (j in 0 until 10) addToG(0 to j, 0 to j + 1)
        for ((k, v) in places) {
            addToG(v[0], v[0].first - 1 to v[0].second)
            for (k in 0 until v.size - 1) {
                addToG(v[k], v[k + 1])
            }
        }
        return g.toMap()
    }

    fun generateFinal(part: Int): List<String> {
        return listOf("...........") + List (if (part == 1) 2 else 4) { "  A B C D" }
    }

    fun printMap(s: List<String>) {
        s.forEach { println(it) }
    }

    fun countMoves(s: List<String>, g: graphType, p1: Pair<Int, Int>, p2: Pair<Int, Int>): Int? {
        val q = mutableListOf(p1 to 0)
        val visited = mutableSetOf(p1)
        while (q.isNotEmpty()) {
            val (p, d) = q.removeFirst()
            for (pNext in g[p]!!) {
                if (pNext in visited) continue
                if (s[pNext.first][pNext.second] != '.') continue
                if (pNext == p2) return d + 1
                q.add(pNext to d + 1)
                visited.add(pNext)
            }
        }
        return null
    }

    fun moveChar(s: List<String>, p1: Pair<Int, Int>, p2: Pair<Int, Int>): List<String> {
        check(s[p2.first][p2.second] == '.')
        val ss = s.toMutableList()
        ss[p2.first] = ss[p2.first].take(p2.second) +
                ss[p1.first][p1.second] +
                ss[p2.first].substring(p2.second + 1)
        ss[p1.first] = ss[p1.first].take(p1.second) + '.' +
                ss[p1.first].substring(p1.second + 1)
        return ss.toList()
    }

    fun getNext(s: List<String>, g: graphType, places: placeType): List<Pair<Int, List<String>>> {
        val p = mutableListOf<Pair<Int, List<String>>>()
        for (p1 in hallways) {
            val x = s[p1.first][p1.second]
            if (x == '.') continue
            val (cost, xPlaces) = costs[x]!! to places[x]!!
            for (place in xPlaces.asReversed()) {
                val d = countMoves(s, g, p1, place)
                if (d != null) p.add(cost * d to moveChar(s, p1, place))
                if (s[place.first][place.second] != x) break
            }
        }
        for (p1 in places.values.flatten()) {
            val x = s[p1.first][p1.second]
            if (x == '.') continue
            val cost = costs[x]!!
            for (h in hallways) {
                val d = countMoves(s, g, p1, h)
                if (d != null) p.add(cost * d to moveChar(s, p1, h))
            }
        }
        return p
    }

    fun f(s: List<String>, g: graphType, places: placeType, finalState: List<String>): Int {
//        printMap(s)
//        println(g)
//        println(places)
//        printMap(finalState)
        val visited = mutableSetOf<String>()
        val pq = PriorityQueue<Pair<Int, List<String>>>(Comparator<Pair<Int, List<String>>>{ a, b ->
            if (a.first == b.first){
                val (aStr, bStr) = a.second.joinToString("") to b.second.joinToString("")
                if (aStr < bStr) -1 else if (aStr > bStr) 1 else 0
            }
            else a.first - b.first
        })
        pq.add(0 to s)
        while (pq.isNotEmpty()) {
            val (d, s) = pq.poll()
            if (s.joinToString("") == finalState.joinToString("")) {
                return d
            }
            if (s.joinToString("") in visited) continue
            visited.add(s.joinToString(""))
            val nextStates = getNext(s, g, places)
            for ((dNext, sNext) in nextStates) {
                pq.add(d + dNext to sNext)
            }
        }
        return 0
    }

    fun part1(input: List<String>): Int {
        val s = parseInput(input, 1)
        val g = generateGraph(1)
        val places = generatePlaces(1)
        val finalState = generateFinal(1)
        return f(s, g, places, finalState)
    }

    fun part2(input: List<String>): Int {
        val s = parseInput(input, 2)
        val g = generateGraph(2)
        val places = generatePlaces(2)
        val finalState = generateFinal(2)
        return f(s, g, places, finalState)
    }

    val input: List<String> = readInput("Day23")
    println(part1(input))
    println(part2(input))
}
