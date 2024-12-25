fun main() {
    solve("Day25_test.txt", 3)
    solve("Day25.txt", 3284)
}

private fun solve(path: String, part1: Int) {
    val lines = lines(path)
    check(part1, part1(lines))
}

private fun part1(lines: List<String>): Int = lines.parseToSchematics().tryAll().count { it }

private typealias Schematics = Set<Schematic>

private data class Schematic(val type: SchematicType, val heights: IntArray) {
    fun fits(other: Schematic) =
        this.type != other.type &&
                this.heights.zip(other.heights) { a, b -> a + b < 6 }.all { it }
}

private enum class SchematicType {
    LOCK, KEY
}

private fun Schematics.tryAll(): List<Boolean> = flatMapIndexed { i, a ->
    drop(i + 1).map { b -> a.fits(b) }
}

private fun List<String>.parseToSchematics(): Schematics = buildSet {
    this@parseToSchematics.windowed(7, 8) {
        add(Schematic(
                if (it[0].contains('.')) SchematicType.KEY else SchematicType.LOCK,
                Grid(it.subList(1, it.size - 1)).columns().map { col -> col.count { char -> char == '#' } }.toIntArray()
            ))
    }
}
