enum class DirectionChar(val direction: Direction, val char: Char) {
    UP(Direction.UP, '^'),
    RIGHT(Direction.RIGHT, '>'),
    DOWN(Direction.DOWN, 'v'),
    LEFT(Direction.LEFT, '<');

    companion object {
        private val charMap = entries.associateBy { it.char }
        private val directionMap = entries.associateBy { it.direction }
        infix fun from(char: Char) = charMap[char]
        infix fun from(dir: Direction) = directionMap[dir]
    }
}