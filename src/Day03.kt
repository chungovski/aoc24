fun main() {
    solve("Day03_test.txt", 161, 161)
    solve("Day03_test2.txt", 161, 48)
    solve("Day03.txt", 175700056, 71668682)
}

private fun solve(path: String, part1: Int, part2: Int) {
    val text = readText(path)
    check(part1, part1(text))
    check(part2, part2(text))
}

private fun part1(text: String): Int =
    """mul\(([0-9]{1,3}),([0-9]{1,3})\)""".toRegex().findAll(text).sumOf { productOfMul(it) }


private fun part2(text: String): Int {
    var active = true
    return """mul\(([0-9]{1,3}),([0-9]{1,3})\)|do\(\)|don't\(\)""".toRegex().findAll(text).filter {
        when (it.value) {
            "do()" -> {
                active = true
                false
            }
            "don't()" -> {
                active = false
                false
            }
            else -> active
        }
    }.sumOf { productOfMul(it) }
}


private fun productOfMul(mul: MatchResult): Int {
    val (x, y) = mul.destructured
    return x.toInt().times(y.toInt())
}