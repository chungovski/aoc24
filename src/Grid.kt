class Grid(var grid: Array<CharArray>, var emptyChar: Char = '.') {

    companion object {
        const val OUT_OF_BOUND_CHAR = '☠'
        const val DEFAULT_EMPTY_CHAR = '.'
        fun createWithSize(width: Int, height: Int, emptyChar: Char = DEFAULT_EMPTY_CHAR): Grid =
            Grid(Array<CharArray>(height + 1) { CharArray(width + 1) { emptyChar } }, emptyChar)
    }

    constructor(lines: List<String>) : this(toChars(lines))

    fun rows(): Array<CharArray> = grid
    fun columns(): Array<CharArray> = Array(grid[0].size) { x ->
        CharArray(grid.size) { y ->
            grid[y][x]
        }
    }

    private fun rowSize(): Int = grid.size

    private fun columnSize(): Int = grid[0].size

    fun getAllPoints(): List<Pair<Point, Char>> = buildList {
        grid.forEachIndexed { y, row ->
            row.forEachIndexed { x, char ->
                add(Point(x, y) to char)
            }
        }
    }

    fun inside(p: Point): Boolean = checkRange(
        p,
        0 until rowSize(),
        0 until columnSize()
    )

    fun isBorderVertical(p: Point): Boolean =
        checkRange(p, 0, 0 until columnSize()) ||
                checkRange(p, rowSize(), 0 until columnSize())

    fun isBorderHorizontal(p: Point): Boolean =
        checkRange(p, 0 until rowSize(), 0) ||
                checkRange(p, 0 until rowSize(), columnSize())

    fun isBorder(p: Point): Boolean = isBorderVertical(p) || isBorderHorizontal(p)


    fun isCorner(p: Point): Boolean =
        (p.y == 0 && p.x == 0) ||
                (p.y == 0 && p.x == columnSize()) ||
                (p.y == rowSize() && p.x == 0) ||
                (p.y == rowSize() && p.x == columnSize())

    private fun checkRange(p: Point, yRange: IntRange, xRange: IntRange): Boolean =
        p.y in yRange && p.x in xRange

    private fun checkRange(p: Point, y: Int, xRange: IntRange): Boolean =
        p.y == y && p.x in xRange

    private fun checkRange(p: Point, yRange: IntRange, x: Int): Boolean =
        p.y in yRange && p.x == x


    fun find(c: Char): Point? = findAll(c).firstOrNull()

    fun findAll(c: Char): List<Point> = findAnyWith { c == it }.values.flatten()

    fun findAny(cs: List<Char>): Map<Char, Point> = findWith { char -> cs.any { char == it } }

    fun findWith(predicate: (Char) -> Boolean): Map<Char, Point> =
        findAnyWith(predicate).map {
            it.key to it.value.first()
        }.toMap()

    fun getDistinct(): List<Char> = grid.flatMap { it.distinct() }

    fun findAnyWith(predicate: (Char) -> Boolean): Map<Char, List<Point>> =
        buildMap<Char, MutableList<Point>> {
            getAllPoints().forEach { (point, char) ->
                if (predicate(char)) {
                    if (containsKey(char)) {
                        get(char)?.add(point)
                    } else {
                        put(char, mutableListOf(point))
                    }
                }
            }
        }

    fun get(x: Int, y: Int): Char = grid[y][x]

    fun get(p: Point): Char {
        try {
            return grid[p.y][p.x]
        } catch (e: ArrayIndexOutOfBoundsException) {
            return OUT_OF_BOUND_CHAR
        }
    }

    fun set(x: Int, y: Int, c: Char) {
        grid[y][x] = c
    }

    fun set(p: Point, c: Char) {
        grid[p.y][p.x] = c
    }

    fun copy(): Grid = Grid(Array(rowSize()) { rowIndex ->
        grid[rowIndex].clone() // Clone each inner array to ensure a deep copy
    })

    fun print() {
        for (i in grid.indices) {
            buildString {
                for (j in grid[i].indices) {
                    append(grid[i][j])
                }
            }.let { it.println() }
        }
    }

    fun emptyChar() = emptyChar
}