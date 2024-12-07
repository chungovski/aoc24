import java.io.File
import java.nio.file.Files

fun main() {
    (1..25).forEach { generateDay(it) }

}

fun generateDay(dayNr: Int) {
    val day = dayNr.toString().padStart(2, '0')
    val dir = "src"
    writeFile(File(dir, "Day${day}.txt"), listOf());
    writeFile(File(dir, "Day${day}_test.txt"), listOf());
    writeFile(
        File(dir, "Day${day}.kt"), listOf(
            "fun main() {",
            "    solve(\"Day${day}_test.txt\", 99, 99)",
            "    solve(\"Day${day}.txt\", 99, 99)",
            "}",
            "",
            "private fun solve(path: String, part1: Int, part2: Int) {",
            "    val input = lines(path)",
            "    check(part1, 99)",
            "    check(part2, 99)",
            "}"
        )
    );
}

fun writeFile(file: File, body: List<String>) {
    if (!file.exists()) {
        Files.write(file.toPath(), body);
    }
}