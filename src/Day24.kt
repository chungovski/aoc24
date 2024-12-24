fun main() {
    solve("Day24_test.txt", 2024L, null)
    solve("Day24_test2.txt", null, "z00,z01,z02,z05")
    solve("Day24.txt", 69201640933606L, "99")
}

private fun solve(path: String, part1: Long?, part2: String?) {
    val lines = lines(path)
    val device = lines.parseDevice()
    part1?.let { check(part1, part1(device)) }
    part2?.let { check(part2, part2(device)) }
}

private typealias Device = Pair<Wires, List<Gate>>
private typealias Wires = Map<String, Boolean>

private data class Gate(
    val firstInputKey: String, val type: GateType, val secondInputKey: String, val outputKey: String
)

private fun part1(device: Device): Long = device.resolveWires().first.sumBitsWithPrefix('z')

private fun part2(device: Device): String {
    val (wires, gates) = device
    val x = wires.sumBitsWithPrefix('x')
    val y = wires.sumBitsWithPrefix('y')

    return ""
}

private fun Device.resolveWires(): Device = buildMap {
    val (wires, gates) = this@resolveWires
    putAll(wires)
    val allOutputs = gates.map { it.outputKey }
    while (allOutputs.any { it !in this }) {
        gates.filter { it.outputKey !in this && it.firstInputKey in this && it.secondInputKey in this }
            .forEach {
                this[it.outputKey] = it.type.op(this[it.firstInputKey]!!, this[it.secondInputKey]!!)
            }
    }
} to this.second

private fun Wires.sumBitsWithPrefix(prefix: Char): Long =
    this.filterKeys { it.startsWith(prefix) }.toSortedMap()
        .values.fold("") { str, b -> "${b.toBinary()}$str" }
        .toLong(2)

private enum class GateType(val op: (Boolean, Boolean) -> Boolean) {
    AND({ a, b -> a && b }),
    OR({ a, b -> a || b }),
    XOR({ a, b -> a != b }),
}

private fun Boolean.toBinary(): String = if (this) "1" else "0"

private fun List<String>.parseDevice(): Device {
    val indexOfSeparator = this.indexOf("")
    return buildMap {
        this@parseDevice.subList(0, indexOfSeparator).forEach {
            it.split(": ").run { put(this[0], this[1].toInt() == 1) }
        }
    } to this.subList(indexOfSeparator + 1, this.size).map { l ->
        l.split(Regex("( -> | )")).let { Gate(it[0], GateType.valueOf(it[1]), it[2], it[3]) }
    }
}