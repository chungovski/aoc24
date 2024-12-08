data class Point(val x: Int, val y: Int) {
    fun move(p: Point): Point = move(p.x, p.y)

    fun moveOpposite(p: Point): Point = move(-p.x, -p.y)

    fun move(x: Int, y: Int): Point = Point(this.x + x, this.y + y)

    fun getDifference(p: Point): Point = Point(p.x - this.x, p.y - this.y)
}

