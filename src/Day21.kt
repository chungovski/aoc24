import kotlin.math.max

fun main() {
    solve("Day21_test.txt", 126384L, 154115708116294L)
    solve("Day21.txt", 231564L, 281212077733592L)
}

private fun solve(path: String, part1: Long, part2: Long) {
    val lines = lines(path)
    check(part1, part1(lines))
    check(part2, part2(lines))
}

private fun part1(lines: List<String>): Long = lines.sumOf { it.toMinRobotDirections(2) * it.dropLast(1).toInt() }
private fun part2(lines: List<String>): Long = lines.sumOf { it.toMinRobotDirections(25) * it.dropLast(1).toInt() }

private typealias Keypad = Map<Char, Point>
private typealias CacheKey = Triple<String, Int, Int>

private val numKeys = listOf("789", "456", "123", " 0A").toKeypad()
private val dirKeys = listOf(" ^A", "<v>").toKeypad()
private var cache = mutableMapOf<CacheKey, Long>()

private fun String.toMinRobotDirections(robotAmount: Int, depth: Int = 0): Long {
    if (robotAmount == depth - 1) return length.toLong()
    val keypad = if (depth == 0) numKeys else dirKeys
    return cache.getOrPut(CacheKey(this, robotAmount, depth)) {
        fold(keypad['A']!! to 0L) { (currPos, total), char ->
            val nextPos = keypad[char]!!
            val sequences = currPos.getDifference(nextPos).toDirection().permutations()
                .filter { moves -> moves.filterValidMoves(currPos, keypad) }
                .map { it + "A" }.ifEmpty { listOf("A") }
            nextPos to total + sequences.minOf { it.toMinRobotDirections(robotAmount, depth + 1) }
        }.second
    }
}

private fun String.filterValidMoves(currPos: Point, keypad: Keypad) =
    asSequence().runningFold(currPos) { pos, char ->
        pos.move(DirectionChar.from(char)!!.direction.point)
    }.all { it in keypad.values }

private fun Point.toDirection(): String =
    "v".repeat(max(0, y)) + "^".repeat(max(0, -y)) +
            ">".repeat(max(0, x)) + "<".repeat(max(0, -x))

private fun List<String>.toKeypad(): Keypad = this.map { it.toList() }.flatMapIndexed { y, row ->
    row.mapIndexedNotNull { x, char ->
        char.takeIf { it != ' ' }?.let { char to Point(x, y) }
    }
}.toMap()
