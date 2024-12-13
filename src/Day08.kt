fun main() {
    solve("Day08_test.txt", 14, 34)
    solve("Day08.txt", 379, 1339)
}

private fun solve(path: String, part1: Int, part2: Int) {
    val grid = Grid(lines(path))
    check(part1, part1(grid))
    check(part2, part2(grid))
}

private fun part1(grid: Grid): Int = countSteps(grid) { points, i, j, _ ->
    buildSet {
        add(points[i].moveOpposite(points[i].getDifference(points[j])))
        add(points[j].moveOpposite(points[j].getDifference(points[i])))
    }
}

private fun part2(grid: Grid): Int  = countSteps(grid) { points, i, j, gr ->
    buildSet {
        addAll(getAllMovesInside(points[i], points[i].getDifference(points[j]), gr))
        addAll(getAllMovesInside(points[j], points[j].getDifference(points[i]), gr))
    }
}

private fun countSteps(grid: Grid, addStepFunction: (List<Point>, Int, Int, Grid) -> Set<Point>): Int {
    return buildSet {
        grid.findAnyWith { it.toString().matches(Regex("\\w")) }
            .filter { it.value.size > 1 }
            .forEach { (_, points) ->
                points.indices.flatMap { i ->
                    (i + 1 until points.size).map { j ->
                        addAll(addStepFunction(points, i, j, grid))
                    }
                }
            }
    }.filter {
        grid.inside(it)
    }.size
}

private fun getAllMovesInside(point: Point, difference: Point, grid: Grid): Set<Point> = buildSet {
    generateSequence(point) { it.moveOpposite(difference) }
        .takeWhile { grid.inside(it) }
        .forEach { add(it) }
}