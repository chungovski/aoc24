fun main() {
    solve("Day16_test.txt", 7036, 45)
    solve("Day16_test2.txt", 11048, 64)
    solve("Day16.txt", 105508, 548)
}

private fun solve(path: String, part1: Int, part2: Int) {
    val grid = Grid(lines(path))
    check(part1, part1(grid))
    check(part2, part2(grid))
}

private const val REINDEER = 'S'
private const val END = 'E'
private const val WALL = '#'
private val startingDirection = Direction.RIGHT
private const val TURN_POINTS = 1_000

private fun part1(grid: Grid): Int {
    val startDP = grid.find(REINDEER)!! to startingDirection
    val endPos = grid.find(END)

    val graph = Graph(NodeOne(startDP, 0, grid))

    return graph.dijkstra { it.dp.first == endPos }
        .filter { it.key.first == endPos }.values.min()
}

private fun part2(grid: Grid): Int {
    val startDP = grid.find(REINDEER)!! to startingDirection
    val endPos = grid.find(END)
    val bestPath = mutableSetOf<Point>()

    var min = Int.MAX_VALUE

    val graph = Graph(NodeTwo(listOf(startDP), 0) { grid.get(it) == WALL })
    graph.dijkstra {
        var found = false
        if (it.path.last().first == endPos) {
            if (it.distance <= min) {
                min = it.distance
                bestPath.addAll(it.path.map { dp -> dp.first })
            } else {
                found = true
            }
        }
        found
    }

    return bestPath.size
}

private typealias DirectionalPoint = Pair<Point, Direction>
private typealias Path = List<DirectionalPoint>

private enum class MoveType(val points: Int) {
    STEP(1), ROTATION(TURN_POINTS),
}

private data class NodeOne(val dp: DirectionalPoint, override var distance: Int, private val grid: Grid) :
    Graph.Node<DirectionalPoint, NodeOne> {
    override val key = dp
    override fun getAdjacentNodes(): List<NodeOne> = buildList {
        val (currentPos, dir) = key
        val nextPos = currentPos.move(dir.point)
        if (grid.get(nextPos) != WALL) {
            add(NodeOne(nextPos to dir, MoveType.STEP.points, grid))
        }
        oneRotationMap[dir]?.toList()?.forEach {
            add(NodeOne(currentPos to it, MoveType.ROTATION.points, grid))
        }
    }
}

private data class NodeTwo(val path: Path, override var distance: Int, private val wallAt: (Point) -> Boolean) :
    Graph.Node<DirectionalPoint, NodeTwo> {
    override val key = path.last()
    override fun getAdjacentNodes(): List<NodeTwo> = buildList {
        val (currentPos, dir) = key
        val nextPos = currentPos.move(dir.point)
        if (!wallAt(nextPos)) {
            add(NodeTwo(path + (nextPos to dir), MoveType.STEP.points, wallAt))
        }
        oneRotationMap[dir]?.toList()?.forEach {
            add(NodeTwo(path + (currentPos to it), MoveType.ROTATION.points, wallAt))
        }
    }
}