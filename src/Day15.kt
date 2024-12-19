fun main() {
    solve("Day15_test2.txt", 2028, 1751)
    solve("Day15_test3.txt", 908, 618)
    solve("Day15_test.txt", 10092, 9021)
    solve("Day15.txt", 1514333, 1528453)
}

private fun solve(path: String, part1: Int, part2: Int) {
    val lines = lines(path)
    val indexOfSeparator = lines.indexOf("")
    val gridLines = lines.subList(0, indexOfSeparator)
    val moves = lines.subList(indexOfSeparator + 1, lines.size).joinToString()
    check(part1, part1(gridLines, moves))
    check(part2, part2(gridLines, moves))
}

private const val ROBOT = '@'
private const val BOX = 'O'
private const val WALL = '#'
private const val WIDE_BOX_LEFT = '['
private const val WIDE_BOX_RIGHT = ']'
private val REPLACEMENTS = mapOf("#" to "##", "O" to "[]", "." to "..", "@" to "@.")

private fun part1(gridLines: List<String>, moves: String): Int =
    Grid(gridLines).runMoves(moves)
        .findAll(BOX).sumOf { 100 * it.y + it.x }

private fun part2(gridLines: List<String>, moves: String): Int =
    Grid(gridLines.map { it.replaceAll(REPLACEMENTS) })
        .runMoves(moves)
        .findAll(WIDE_BOX_LEFT).sumOf { 100 * it.y + it.x }

private fun Grid.runMoves(moves: String): Grid =
    moves.mapNotNull { DirectionChar.from(it)?.direction }.forEach { dir ->
        this.find(ROBOT)?.moveAdjacent(dir.point, this)
    }.let { this }

private fun Point.moveAdjacent(step: Point, grid: Grid) {
    val visited = mutableSetOf<Point>()
    val queue = ArrayDeque<Point>().apply { add(this@moveAdjacent) }
    while (queue.isNotEmpty()) {
        val currentP = queue.removeFirst()
        if (visited.add(currentP)) {
            val nextP = currentP.move(step)
            when (grid.get(nextP)) {
                BOX -> queue.add(nextP)
                WIDE_BOX_LEFT -> queue.addWideBoxToQueue(setOf(nextP, nextP.getRight()), step)
                WIDE_BOX_RIGHT -> queue.addWideBoxToQueue(setOf(nextP.getLeft(), nextP), step)
                WALL -> return
            }
        }
    }
    visited.reversed().forEach {
        grid.set(it.move(step), grid.get(it))
        grid.set(it, grid.emptyChar)
    }
}

private fun Point.getLeft(): Point = this.move(-1, 0)
private fun Point.getRight(): Point = this.move(1, 0)

private fun String.replaceAll(replacements: Map<String, String>): String =
    replacements.entries.fold(this) { acc, (key, value) -> acc.replace(key, value) }

private fun ArrayDeque<Point>.addWideBoxToQueue(points: Set<Point>, step: Point) = this.addAll(
    if (step.x > 0 || step.y > 0) points else points.reversed()
)
