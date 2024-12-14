fun main() {
    solve("Day07_test.txt", 3749L, 11387L)
    solve("Day07.txt", 1430271835320L, 456565678667482L)
}

private fun solve(path: String, part1: Long, part2: Long) {
    val lines = lines(path)
    val map = mutableListOf<Pair<Long, List<Long>>>().apply {
        lines.forEach { it ->
            split(it, ":").let {
                val key = it[0].toLong()
                val value = it[1].split(" ").map { it.toLong() }
                add(key to value)
            }
        }
    }
    check(part1, part1(map))
    check(part2, part2(map))
}

private typealias ResultOperandsMap = List<Pair<Long, List<Long>>>

private fun part1(map: ResultOperandsMap): Long = map.sumOf { (key, value) ->
    if (calcAllResults(
            value, listOf(OPERATOR.ADD, OPERATOR.TIMES)
        ).contains(key)
    ) key else 0
}


private fun part2(map: ResultOperandsMap): Long = map.sumOf { (key, value) ->
    if (calcAllResults(
            value, OPERATOR.entries
        ).contains(key)
    ) key else 0
}

private fun calcAllResults(nrs: List<Long>, allowedOperators: List<OPERATOR>): List<Long> = when {
    nrs.size == 1 -> nrs
    else -> generateExpression(nrs, allowedOperators).map { exp ->
        exp.foldIndexed(0L) { i, acc, el ->
            when {
                i == 0 -> el as Long
                el is Long -> (exp[i - 1] as OPERATOR).operation(acc, el)
                else -> acc
            }
        }
    }
}

private fun generateExpression(nrs: List<Long>, allowedOperators: List<OPERATOR>): List<List<Any>> = when {
    nrs.size == 1 -> listOf(listOf(nrs.first()))
    else -> generateExpression(nrs.drop(1), allowedOperators).flatMap { acc ->
        allowedOperators.map { op -> listOf(nrs.first(), op) + acc }
    }
}

private enum class OPERATOR(val operation: (Long, Long) -> Long) {
    ADD({ a, b -> a + b }),
    TIMES({ a, b -> a * b }),
    CONCATENATE({ a, b -> "$a$b".toLong() }),
}
