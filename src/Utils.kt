import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun lines(name: String) = readText(name).trim().lines()

fun readText(name: String) = try {
    Path("src/$name").readText()
} catch (e: IOException) {
    throw RuntimeException(e)
}

fun check(expected: Any, actual: Any) = require(expected == actual) { "Expected $expected but was $actual" }

fun linesSplit(path: String, delimiter: String): List<List<String>> = split(path, delimiter, ::split)

fun linesSplitInt(path: String, delimiter: String): List<List<Int>> = split(path, delimiter, ::splitInt)

fun linesSplitLong(path: String, delimiter: String): List<List<Long>> = split(path, delimiter, ::splitLong)

fun <T> split(path: String, delimiter: String, mapper: (String, String) -> List<T>): List<List<T>> =
    lines(path).map { l -> mapper(l, delimiter) }

fun split(str: String, delimiter: String): List<String> = split(str, delimiter) { s: String -> s }

fun splitInt(str: String, delimiter: String): List<Int> = split(str, delimiter) { s: String -> s.toInt() }

fun splitLong(str: String, delimiter: String): List<Long> = split(str, delimiter) { s: String -> s.toLong() }

fun <T> split(str: String, delimiter: String, mapper: (String) -> T): List<T> =
    str.trim().split(delimiter.toRegex()).filter { it.isNotEmpty() }.map { mapper(it.trim()) }

/**
 * Converts a list of lines into a char-grid.
 */
fun toChars(lines: List<String>): Array<CharArray> = lines.map { it.toCharArray() }.toTypedArray()

/**
 * Converts a list of lines with Points to a list of columns.
 */
fun <T> toColumns(lines: List<List<T>>): List<MutableList<T>> {
    val cols = lines[0].size // Same columns in all lines
    return List(cols) { colIndex ->
        lines.map { it[colIndex] }.toMutableList()
    }
}

/**
 * Create all possible permutations of the elements in a list.
 */
fun <T> Collection<T>.permutations(): List<Collection<T>> = if (size == 1) listOf(this)
else flatMap { i -> (this - i).permutations().map { listOf(i) + it } }


fun String.permutations(): List<String> = toList().permutations().map { it.joinToString("") }

infix fun Set<*>.or(other: Set<*>): Set<*> = this.union(other)
infix fun Set<*>.xor(other: Set<*>): Set<*> = (this or other).minus(this.intersect(other))

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16).padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)
