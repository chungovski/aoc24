import kotlin.collections.ArrayDeque

fun main() {
    solve("Day12_test2.txt", 140, 80)
    solve("Day12_test3.txt", 772, 436)
    solve("Day12_test.txt", 1930, 1206)
    solve("Day12.txt", 1549354, 937032)
}

private fun solve(path: String, part1: Int, part2: Int) {
    val grid = Grid(lines(path))
    check(part1, part1(grid))
    check(part2, part2(grid))
}

private fun part1(grid: Grid): Int = grid.parseRegions().sumOf { it.getArea() * it.getPerimeterAmount() }
private fun part2(grid: Grid): Int = grid.parseRegions().sumOf { it.getArea() * it.getSidesAmount() }

private typealias Region = Set<Point>

fun Grid.parseRegions() = buildList {
    val visited = mutableSetOf<Point>()
    val plantFieldMap = this@parseRegions.getAllPoints()

    plantFieldMap.forEach { (field, plant) ->
        if (field in visited) {
            return@forEach
        }
        val queue = ArrayDeque<Point>().apply { add(field) }
        val fields = mutableSetOf<Point>()
        while (queue.isNotEmpty()) {
            val currentField = queue.removeFirst()
            if (visited.add(currentField)) {
                fields.add(currentField)
                //add all neighbouring that are inside grid and matching the plant
                queue.addAll(orthogonalDirections.map { currentField.move(it.point) }
                    .filter { this@parseRegions.inside(it) && it to plant in plantFieldMap })
            }
        }
        add(fields)
    }
}

fun Region.getArea(): Int = this.size

fun Region.getPerimeterAmount(): Int = this.sumOf { p ->
    orthogonalDirections.map { p.move(it.point) }
        .count { it !in this }
}

fun Region.getSidesAmount(): Int = this.sumOf { it.getCornersAmount(this) }

fun Point.getCornersAmount(region: Region): Int =
    (orthogonalDirections + orthogonalDirections.first()) //all combinations
        .zipWithNext().count() { (dir1, dir2) ->
            val orthogonalMovedPoint1 = this.move(dir1.point)
            val orthogonalMovedPoint2 = this.move(dir2.point)
            val diagonallyMovedPoint = orthogonalMovedPoint1.move(dir2.point)
            // it's a corner if either neighbours are outside or both are inside but diagonal neighbour outside
            (orthogonalMovedPoint1 !in region && orthogonalMovedPoint2 !in region) ||
                    (orthogonalMovedPoint1 in region && orthogonalMovedPoint2 in region && diagonallyMovedPoint !in region)

        }

