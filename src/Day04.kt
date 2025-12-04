typealias Diagram = CharMatrix

fun Lines.toDiagram(): Diagram = toCharMatrix()

fun main() {
    fun Diagram.isAccessedRoll(roll: Position) =
        aroundOf(roll,allDirections).count { it in limits() && get(it)=='@' } < 4

    fun part1(input: List<String>): Int =
        with( input.toDiagram() ) {
            findAll('@').count { isAccessedRoll(it) }
        }

    fun part2(input: List<String>): Int {
        val diagram = input.toDiagram()
        val rollsPosition = diagram.findAll('@').toMutableSet()
        var counter = 0
        while (true) {
            val accessed = rollsPosition.filter { diagram.isAccessedRoll(it) }
            if (accessed.isEmpty()) break
            rollsPosition.removeAll(accessed)
            accessed.forEach { diagram[it] = '.' }
            counter += accessed.size
        }
        return counter
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 43)

    val input = readInput("Day04")
    part1(input).println() // 1564
    part2(input).println() // 9401
}
