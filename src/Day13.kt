fun main() {
    solve("Day13_test.txt", 480, 875318608908)
    solve("Day13.txt", 29201, 104140871044942)
}

private fun solve(path: String, part1: Long, part2: Long) {
    val lines = lines(path)
    val machines = lines.chunked(4).map {
        val (a, b, prize) = it
        Machine(
            a.extractPoint('+'),
            b.extractPoint('+'),
            prize.extractPoint('=')
        )
    }
    check(part1, part1(machines))
    check(part2, part2(machines))
}

private fun part1(machines: List<Machine>): Long = machines.sumOf { it.getCost() }

private fun part2(machines: List<Machine>): Long = machines.sumOf { it.getCost(10_000_000_000_000L) }

private typealias BigPoint = Pair<Long, Long>

private data class Machine(val a: BigPoint, val b: BigPoint, val prize: BigPoint) {
    private val costA = 3
    private val costB = 1

    fun getCost(offset: Long = 0): Long = calcCombinations(
        a, b, BigPoint(prize.first + offset, prize.second + offset)
    ).let { (coeffA, coeffB) -> coeffA * costA + coeffB * costB }
}

private fun calcCombinations(a: BigPoint, b: BigPoint, sum: BigPoint): Pair<Long, Long> {
    val (aX, aY) = a
    val (bX, bY) = b
    val (sumX, sumY) = sum

    val determinant = aX * bY - aY * bX
    val crossDiffA = sumX * bY - sumY * bX
    val crossDiffB = aX * sumY - aY * sumX

    // No combination possible
    if (determinant == 0L || crossDiffA % determinant != 0L || crossDiffB % determinant != 0L) {
        return 0L to 0L
    }

    return crossDiffA / determinant to crossDiffB / determinant
}

private fun String.extractPoint(separator: Char): Pair<Long, Long> =
    "X\\$separator([0-9]+), Y\\$separator([0-9]+)".toRegex().find(this)
        .let { it!!.groupValues[1].toLong() to it.groupValues[2].toLong() }
