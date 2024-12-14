fun main() {
    solve("Day04_test.txt", 18, 9)
    solve("Day04.txt", 2633, 1936)
}

private fun solve(path: String, part1: Int, part2: Int) {
    val grid = Grid(lines(path))
    check(part1, part1(grid))
    check(part2, part2(grid))
}

private fun part1(grid: Grid): Int {
    return grid.findAll(XMAS_LETTERS.first()).sumOf { xPoint ->
        Direction.entries.count { dir ->
            var movingPoint = xPoint
            XMAS_LETTERS.drop(1).forEach {
                movingPoint = movingPoint.move(dir.point)
                if (grid.get(movingPoint) != it) {
                    return@count false
                }
            }
            true
        }
    }
}

private fun part2(grid: Grid): Int = grid.findAll('A').count { aPoint ->
    Direction.entries.filter { it.name.startsWith("DIAGONAL") }
        .count { dir ->
            (grid.get(aPoint.move(dir.point)) == 'M' && grid.get(aPoint.moveOpposite(dir.point)) == 'S') ||
                    (grid.get(aPoint.move(dir.point)) == 'S' && grid.get(aPoint.moveOpposite(dir.point)) == 'M')
        } / 2 == 2 //removed double counts returning 2
}

private val XMAS_LETTERS = "XMAS".toCharArray()


