fun main() {
    fun parseInput(input: List<String>): Pair<String, Map<String, String>> {
        return input[0] to input.drop(2).map { line -> line.split(" -> ") }.associate { it[0]!! to it[1]!! }
    }

    fun createOrAdd(acc: Map<String, Long>, s: String, count: Long): Map<String, Long> {
        if (!acc.containsKey(s)) return acc.plus(s to count)
        return acc.minus(s).plus(s to acc[s]!! + count)
    }

    fun doStep(pairs: Map<String, Long>, rules: Map<String, String>): Map<String, Long> {
        return pairs.toList().fold(mapOf<String, Long>()) { acc, (s, count) ->
            if (rules.containsKey(s)) createOrAdd(createOrAdd(acc, rules[s]+s.takeLast(1), count), s.take(1)+rules[s], count)
            else createOrAdd(acc, s, count)
        }
    }

    fun doSteps(pairs: Map<String, Long>, rules: Map<String, String>, nSteps: Long): Map<String, Long> {
        if (nSteps == 0L) return pairs
        return doSteps(doStep(pairs, rules), rules, nSteps - 1)
    }

    fun part1(input: List<String>): Long {
        val (tmpl, rules) = parseInput(input)
        val (firstChar, lastChar) = tmpl.take(1) to tmpl.takeLast(1)
        val nSteps = 10L
        val pairs = doSteps(
            (0 until tmpl.length - 1).map { idx -> tmpl.substring(idx, idx+2) }
                .fold(mapOf<String, Long>()) { acc, s -> if (!acc.containsKey(s)) { acc.plus(s to 1) } else { acc.minus(s).plus(s to acc[s]!! + 1) } },
            rules, nSteps)
        val counts = createOrAdd(createOrAdd(pairs.toList().fold(mapOf<String, Long>()) { acc, (s, count) ->
            createOrAdd(createOrAdd(acc, s.take(1), count), s.takeLast(1), count)
        }, firstChar, 1), lastChar, 1).mapValues { it.value / 2 }.toList().sortedBy { it.second }
        return counts.last().second - counts.first().second
    }

    fun part2(input: List<String>): Long {
        val (tmpl, rules) = parseInput(input)
        val (firstChar, lastChar) = tmpl.take(1) to tmpl.takeLast(1)
        val nSteps = 40L
        val pairs = doSteps(
            (0 until tmpl.length - 1).map { idx -> tmpl.substring(idx, idx+2) }
                .fold(mapOf<String, Long>()) { acc, s -> if (!acc.containsKey(s)) { acc.plus(s to 1) } else { acc.minus(s).plus(s to acc[s]!! + 1) } },
            rules, nSteps)
        val counts = createOrAdd(createOrAdd(pairs.toList().fold(mapOf<String, Long>()) { acc, (s, count) ->
            createOrAdd(createOrAdd(acc, s.take(1), count), s.takeLast(1), count)
        }, firstChar, 1), lastChar, 1).mapValues { it.value / 2 }.toList().sortedBy { it.second }
        return counts.last().second - counts.first().second
    }

    val input: List<String> = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
