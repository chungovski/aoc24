fun main() {
    solve("Day22_test.txt", 37327623L, null)
    solve("Day22_test2.txt", null, 23)
    solve("Day22.txt", 17612566393L, 1968)
}

private fun solve(path: String, part1: Long?, part2: Int?) {
    val lines = lines(path).map { it.toLong() }
    part1?.let { check(part1, part1(lines)) }
    part2?.let { check(part2, part2(lines)) }
}

private fun part1(buyers: List<Long>): Long = buyers.sumOf { it.listSecretNrs().last() }

private fun part2(buyers: List<Long>): Int = buyers.flatMap {
    it.sequences().distinctBy { seq -> seq.first }
}.groupBy({ it.first }, { it.second })
    .maxOf { it.value.sum() }

typealias SequenceWithBanana = Pair<List<Int>, Int>

private fun Long.sequences(): List<SequenceWithBanana> = this.listSecretNrs()
    .map { it.lastDigit().toInt() }
    .zipWithNext { a, b -> (b - a) to b }
    .windowed(4) { changes ->
        changes.map { it.first } to changes.last().second
    }

private fun Long.listSecretNrs(amount: Int = 2000): List<Long> = buildList {
    add(this@listSecretNrs)
    for (i in 0 until amount) {
        add(get(lastIndex).times(64).mix(get(lastIndex)).prune()
            .let { it.floorDiv(32).mix(it).prune() }
            .let { it.times(2048).mix(it).prune() })
    }
}

private fun Long.lastDigit() = this % 10
private fun Long.mix(number: Long): Long = this.xor(number)
private fun Long.prune(): Long = this % 16777216