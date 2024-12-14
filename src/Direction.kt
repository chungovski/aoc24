enum class Direction(val point: Point) {
    HORIZONTAL(Point(1, 0)),
    VERTICAL(Point(0, 1)),
    HORIZONTAL_BACKWARDS(Point(-1, 0)),
    VERTICAL_BACKWARDS(Point(0, -1)),
    DIAGONAL_DOWN_RIGHT(Point(1, 1)),
    DIAGONAL_UP_RIGHT(Point(1, -1)),
    DIAGONAL_DOWN_LEFT(Point(-1, 1)),
    DIAGONAL_UP_LEFT(Point(-1, -1)),
}

val orthogonalDirections = listOf(
    Direction.VERTICAL, Direction.HORIZONTAL,
    Direction.VERTICAL_BACKWARDS, Direction.HORIZONTAL_BACKWARDS
)