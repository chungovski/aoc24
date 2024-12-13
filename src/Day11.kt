fun main() {
    solve("Day11_test.txt", 55312L, 65601038650482L)
    solve("Day11.txt", 199946L, 237994815702032L)
}

private fun solve(path: String, part1: Long, part2: Long) {
    val stones = splitLong(readText(path), " ")
    check(part1, part1(stones))
    check(part2, part2(stones))
}

private typealias Stone = Long
private typealias Stones = List<Long>
private val cache = mutableMapOf<Pair<Stone, Int>, Stone>()

fun part1(stones: Stones): Long = stones.blink(25)
fun part2(stones: Stones): Long = stones.blink(75)

private fun Stones.blink(times: Int): Long = this.sumOf { it.change(times) }

private fun Stone.change(times: Int): Stone = when {
    times == 0 -> 1L
    else -> cache.getOrPut(this to times) {
        val str = "$this"
        val oneLess = times - 1
        when {
            this == 0L -> 1L.change(oneLess)
            str.length % 2 == 0 -> str.chunked(str.length / 2)
                .sumOf { it.toLong().change(oneLess) }
            else -> 2024.times(this).change(oneLess)
        }
    }
}