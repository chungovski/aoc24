fun main() {
    solve("Day14_test.txt", 12, 7)
    solve("Day14.txt", 223020000, 2187)
}

private fun solve(path: String, part1: Int, part2: Int) {
    val splitLines = linesSplitInt(path, "p=|,| v=")
    check(part1, part1(splitLines))
    check(part2, part2(splitLines))
}

private fun part1(lines: List<List<Int>>): Int = lines.parseBathroom().also {
    it.moveGuards(100)
}.getSafetyFactor()

private fun part2(lines: List<List<Int>>): Int = lines.parseBathroom().let { initialRoom ->
    return generateSequence(initialRoom to 0) { (bathroom, s) ->
        bathroom.moveGuards()
        // if there is no guards overlapping for maximum christmasness
        if (bathroom.guards.map { it.position }.toSet().size == bathroom.guards.size) {
            println("""${s + 1}-seconds-elapsed${"-".repeat(bathroom.width)}""")
            bathroom.print()
        }
        bathroom to s + 1
    }.takeWhile { it.second < 10000 }.minBy { it.first.getSafetyFactor() }.second
}

private data class Guard(var position: Point, val velocity: Point) {
    fun belongsToQuadrant(width: Int, height: Int): QUADRANT {
        val widthCutOff = width / 2
        val heightCutOff = height / 2
        return when {
            this.position.x < widthCutOff && this.position.y < heightCutOff -> QUADRANT.FIRST
            this.position.x > widthCutOff && this.position.y < heightCutOff -> QUADRANT.SECOND
            this.position.x < widthCutOff && this.position.y > heightCutOff -> QUADRANT.THIRD
            this.position.x > widthCutOff && this.position.y > heightCutOff -> QUADRANT.FOURTH
            else -> QUADRANT.NONE
        }
    }
}

private data class Bathroom(val width: Int, val height: Int, val guards: List<Guard>) {
    fun moveGuards(times: Int = 1) {
        for (s in 1..times) {
            guards.forEach {
                it.position = it.position.move(it.velocity).teleportedPoint(width, height)
            }
        }
    }

    fun getSafetyFactor(): Int = getQuadrants().filter { it.key != QUADRANT.NONE }
        .values.fold(1) { acc, guards -> acc * guards.size }

    fun print() {
        for (y in 0..height) {
            buildString {
                for (x in 0..width) {
                    append(if (guards.any { it.position.x == x && it.position.y == y }) "X" else " ")
                }
            }.let { it.println() }
        }
    }

    private fun getQuadrants(): Map<QUADRANT, List<Guard>> =
        this.guards.groupBy { it.belongsToQuadrant(width, height) }
}

private enum class QUADRANT { NONE, FIRST, SECOND, THIRD, FOURTH }

private fun List<List<Int>>.parseBathroom(): Bathroom {
    val guards = buildList<Guard> {
        this@parseBathroom.forEach { add(Guard(Point(it[0], it[1]), Point(it[2], it[3]))) }
    }
    return Bathroom(
        guards.map { it.position.x }.max() + 1,
        guards.map { it.position.y }.max() + 1,
        guards
    )
}

fun Point.teleportedPoint(width: Int, height: Int): Point =
    Point(this.x.wrapAround(width), this.y.wrapAround(height))

fun Int.wrapAround(bound: Int) = (this + bound) % bound