fun main() {
    fun parseInput(input: List<String>): Map<String, List<String>> {
        val edges = input.map { line -> val s = line.split("-"); listOf(s[0] to s[1], s[1] to s[0]) }.flatten()
        return edges.map { it.first }.toSet().associateWith { v ->
            edges.filter { it.first == v }.map { it.second }.sorted()
        }
    }

    fun numPaths(v: String, vCheck: Set<String>, g: Map<String, List<String>>): Int {
        if (v == "end") return 1
        return g.getOrDefault(v, emptyList())
            .filter { !vCheck.contains(it) }
            .sumOf {
                if (it[0].isLowerCase()) numPaths(it, vCheck.plus(it), g)
                else numPaths(it, vCheck, g)
            }
    }

    fun part1(input: List<String>): Int {
        val g = parseInput(input)
        return numPaths("start", setOf("start"), g)
    }

    fun numPaths2(v: String, vCheck: Set<String>, revisitedLower: Boolean, g: Map<String, List<String>>): Int {
        if (v == "end") return 1
        return g.getOrDefault(v, emptyList()).filterNot { it == "start" }
            .sumOf {
                if (it[0].isLowerCase()) {
                    if (vCheck.contains(it)) {
                        if (revisitedLower) 0
                        else numPaths2(it, vCheck, true, g)
                    } else {
                        if (revisitedLower) numPaths2(it, vCheck.plus(it), true, g)
                        else numPaths2(it, vCheck.plus(it), false, g)
                    }
                }
                else {
                    numPaths2(it, vCheck, revisitedLower, g)
                }
            }
    }

    fun part2(input: List<String>): Int {
        val g = parseInput(input)
        return numPaths2("start", setOf("start"), false, g)
    }

    val input: List<String> = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
