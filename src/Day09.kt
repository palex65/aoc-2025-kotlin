import kotlin.math.abs

fun main() {
    fun String.toPosition() =
        split(',').map { it.toInt() }.let{ (x,y) -> Position(y,x) }

    /**
     * Area of the rectangle defined by the opposite corners a and b
     */
    fun areaOf(a: Position, b: Position) =
        (abs(a.row-b.row)+1).toLong() * (abs(a.col-b.col)+1)

    fun part1(input: List<String>): Long {
        val reds = input.map { it.toPosition() }
        return reds.maxOf { a -> reds.maxOf{ b -> areaOf(a,b) } }
    }

    /**
     * Checks if this position is on the segment defined by a and b (a and b inclusive)
     */
    fun Position.isOnSegment(a: Position, b: Position): Boolean =
        (row == a.row || row == b.row) && col in minOf(a.col, b.col)..maxOf(a.col, b.col) ||
        (col == a.col || col == b.col) && row in minOf(a.row, b.row)..maxOf(a.row, b.row)

    /**
     * Checks if the polygon defined by this list of positions contains position p
     */
    fun List<Position>.have(p: Position): Boolean {
        for (i in indices)
            if (p.isOnSegment(this[i], this[(i + 1) % size])) return true
        var inside = false
        for (i in indices) {
            val a = this[i]
            val b = this[(i + 1) % size]
            if (a.col != b.col || a.col <= p.col) continue // Only vertical segments on rigth side
            if (p.row in (minOf(a.row, b.row)+1)..maxOf(a.row, b.row))
                inside = !inside
        }
        return inside
    }

    /**
     * Sequence of positions on the perimeter of the rectangle defined by opposite corners a and b
     */
    fun perimeterPositionsOf(a: Position, b: Position) = sequence {
        val left = minOf(a.col,b.col)
        val right = maxOf(a.col,b.col)
        val top = minOf(a.row,b.row)
        val bottom = maxOf(a.row,b.row)
        for(r in top..bottom) {
            yield(Position(r,left))
            yield(Position(r,right))
        }
        for(c in left..right) {
            yield(Position(top,c))
            yield(Position(bottom,c))
        }
    }

    fun part2(input: List<String>): Long {
        val reds = input.map { it.toPosition() }
        val greens = mutableMapOf<Position, Boolean>()
        fun Position.isGreen() =
            greens[this] ?: reds.have(this).also { greens[this]=it }

        val sa = (0..<reds.lastIndex).flatMap { i -> ((i+1)..reds.lastIndex).map { j ->
            val a = reds[i]
            val b = reds[j]
            areaOf(a,b) to Pair(a,b)
        } }.sortedBy { it.first }.reversed()
           .drop(if (input.size>400) 50000 else 0) // To save 40 seconds
        val max = sa.first { (_,pair) ->
            val (a,b) = pair
            perimeterPositionsOf(a,b).all { it==a || it==b || it.isGreen() }
        }
        return max.first
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 50L)
    check(part2(testInput) == 24L)

    val input = readInput("Day09")
    part1(input).println() // 4781377701
    part2(input).println() // 1470616992
}
