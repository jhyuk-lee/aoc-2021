fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            line.split(" | ")[1]!!.split(" ")
                .count { it.length == 2 || it.length == 3 || it.length == 4 || it.length == 7 }
        }
    }

    fun generatePermutations(source: String, result: String): List<String> {
        if (source.isEmpty()) {
            return listOf(result)
        }
        return source.withIndex()
            .map { (idx, c) -> generatePermutations(source.substring(0, idx) + source.substring(idx+1), result + c) }
            .flatten()
    }

    fun parseLine(line: String): Pair<List<String>, List<String>> {
        val s = line.split(" | ")
        return s[0].split(" ") to s[1].split(" ")
    }

    fun replaceByPermutation(s: String, p: String): String {
        return s.map { c -> p[c.code-'a'.code]!! }.sorted().joinToString("")
    }

    val referenceList = listOf("abcefg", "cf", "acdeg", "acdfg", "bcdf", "abdfg", "abdefg", "acf", "abcdefg", "abcdfg")
    fun f(a: Pair<List<String>, List<String>>, permutations: List<String>): Int? {
        val (clue, output) = a
        for (p in permutations) {
            if (referenceList.toSet() == clue.map { replaceByPermutation(it, p) }.toSet()) {
                return output.map { (referenceList.indexOf(replaceByPermutation(it, p))!! + '0'.code).toChar() }.joinToString("").toInt()
            }
        }
        return null
    }

    fun part2(input: List<String>): Int {
        val permutations = generatePermutations("abcdefg", "")
        return input.sumOf { line ->
            f(parseLine(line), permutations)!!
        }
    }

    val input: List<String> = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
