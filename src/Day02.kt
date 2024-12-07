fun main() {
    solve("Day02_test.txt", 2, 4)
    solve("Day02_test2.txt", 3, 6)
    solve("Day02.txt", 670, 700)
}

private fun solve(path: String, part1: Int, part2: Int) {
    val lines = linesSplitInt(path, " ")
    check(part1, part1(lines))
    check(part2, part2(lines))
}

private fun part1(lines: List<List<Int>>): Int = lines.count { line -> isLineSafe(line) }

private fun part2(lines: List<List<Int>>): Int = lines.count { line ->
    isLineSafe(line) || line.indices.any { isLineSafe(line.filterIndexed { i, _ -> it != i }) }
}

private fun isLineSafe(line: List<Int>): Boolean {
    val steps = line.zipWithNext().map { (a, b) -> a - b }
    return steps.all { it in 1..3 } || steps.all { it in -3..-1 }
}