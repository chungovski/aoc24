import java.math.BigInteger

fun main() {
    solve("Day07_test.txt", BigInteger.valueOf(3749), BigInteger.valueOf(11387))
    solve("Day07.txt", BigInteger.valueOf(1430271835320), BigInteger.valueOf(456565678667482))
}

private fun solve(path: String, part1: BigInteger, part2: BigInteger) {
    val lines = lines(path)
    val map = mutableMapOf<BigInteger, MutableList<List<BigInteger>>>().apply {
        lines.forEach {
            split(it, ":").let {
                val key = it[0].toBigInteger()
                val value = it[1].split(" ").map { it.toBigInteger() }
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

fun part1(map: Map<BigInteger, List<List<BigInteger>>>): BigInteger {
    return map.map { (key, value) ->
        key.multiply(BigInteger.valueOf(value.count {
            calcAllResults(
                it,
                listOf(OPERATOR.ADD, OPERATOR.MULTIPLY)
            ).contains(key)
        }.toLong()))
    }.sumOf { it }
}

fun part2(map: Map<BigInteger, List<List<BigInteger>>>): BigInteger {
    return map.map { (key, value) ->
        key.multiply(BigInteger.valueOf(value.count {
            calcAllResults(
                it, OPERATOR.entries
            ).contains(key)
        }.toLong()))
    }.sumOf { it }
}

private fun calcAllResults(nrs: List<BigInteger>, allowedOperators: List<OPERATOR>): List<BigInteger> {
    if (nrs.size == 1) return nrs
    return generateExpression(nrs, allowedOperators).map { exp ->
        exp.foldIndexed(BigInteger.ZERO) { i, acc, el ->
            when {
                i == 0 -> el as BigInteger
                el is BigInteger -> (exp[i - 1] as OPERATOR).operation(acc, el)
                else -> acc
            }
        }
    }
}

private fun generateExpression(nrs: List<BigInteger>, allowedOperators: List<OPERATOR>): List<List<Any>> {
    if (nrs.size == 1) return listOf(listOf(nrs.first()))
    return generateExpression(nrs.drop(1), allowedOperators).flatMap { acc ->
        allowedOperators.map { op -> listOf(nrs.first(), op) + acc }
    }
}

private enum class OPERATOR(val operation: (BigInteger, BigInteger) -> BigInteger) {
    ADD({ a, b -> a + b }),
    MULTIPLY({ a, b -> a * b }),
    CONCATENATE({ a, b -> BigInteger("$a$b") }),
}
