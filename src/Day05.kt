import java.lang.Integer.max
import java.lang.Integer.min

fun main() {
    fun parseInput(input: List<String>): List<List<Int>> {
        return input.map { line -> line.split(" -> ").map { pair -> pair.split(",").map { it.toInt() } }.flatten() }
    }

    fun intersects(seg: List<Int>, x: Int, y: Int): Int {
        if (seg[0] == seg[2]) {
            if (seg[0] == x && min(seg[1], seg[3]) <= y && y <= max(seg[1], seg[3])) return 1
        }
        else if (seg[1] == seg[3] && min(seg[0], seg[2]) <= x && x <= max(seg[0], seg[2])) {
            if (seg[1] == y) return 1
        }
        return 0
    }

    fun part1(input: List<String>): Int {
        val segs = parseInput(input)
        check(segs.all { it.size == 4 })
        val n = segs.flatten().maxOrNull()!!
        return (0..n).map { x -> (0..n).map {y -> x to y} }.flatten()
            .map { p -> segs.sumOf { seg -> intersects(seg, p.first, p.second) } }
            .count { it >= 2 }
    }

    fun intersectsDiag(seg: List<Int>, x: Int, y: Int): Int {
        if (seg[0] == seg[2]) {
            if (seg[0] == x && min(seg[1], seg[3]) <= y && y <= max(seg[1], seg[3])) return 1
        }
        else if (seg[1] == seg[3] && min(seg[0], seg[2]) <= x && x <= max(seg[0], seg[2])) {
            if (seg[1] == y) return 1
        }
        else if (seg[0] - seg[1] == seg[2] - seg[3] && min(seg[0], seg[2]) <= x && x <= max(seg[0], seg[2])) {
            if (seg[0] - seg[1] == x - y) return 1
        }
        else if (seg[0] + seg[1] == seg[2] + seg[3] && min(seg[0], seg[2]) <= x && x <= max(seg[0], seg[2])) {
            if (seg[0] + seg[1] == x + y) return 1
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        val segs = parseInput(input)
        check(segs.all { it.size == 4 })
        val n = segs.flatten().maxOrNull()!!
        return (0..n).map { x -> (0..n).map {y -> x to y} }.flatten()
            .map { p -> segs.sumOf { seg -> intersectsDiag(seg, p.first, p.second) } }
            .count { it >= 2 }
    }

    val input: List<String> = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
