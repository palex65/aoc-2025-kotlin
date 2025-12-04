/**
 * 2D data structures and operations
 * Types: Position, Direction, Map2D, Limits
 */

data class Position(val row: Int, val col: Int) {
    override fun toString() = "($row, $col)"
}

data class Direction(val dRow: Int, val dCol: Int) {
    companion object {
        val UP = Direction(-1, 0)
        val DOWN = Direction(1, 0)
        val LEFT = Direction(0, -1)
        val RIGHT = Direction(0, 1)
        val UP_LEFT = Direction(-1, -1)
        val UP_RIGHT = Direction(-1, 1)
        val DOWN_LEFT = Direction(1, -1)
        val DOWN_RIGHT = Direction(1, 1)
    }
}

/**
 * All Directions in the order of rotate 45º
 */
val allDirections = listOf(
    Direction.UP, Direction.UP_RIGHT, Direction.RIGHT, Direction.DOWN_RIGHT,
    Direction.DOWN, Direction.DOWN_LEFT, Direction.LEFT, Direction.UP_LEFT
)
/**
 * Orthogonal Directions in the order of rotate 90º
 */
val orthogonalDirections = listOf(
    Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT
)

/**
 * Returns a new position by adding a direction.
 * Use: pos2 = pos1 + dir
 */
operator fun Position.plus(dir: Direction) = Position(row + dir.dRow, col + dir.dCol)

/**
 * Returns all positions around a central position.
 * @param p the central position
 * @param dirs the directions to consider around (default is orthogonal)
 */
fun aroundOf(p: Position, dirs: List<Direction> =orthogonalDirections) = dirs.map { p + it }

/**
 * 2D map type for immutable information.
 */
typealias Map2D<T> = List<List<T>>

/**
 * Returns all positions of a 2D map from top to bottom and left to right.
 */
fun <T> Map2D<T>.allPositions(): Sequence<Position> = sequence {
    for (row in indices) for (col in get(row).indices) yield(Position(row, col))
}

/**
 * Returns one element of a 2D map by its position.
 * @throws IndexOutOfBoundsException if the position is out of bounds
 * Use: val elem = map[pos]
 */
operator fun <T> Map2D<T>.get(p: Position) = get(p.row)[p.col]

/**
 * Verifies if a position is inside the 2D map.
 * Use: if (pos in map) ...
 */
operator fun <T> Map2D<T>.contains(p: Position) = p.row in indices && p.col in get(p.row).indices

/**
 * Returns one element of a 2D map by its position or null if the position is out of bounds.
 * Use: val elem = map.getOrNull(pos)
 */
fun <T> Map2D<T>.getOrNull(p: Position) = if (p in this) get(p) else null

/**
 * Limits of a rectangular map
 */
data class Limits(val rows: Int, val cols:Int)

/**
 * Verifies if a position is inside the limits.
 * Use: if (pos.inBounds(limits)) ...
 */
fun Position.inBounds(limits: Limits) = row in 0..<limits.rows && col in 0..<limits.cols

/**
 * Verifies if a position is inside the limits.
 * Use: if (pos in limits) ...
 */
operator fun Limits.contains(p: Position) = p.inBounds(this)

/**
 * Returns all positions of a rectangle defined by the limits.
 * Iterating from top to bottom and left to right.
 */
fun Limits.allPositions(): Sequence<Position> = sequence {
    for (row in 0..<rows) for (col in 0..<cols) yield(Position(row, col))
}

/**
 * Executes a block for each position of a rectangle defined by the limits.
 */
inline fun Limits.forEachPosition(block: (Position) -> Unit) =
    allPositions().forEach(block)


/**
 * Char matrix type for mutable chars
 */
typealias CharMatrix = List<CharArray>

/**
 * Converts a list of strings to a char matrix.
 */
fun Lines.toCharMatrix(): CharMatrix = map { it.toCharArray() }

/**
 * Gets or sets a char in a char matrix by its position.
 * Use: val c = matrix[pos]
 * Use: matrix[pos] = c
 */
operator fun CharMatrix.get(p: Position): Char = this[p.row][p.col]
operator fun CharMatrix.set(p: Position, c: Char) { this[p.row][p.col] = c }

/**
 * Returns the position of the first char occurrence in a char matrix.
 * @throws IllegalStateException if the char is not found
 */
fun CharMatrix.find(c: Char): Position {
    Limits(size, this[0].size).forEachPosition { p ->
        if (this[p] == c) return p
    }
    error("Not found $c")
}

/**
 * Returns all positions of a char occurrences in a char matrix.
 */
fun CharMatrix.findAll(c: Char): List<Position> = buildList {
    val m = this@findAll
    Limits(m.size, m[0].size).forEachPosition { p ->
        if (m[p] == c) add(p)
    }
}

fun CharMatrix.limits() = Limits(size,get(0).size)




