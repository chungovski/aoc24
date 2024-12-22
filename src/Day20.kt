fun main() {
    solve("Day20_test.txt", 44 to 2, 3081 to 50)
    solve("Day20.txt", 1429 to 100, 988931 to 100)
}

private fun solve(path: String, part1: FilterResult, part2: FilterResult) {
    val grid = Grid(lines(path))
    check(part1.first, part1(grid, part1.second))
    check(part2.first, part2(grid, part1.second))
}

private fun part1(grid: Grid, minSavedSteps: Int): Int = grid.getPath().countSkippablePaths(2, minSavedSteps)
private fun part2(grid: Grid, minSavedSteps: Int): Int = grid.getPath().countSkippablePaths(20, minSavedSteps)

private typealias FilterResult = Pair<Int, Int>

private fun List<Point>.countSkippablePaths(maxSkipSteps: Int, minSavedSteps: Int): Int =
    this.withIndex().sumOf { (startIndex, cheatStart) ->
        // Only check with minSavedSteps  and +2 (wall plus one field) upwards
        (startIndex + minSavedSteps + 2 until size).count { endIndex ->
            val cheatEnd = this[endIndex]
            val stepsRequired = cheatStart.getOrthogonalDistance(cheatEnd)
            stepsRequired <= maxSkipSteps && endIndex - startIndex - stepsRequired >= minSavedSteps
        }
    }

private fun Grid.getPath(): List<Point> {
    val start = this.find('S')!!
    val end = this.find('E')!!
    val visited = mutableSetOf<Point>().apply { add(start) }
    return generateSequence(start) { p ->
        orthogonalDirections.map { p.move(it.point) }
            .first { get(it) != '#' && visited.add(it) }
    }.takeWhile { it != end }.toList() + end
}