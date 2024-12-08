class Grid(var grid: Array<CharArray>) {

    companion object {
        const val OUT_OF_BOUND_CHAR = '☠'
    }

    constructor(lines: List<String>) : this(toChars(lines))

    fun rows(): Int = grid.size

    fun columns(): Int = grid[0].size

    fun getAllPoints(): List<Point> = buildList<Point> {
        grid.forEachIndexed { i, row ->
            row.forEachIndexed { j, Point ->
                add(Point(j, i))
            }
        }
    }

    fun inside(p: Point): Boolean {
        val validYRange = 0 until rows()
        val validXRange = 0 until columns()
        return p.y in validYRange && p.x in validXRange
    }

    fun find(c: Char): Point? = findAll(c).firstOrNull()

    fun findAll(c: Char): List<Point> = findAnyWith { c == it}.values.flatten()

    fun findAny(cs: List<Char>): Map<Char, Point> = findWith { char -> cs.any {char == it}  }

    fun findWith(predicate: (Char) -> Boolean): Map<Char, Point> = findAnyWith(predicate).map {
        it.key to it.value.first()
    }.toMap()

    fun findAnyWith(predicate: (Char) -> Boolean): Map<Char, List<Point>> = buildMap<Char, MutableList<Point>> {
        grid.forEachIndexed { i, row ->
            row.forEachIndexed { j, char ->
                if (predicate(char)) {
                    if(containsKey(char)) {
                        get(char)?.add(Point(j, i))
                    } else {
                        put(char, mutableListOf(Point(j, i)))
                    }
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

    fun copy(): Grid = Grid(Array(rows()) { rowIndex ->
        grid[rowIndex].clone() // Clone each inner array to ensure a deep copy
    })

    fun print() {
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                print(grid[i][j])
            }
            println()
        }
    }
}