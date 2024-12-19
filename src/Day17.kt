import kotlin.math.pow

fun main() {
    solve("Day17_test.txt", "4,6,3,5,6,3,5,2,1,0", null)
    solve("Day17.txt", "4,3,7,1,5,3,0,5,4", 190384615275535L)
    solve("Day17_2.txt", null, 117440L)
}

private fun solve(path: String, part1: String?, part2: Long?) {
    val lines = lines(path)
    val registers = Registers(lines.first().substringAfterLast(' ').toLong())
    val instructions = lines.last().substringAfterLast(' ')
        .split(',').map { it.toInt() }
    part1?.let { check(part1, part1(registers, instructions)) }
    part2?.let { check(part2, part2(instructions)) }
}

private fun part1(registers: Registers, instructions: List<Int>): String =
    registers.doInstruction(instructions).joinToString(",")

private fun part2(instructions: List<Int>): Long = findMatchingA(instructions, instructions)

private data class Registers(var a: Long, var b: Long = 0, var c: Long = 0)
private data class InstructionOutput(val registers: Registers, val pointer: Int? = null, val output: Int? = null)

private fun Registers.doInstruction(instructions: List<Int>): List<Int> = buildList {
    val chunked = instructions.chunked(2)
    var i = 0
    var changingRegisters = this@doInstruction
    while (i < chunked.size) {
        Instruction.from(chunked[i][0])?.let { instruction ->
            println("instruction is $instruction with registers $changingRegisters")
            val instructionOutput = instruction.operation(chunked[i][1], changingRegisters)
            changingRegisters = instructionOutput.registers
            instructionOutput.output?.let { add(it) }
            // Because of chunking we need to correct the pointer
            instructionOutput.pointer.let { i = it?.div(2) ?: (i + 1) }
        }

    }
}

private fun findMatchingA(instructions: List<Int>, target: List<Int>): Long {
    //value in register a changes every 8 instructions
    val start = if(target.size == 1) 0 else  8 * findMatchingA(instructions, target.subList(1, target.size))
    return generateSequence(start) { it + 1 }.first { Registers(it).doInstruction(instructions) == target }
}

// Disgusting but works
private enum class Instruction(val opcode: Int, val operation: (Int, Registers) -> InstructionOutput) {
    ADV(0, { i, r -> InstructionOutput(r.copy(a = (r.a / 2.0.pow(r.comboOperand(i).toDouble())).toLong())) }),
    BXL(1, { i, r -> InstructionOutput(r.copy(b = r.b.xor(i.toLong()))) }),
    BST(2, { i, r -> InstructionOutput(r.copy(b = r.comboOperand(i) % 8)) }),
    JNZ(3, { i, r -> if (r.a == 0L) InstructionOutput(r) else InstructionOutput(r, i) }),
    BXC(4, { _, r -> InstructionOutput(r.copy(b = r.b.xor(r.c))) }),
    OUT(5, { i, r -> InstructionOutput(r, null, (r.comboOperand(i) % 8).toInt()) }),
    BDV(6, { i, r -> InstructionOutput(r.copy(b = (r.a / 2.0.pow(r.comboOperand(i).toDouble())).toLong())) }),
    CDV(7, { i, r -> InstructionOutput(r.copy(c = (r.a / 2.0.pow(r.comboOperand(i).toDouble())).toLong())) });

    companion object {
        private val opcodeMap = entries.associateBy { it.opcode }
        infix fun from(code: Int) = opcodeMap[code]
    }
}

private fun Registers.comboOperand(operand: Int): Long = when (operand) {
    in 0..3 -> operand.toLong()
    4 -> this.a
    5 -> this.b
    6 -> this.c
    else -> 0 // Should never happen
}