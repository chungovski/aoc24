fun main() {
    solve("Day10_test.txt", 36, 81)
    solve("Day10.txt", 760, 1764)
}

private fun solve(path: String, part1: Int, part2: Int) {
    val grid = Grid(lines(path))
    check(part1, part1(grid))
    check(part2, part2(grid))
}

private fun part1(grid: Grid): Int = grid.findAll('0').sumOf {
    it.findNextHeight(1, grid).toSet().count()
}

private fun part2(grid: Grid): Int = grid.findAll('0').sumOf {
    it.findNextHeight(1, grid).count()
}

private fun Point.findNextHeight(i: Int, grid: Grid): List<Point> = when {
    i == 10 -> listOf(this) // input index starts with 1
    else -> nextMoves(i.digitToChar(), grid).fold(listOf()) { list, point ->
        list + point.findNextHeight(i + 1, grid)
    }
}

private fun Point.nextMoves(char: Char, grid: Grid): List<Point> = buildList {
    orthogonalDirections.forEach {
        move(it.point).let {
            if (grid.get(it) == char) {
                add(it)
            }
        }
    }
}