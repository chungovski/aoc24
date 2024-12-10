fun main() {
    solve("Day09_test.txt", 1928L, 2858L)
    solve("Day09.txt", 6334655979668L, 6349492251099L)
}

private fun solve(path: String, part1: Long, part2: Long) {
    val input = readText(path)
    check(part1, part1(input))
    check(part2, part2(input))
}

private fun part1(input: String): Long = input.generateBlocks().defragment().checkSum()

private fun part2(input: String): Long = input.generateChunks().defragment().checkSum()

sealed class Block {
    data class File(val id: Int) : Block()
    data object Space : Block()
}

private data class Chunk(val content: Block, val length: Int)

private typealias Blocks = List<Block>
private typealias Files = List<Block.File>
private typealias Chunks = List<Chunk>

fun Blocks.checkSum(): Long = withIndex().sumOf { (i, block) ->
    when (block) {
        is Block.File -> block.id.toLong().times(i)
        else -> 0
    }
}

@JvmName("defragmentBlocks")
private fun Blocks.defragment(): Files {
    val filesFromBack = filterIsInstance<Block.File>().asReversed().toMutableList()
    val filesAmount = filesFromBack.size
    val result = mutableListOf<Block.File>()
    forEach { block ->
        if (block is Block.File) {
            result.add(block)
        } else {
            result.add(filesFromBack.first())
            filesFromBack.removeFirst()
        }
        if (result.size == filesAmount) {
            return result
        }
    }
    return result
}

@JvmName("defragmentChunks")
private fun Chunks.defragment(): Blocks {
    val swapFiles = filter { it.content is Block.File }.reversed().toMutableList()

    val filesAmount = swapFiles.size
    val result = mutableListOf<Block>()

    forEach { chunk ->
        if (chunk.content is Block.File && swapFiles.contains(chunk)) {
            repeat(chunk.length) { result.add(chunk.content) }
            swapFiles.remove(chunk)
        } else {
            var chunkSize = chunk.length
            while (chunkSize > 0) {
                swapFiles.find { it.length <= chunkSize }?.let { swap ->
                    repeat(swap.length) { result.add(swap.content) }
                    swapFiles.remove(swap)
                    chunkSize -= swap.length
                } ?: run {
                    repeat(chunkSize) { result.add(Block.Space) }
                    chunkSize = 0
                }
            }
        }
        if (result.size == filesAmount) {
            return result
        }
    }
    return result
}

private fun String.generateBlocks(): Blocks = buildList {
    toCharArray().forEachIndexed { i, char ->
        when (i % 2) {
            0 -> repeat(char.digitToInt()) { add(Block.File(i / 2)) }
            else -> repeat(char.digitToInt()) { add(Block.Space) }
        }
    }
}

private fun String.generateChunks(): Chunks = buildList {
    toCharArray().forEachIndexed { i, char ->
        when (i % 2) {
            0 -> add(Chunk(Block.File(i / 2), char.digitToInt()))
            else -> add(Chunk(Block.Space, char.digitToInt()))
        }
    }
}