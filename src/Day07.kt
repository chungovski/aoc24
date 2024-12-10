fun main() {
    solve("Day07_test.txt", 3749L, 11387L)
    solve("Day07.txt", 1430271835320L, 456565678667482L)
}

private fun solve(path: String, part1: Long, part2: Long) {
    val lines = lines(path)
    val map = mutableMapOf<Long, MutableList<List<Long>>>().apply {
        lines.forEach { it ->
            split(it, ":").let {
                val key = it[0].toLong()
                val value = it[1].split(" ").map { it.toLong() }
                if (contains(key)) {
                    get(key)?.add(value)
                } else {
                    put(key, mutableListOf(value))
                }
            }
        }
    }
    check(part1, part1(map))
    check(part2, part2(map))
}

fun part1(map: Map<Long, List<List<Long>>>): Long {
    return map.map { (key, value) ->
        key.times(value.count {
            calcAllResults(
                it,
                listOf(OPERATOR.ADD, OPERATOR.TIMES)
            ).contains(key)
        })
    }.sumOf { it }
}

fun part2(map: Map<Long, List<List<Long>>>): Long {
    return map.map { (key, value) ->
        key.times(value.count {
            calcAllResults(
                it, OPERATOR.entries
            ).contains(key)
        })
    }.sumOf { it }
}

private fun calcAllResults(nrs: List<Long>, allowedOperators: List<OPERATOR>): List<Long> {
    if (nrs.size == 1) return nrs
    return generateExpression(nrs, allowedOperators).map { exp ->
        exp.foldIndexed(0L) { i, acc, el ->
            when {
                i == 0 -> el as Long
                el is Long -> (exp[i - 1] as OPERATOR).operation(acc, el)
                else -> acc
            }
        }
    }
}

private fun generateExpression(nrs: List<Long>, allowedOperators: List<OPERATOR>): List<List<Any>> {
    if (nrs.size == 1) return listOf(listOf(nrs.first()))
    return generateExpression(nrs.drop(1), allowedOperators).flatMap { acc ->
        allowedOperators.map { op -> listOf(nrs.first(), op) + acc }
    }
}

private enum class OPERATOR(val operation: (Long, Long) -> Long) {
    ADD({ a, b -> a + b }),
    TIMES({ a, b -> a * b }),
    CONCATENATE({ a, b -> "$a$b".toLong() }),
}
