fun main() {
    solve("Day19_test.txt", 6, 16L)
    solve("Day19.txt", 304, 705756472327497L)
}

private fun solve(path: String, part1: Int, part2: Long) {
    val lines = lines(path)
    val towels = lines[0].split(", ")
    val patterns = lines.subList(2, lines.size)
    check(part1, part1(towels, patterns))
    check(part2, part2(towels, patterns))
}

private typealias Cache = MutableMap<String, Long>

private fun part1(towels: List<String>, patterns: List<String>): Int =
    patterns.count { towels.countCombinationsFor(it) > 0 }

private fun part2(towels: List<String>, patterns: List<String>): Long = patterns.sumOf { towels.countCombinationsFor(it) }

private fun List<String>.countCombinationsFor(pattern: String, cache: Cache = mutableMapOf()): Long =
    cache.getOrPut(pattern) {
        filter { pattern.startsWith(it) }
        sumOf {
            countCombinationsFor(pattern.removePrefix(it), cache)
        }
    }