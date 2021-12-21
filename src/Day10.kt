fun main() {
    val pairs = mapOf(')' to '(', ']' to '[', '}' to '{', '>' to '<')

    fun firstIllegal(line: String, stack: String): Char? {
        if (line.isEmpty()) return null
        val c = line[0]
        if (pairs.containsKey(c)) {
            if (stack.isEmpty() || stack.last() != pairs[c]) return c
            return firstIllegal(line.drop(1), stack.dropLast(1))
        }
        return firstIllegal(line.drop(1), stack + c)
    }

    fun part1(input: List<String>): Int {
        val points = mapOf(null to 0, ')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
        return input.map { firstIllegal(it, "") }.sumOf { points[it]!! }
    }

    fun incompletes(line: String, stack: String): String? {
        if (line.isEmpty()) return stack
        val c = line[0]
        if (pairs.containsKey(c)) {
            if (stack.isEmpty() || stack.last() != pairs[c]) return null
            return incompletes(line.drop(1), stack.dropLast(1))
        }
        return incompletes(line.drop(1), stack + c)
    }

    fun incompleteScore(s: String): Long {
        if (s.isEmpty()) return 0L
        return when(s[0]) {
            '(' -> 1 + 5 * incompleteScore(s.drop(1))
            '[' -> 2 + 5 * incompleteScore(s.drop(1))
            '{' -> 3 + 5 * incompleteScore(s.drop(1))
            '<' -> 4 + 5 * incompleteScore(s.drop(1))
            else -> 0L
        }
    }

    fun part2(input: List<String>): Long {
        val scores = input.mapNotNull { incompletes(it, "") }.map { incompleteScore(it) }.sorted()
        return scores[scores.size / 2]
    }

    val input: List<String> = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
