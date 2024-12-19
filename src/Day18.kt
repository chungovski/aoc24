fun main() {
    solve("Day18_test.txt", 12, 22, "6,1")
    solve("Day18.txt", 1024, 356, "22,33")
}

private fun solve(path: String, amountFalling: Int, part1: Int, part2: String) {
    val lines = lines(path)
    check(part1, part1(lines, amountFalling))
    check(part2, part2(lines))
}

private fun part1(lines: List<String>, amountFalling: Int): Int {
    val (bytesPos, maxIndex) = parseBytesAndMax(lines)
    val (start, end) = createStartEnd(maxIndex)
    val grid = Grid.createWithSize(maxIndex, maxIndex)

    return Graph.withStartNode(Node(start, 0) {
        grid.inside(it) && it !in bytesPos.take(amountFalling)
    }).shortestPathToOrNull(end) ?: 0
}

private fun part2(lines: List<String>): String {
    val (bytesPos, maxIndex) = parseBytesAndMax(lines)
    val (start, end) = createStartEnd(maxIndex)
    val grid = Grid.createWithSize(maxIndex, maxIndex)

    val firstNoPathIndex = binarySearch(bytesPos.indices) { index ->
        Graph.withStartNode(Node(start, 0) {
            grid.inside(it) && it !in bytesPos.take(index)
        }).shortestPathToOrNull(end) == null // find first that's not working anymore
    }
    val lastByte = bytesPos[firstNoPathIndex - 1]
    return "${lastByte.x},${lastByte.y}"
}

// Should be O(log n) speedier than linear search
// Copied and adjusted from https://gist.github.com/vamsitallapudi/44f8fd1b86f5343306bf985112e2f390
private fun binarySearch(input: IntRange, predicate: (Int) -> Boolean): Int {
    var (low, high) = input.first to input.last
    var mid: Int
    while (low < high) {
        mid = low + (high - low) / 2
        if (predicate(mid)) {
            high = mid
        } else {
            low = mid + 1
        }
    }
    return low
}

private fun createStartEnd(maxIndex: Int): Pair<Point, Point> {
    val start = Point(0, 0)
    val end = Point(maxIndex, maxIndex)
    return Pair(start, end)
}

private fun parseBytesAndMax(lines: List<String>): Pair<List<Point>, Int> {
    val bytesPos = lines.map {
        val split = it.split(',')
        Point(split[0].toInt(), split[1].toInt())
    }
    val maxIndex = bytesPos.maxOf { if (it.x > it.y) it.x else it.y }
    return Pair(bytesPos, maxIndex)
}

data class Node(val p: Point, override var distance: Int, private val isValid: (Point) -> Boolean) :
    Graph.Node<Point, Node> {
    override val key = p
    override fun getAdjacentNodes(): List<Node> =
        orthogonalDirections.map { p.move(it.point) }
            .filter { isValid(it) }
            .map { Node(it, 1, isValid) }
}
