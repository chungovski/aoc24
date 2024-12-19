enum class Direction(val point: Point) {
    RIGHT(Point(1, 0)),
    DIAGONAL_DOWN_RIGHT(Point(1, 1)),
    DOWN(Point(0, 1)),
    DIAGONAL_DOWN_LEFT(Point(-1, 1)),
    LEFT(Point(-1, 0)),
    DIAGONAL_UP_LEFT(Point(-1, -1)),
    UP(Point(0, -1)),
    DIAGONAL_UP_RIGHT(Point(1, -1)),
}

enum class Rotation {
    CLOCKWISE,
    COUNTERCLOCKWISE,
}

val orthogonalDirections = listOf(
    Direction.RIGHT, Direction.DOWN,
    Direction.LEFT, Direction.UP,
)

val oneRotationMap = mapOf(
    Direction.RIGHT to Pair(Direction.DOWN, Direction.UP),
    Direction.DOWN to Pair(Direction.LEFT, Direction.RIGHT),
    Direction.LEFT to Pair(Direction.UP, Direction.DOWN),
    Direction.UP to Pair(Direction.RIGHT, Direction.LEFT)
)

public fun Direction.turn(rotation: Rotation = Rotation.CLOCKWISE): Direction =
    oneRotationMap[this]?.let { (clockwiseDir, anticlockwiseDir) ->
        if (rotation == Rotation.CLOCKWISE) clockwiseDir else anticlockwiseDir
    } ?: Direction.UP // fallback should not happen