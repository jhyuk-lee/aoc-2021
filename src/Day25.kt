fun main() {
    fun parseInput(input: List<String>): Pair<Set<Pair<Int, Int>>, Set<Pair<Int, Int>>> {
        return input.mapIndexedNotNull { r, row ->
            row.mapIndexedNotNull { c, v ->
                if (v == '>') r to c
                else null
            }
        }.flatten().toSet() to input.mapIndexedNotNull { r, row ->
            row.mapIndexedNotNull { c, v ->
                if (v == 'v') r to c
                else null
            }
        }.flatten().toSet()
    }

    fun printBoard(boardSize: Pair<Int, Int>, easts: Set<Pair<Int, Int>>, souths: Set<Pair<Int, Int>>) {
        (0 until boardSize.first).forEach { r ->
            println((0 until boardSize.second).map { c ->
                if (r to c in easts) '>'
                else if (r to c in souths) 'v'
                else '.'
            }.joinToString(""))
        }
    }

    fun part1(input: List<String>): Int {
        val (R, C) = input.size to input[0].length
        var (easts, souths) = parseInput(input)
        var rounds = 0
        while (true) {
            rounds += 1
            var moved = false
            easts = easts.map { (r, c) ->
                val next = r to (c+1)%C
                if (easts.contains(next) || souths.contains(next)) r to c
                else { moved = true; next }
            }.toSet()
            souths = souths.map { (r, c) ->
                val next = (r+1)%R to c
                if (easts.contains(next) || souths.contains(next)) r to c
                else { moved = true; next }
            }.toSet()
            if (!moved) break
        }
        printBoard(R to C, easts, souths)
        return rounds
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val input: List<String> = readInput("Day25")
    println(part1(input))
    println(part2(input))
}
