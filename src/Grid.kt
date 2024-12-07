class Grid(var grid: Array<CharArray>) {

    companion object {
        const val OUT_OF_BOUND_CHAR = '☠'
    }

    constructor(lines: List<String>) : this(toChars(lines))

    fun rows(): Int = grid.size

    fun columns(): Int = grid[0].size

    fun Points(): List<Point> {
        val points: MutableList<Point> = ArrayList()
        for (y in grid.indices) {
            for (x in grid[y].indices) {
                points.add(Point(x, y))
            }
        }
        return points
    }

    fun inside(p: Point): Boolean {
        val validYRange = 0 until rows()
        val validXRange = 0 until columns()
        return p.y in validYRange && p.x in validXRange
    }

    fun find(c: Char): Point? = findAll(c).firstOrNull()

    fun findAll(c: Char): List<Point> = buildList<Point> {
        grid.forEachIndexed{ i, row ->
            row.forEachIndexed {j, Point ->
                if (Point == c) {
                    add(Point(j, i))
                }
            }
        }
    }

    fun findAny(cs: List<Char>): Map<Char, Point> {
        val finds = mutableMapOf<Char, Point>()
        for (i in grid.indices) {
            for (j in grid[0].indices) {
                var foundChar = '.'
                if (cs.any {
                        foundChar = it
                        it == grid[i][j]
                    }) {
                    finds[foundChar] = Point(j, i)
                }
            }
        }
        return finds
    }

    fun findInRow(y: Int, c: Char): Point? {
        for (x in 0..<columns()) {
            if (grid[y][x] == '.') {
                return Point(x, y)
            }
        }
        return null
    }

    fun get(x: Int, y: Int): Char =  grid[y][x]

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

    fun at(p: Point): Char = at(p.x, p.y)

    fun at(x: Int, y: Int): Char {
        if (y < 0 || y >= grid.size || x < 0 || x >= grid[0].size) {
            return '-'
        }
        return grid[y][x]
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