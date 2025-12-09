

fun main() {
    fun part1(input: List<String>): Int {
        val diagram = input.toCharMatrix()
        val splitters = diagram.findAll('^').groupBy { it.row }

        class State(val beams: Set<Int>, val splits: Int)
        val startState = State(beams = setOf(diagram.find('S').col), splits = 0)
        val endState = (1..diagram.lastIndex).fold( startState ) { state, row ->
            val splittersInRow = splitters[row]?.map { it.col } ?: emptyList()
            val (split, pass) = state.beams.partition{ it in splittersInRow }
            State((split.flatMap { listOf(it-1,it+1) } + pass).toSet(), state.splits+split.size )
        }
        return endState.splits
    }

    fun part2(input: List<String>): Long {
        val diagram = input.toCharMatrix()
        val splitters = diagram.findAll('^').groupBy { it.row }

        class Beam(val col: Int, val timeline: Long)
        val beams = (1..diagram.lastIndex).fold( listOf( Beam(diagram.find('S').col,1) ) ) { beams, row ->
            val splittersInRow = splitters[row]?.map { it.col } ?: emptyList()
            val split = beams.filter{ it.col in splittersInRow }
            split.fold( beams ) { beams, b ->
                fun List<Beam>.processSplit(col: Int) = find { it.col == col }
                    ?.let { minus(it) + Beam(col, b.timeline + it.timeline) }
                    ?: plus(Beam(col, b.timeline))
                beams.processSplit(b.col-1).processSplit(b.col+1) - b
            }
        }
        return beams.sumOf { it.timeline }
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 40L)

    val input = readInput("Day07")
    part1(input).println() // 1553
    part2(input).println() // 15811946526915
}
