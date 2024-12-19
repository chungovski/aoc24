fun main() {
    solve("Day06_test.txt", 41, 6)
    solve("Day06.txt", 5095, 1933)
}

private fun solve(path: String, part1: Int, part2: Int) {
    val grid = Grid(lines(path))
    val walkedGrid = grid.copy()
    check(part1, part1(walkedGrid))
    check(part2, part2(grid, walkedGrid))
}

private fun part1(grid: Grid): Int {
    startWalking(grid)
    return grid.findAll(STEP).size
}

private fun part2(grid: Grid, walkedGrid: Grid): Int = walkedGrid.findAll('X').parallelStream().filter {
    var shouldRunFurther = true
    commandGuard(grid) { guard, pos ->
        if (it == pos || it == pos.move(guard.direction.point)) {
            shouldRunFurther = false
        }
    }

    if(shouldRunFurther) {
        val editedGrid = grid.copy()
        editedGrid.set(it, '0')
        try {
            startWalking(editedGrid)
        } catch (e: Exception) {
            if (e.message == "loop") {
                println("issa loop [${it.x}/${it.y}]")
                return@filter true
            }
        }
    }
    false
}.count().toInt()

const val STEP = 'X'

private fun startWalking(grid: Grid) {
    var guardOnGrid = true
    val path = mutableMapOf<Direction, MutableSet<Point>>().apply {
        DirectionChar.entries.map { it.direction }.forEach { put(it, mutableSetOf()) }
    }
    while (guardOnGrid) {
        commandGuard(grid) { guard, pos ->
            val newPosition = walkStraight(pos, guard.direction, grid, path)
            guardOnGrid = grid.inside(newPosition)
        }
        guardOnGrid = grid.findAny(DirectionChar.entries.map { it.char }).isNotEmpty()
    }
}

private fun commandGuard(grid: Grid, action: (DirectionChar, Point) -> Unit) {
    grid.findAny(DirectionChar.entries.map { it.char }).forEach { (char, pos) ->
        DirectionChar.from(char)?.let {
            action(it, pos)
        }
    }
}

private fun walkStraight(
    position: Point,
    direction: Direction,
    grid: Grid,
    path: Map<Direction, MutableSet<Point>>
): Point {
    var newPosition = position
    while (".X".toCharArray().contains(grid.get(newPosition.move(direction.point)))) {
        grid.set(newPosition, STEP)
        if (!path[direction]?.contains(newPosition)!!) {
            path[direction]?.add(newPosition)
        } else {
            throw Exception("loop")
        }
        newPosition = newPosition.move(direction.point)
    }
    if (grid.get(newPosition.move(direction.point)) == Grid.OUT_OF_BOUND_CHAR) {
        grid.set(newPosition, STEP)
    } else {
        DirectionChar.from(direction.turn())?.let { grid.set(newPosition, it.char) }
    }
    return newPosition
}


