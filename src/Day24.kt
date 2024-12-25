fun main() {
    solve("Day24_test.txt", 2024L, null)
    solve("Day24_test2.txt", null, "z00,z01,z02,z05")
    solve("Day24.txt", 69201640933606L, "dhq,hbs,jcp,kfp,pdg,z18,z22,z27")
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
    val firstInputKey: String, val type: GateType, val secondInputKey: String, var outputKey: String
)

private fun part1(device: Device): Long = device.resolveWires()

// Broken for test data somehow
private fun part2(device: Device): String {
    val (wires, gates) = device

    // Non-XOR gates that output to Z wires (except last one).
    val lastZWire = gates.filter { it.outputKey.startsWith('z') }
        .maxBy { it.outputKey.drop(1).toInt() }.outputKey
    val nonXorOutGates =
        gates.filter {
            it.type != GateType.XOR &&
                    it.outputKey.startsWith('z') &&
                    it.outputKey != lastZWire
        }

    // XOR intermediary gates that do not output to Z or accept X,Y inputs.
    val xorIntermediaries = gates.filter { gate ->
        gate.type == GateType.XOR && !gate.outputKey.startsWith('z') &&
                setOf(gate.firstInputKey, gate.secondInputKey).none {
                    it.startsWith('x') || it.startsWith('y')
                }
    }

    // The last swap is determined by a mismatched carry bit.
    // Do the swaps between the gates detected earlier, and simulate - It will be correct up until a bit.
    // The position of the error tells us where to look, they must be exactly two gates wired to X and Y followed by
    // the error position, that point to two intermediary gates.
    val falseCarryGates = run {
        val swappedGates = buildList {
            addAll(gates)
            for (a in xorIntermediaries) {
                val b = nonXorOutGates.first { it.outputKey == gates.firstZThatUsesC(a.outputKey) }
                removeAll(setOf(a, b))
                addAll(setOf(a.copy(outputKey = b.outputKey), b.copy(outputKey = a.outputKey)))
            }
        }

        val expectedResult = wires.sumBitsWithPrefix('x').plus(wires.sumBitsWithPrefix('y'))
        val actualResult = (wires to swappedGates).resolveWires()
        val falseCarryBit = (expectedResult xor actualResult).countTrailingZeroBits().toString().padStart(2, '0')

        swappedGates.filter {
            it.firstInputKey.endsWith(falseCarryBit) &&
                    it.secondInputKey.endsWith(falseCarryBit)
        }
    }

    return (nonXorOutGates + xorIntermediaries + falseCarryGates)
        .map { it.outputKey }.sorted().joinToString(",")
}

private fun List<Gate>.firstZThatUsesC(c: String): String? {
    val x = filter { it.firstInputKey == c || it.secondInputKey == c }
    x.find { it.outputKey.startsWith('z') }?.let {
        return "z" + (it.outputKey.drop(1).toInt() - 1).toString().padStart(2, '0')
    }
    return x.firstNotNullOfOrNull { firstZThatUsesC(it.outputKey) }
}

private fun Device.resolveWires(): Long = buildMap {
    val (wires, gates) = this@resolveWires
    putAll(wires)
    fun traverse(curr: String): Boolean = gates.find { it.outputKey == curr }?.let {
        it.type.op(getOrPut(it.firstInputKey) { traverse(it.firstInputKey) },
            getOrPut(it.secondInputKey) { traverse(it.secondInputKey) }
        )
    } ?: false
    gates.forEach { set(it.outputKey, traverse(it.outputKey)) }
}.sumBitsWithPrefix('z')

private fun Wires.sumBitsWithPrefix(prefix: Char): Long =
    this.filterKeys { it.startsWith(prefix) }.toSortedMap()
        .values.fold("") { str, b -> "${b.toBinary()}$str" }
        .toLong(2)

private enum class GateType(val op: (Boolean, Boolean) -> Boolean) {
    AND({ a, b -> a and b }),
    OR({ a, b -> a or b }),
    XOR({ a, b -> a xor b }),
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