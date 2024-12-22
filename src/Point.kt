import kotlin.math.abs

data class Point(val x: Int, val y: Int) {
    fun move(step: Point): Point = move(step.x, step.y)
    fun moveOpposite(step: Point): Point = move(-step.x, -step.y)
    fun move(stepX: Int, stepY: Int): Point = Point(this.x + stepX, this.y + stepY)
    fun getDifference(p: Point): Point = Point(p.x - this.x, p.y - this.y)
    fun getOrthogonalDistance(p: Point): Int = abs(p.x - this.x) + abs(p.y - this.y)
}