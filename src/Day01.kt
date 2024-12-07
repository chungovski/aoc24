import kotlin.math.abs

fun main() {
    solve("Day01_test.txt", 11, 31)
    solve("Day01.txt", 2769675, 24643097)
}

private fun solve(path: String, part1: Int, part2: Int) {
    val cols = toColumns(linesSplitInt(path, " "))
    cols.forEach { l -> l.sort() }
    check(part1, part1(cols))
    check(part2, part2(cols))
}

private fun part1(columns: List<List<Int>>): Int = columns[0].zip(columns[1]).sumOf { abs(it.first - it.second) }


private fun part2(columns: List<List<Int>>): Int = columns[0].sumOf { columns[1].count { lookup -> it == lookup } * it }
