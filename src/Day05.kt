fun main() {
    solve("Day05_test.txt", 143, 123)
    solve("Day05.txt", 5732, 4716)
}

private fun solve(path: String, part1: Int, part2: Int) {
    val lines = lines(path)
    val indexOfSeparator = lines.indexOf("")
    val rules = lines.subList(0, indexOfSeparator)
    val updates = lines.subList(indexOfSeparator + 1, lines.size)
    check(part1, part1(rules, updates))
    check(part2, part2(rules, updates))
}

private fun part1(rules: List<String>, updates: List<String>): Int = updates.filter {
    getRuleBreakingIndex(it, rules) == -1
}.sumOf { getCenterNo(it) }

private fun part2(rules: List<String>, updates: List<String>): Int = updates.filter {
    getRuleBreakingIndex(it, rules) != -1
}.map {
    var splitUpdates = splitInt(it, ",")
    while (true) {
        val wrongIndex = getRuleBreakingIndex(splitUpdates.joinToString(","), rules)
        if (wrongIndex == -1) break
        var correctIndex = wrongIndex + 1
        var lastUpdate = splitUpdates.toMutableList()
        while (correctIndex <= splitUpdates.size - 1 && getRuleBreakingIndex(
                lastUpdate.joinToString(","), rules
            ) == wrongIndex
        ) {
            lastUpdate = splitUpdates.toMutableList()
            lastUpdate[wrongIndex] = lastUpdate[correctIndex].also {
                lastUpdate[correctIndex] = lastUpdate[wrongIndex]
            }
            correctIndex++
        }
        val newWrongIndex = getRuleBreakingIndex(lastUpdate.joinToString(","), rules)
        if (newWrongIndex == -1 || newWrongIndex > wrongIndex) {
            splitUpdates = lastUpdate
        }
    }
    splitUpdates.joinToString(",")
}.sumOf { getCenterNo(it) }


private fun getRuleBreakingIndex(updates: String, rules: List<String>): Int {
    val splitUpdates = splitInt(updates, ",")
    val lastCompareIndex = splitUpdates.size - 2
    for (i in 0..lastCompareIndex) {
        for (j in i + 1..lastCompareIndex + 1) {
            if (!rules.contains("${splitUpdates[i]}|${splitUpdates[j]}")) {
                return i
            }
        }
    }
    return -1
}

private fun getCenterNo(updates: String): Int {
    val splitUpdates = splitInt(updates, ",")
    return splitUpdates[splitUpdates.size / 2]
}


