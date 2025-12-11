
fun main() {
    data class Machine(
        val lights: List<Boolean>,
        val buttons: List<List<Int>>,
        val joltage: List<Int>
    )
    fun String.toMachine() = split(' ').let{ parts -> Machine(
        lights = parts.first().drop(1).dropLast(1).map { it == '#' },
        joltage = parts.last().drop(1).dropLast(1).split(',').map { it.toInt() },
        buttons = parts.drop(1).dropLast(1).map { b ->
            b.drop(1).dropLast(1).split(',').map { it.toInt() }
        }
    ) }

    fun part1(input: List<String>): Int {
        data class State(val lights: List<Boolean>, val presses: Int)
        fun State.press(button: List<Int>) =
            State( lights.mapIndexed { idx,l -> if (idx in button) !l else l }, presses+1 )

        fun Machine.fewestPresses(): Int {
            val start = State( List(lights.size){ false }, 0 )
            val openStates = mutableListOf(start)
            while(true) {
                val state = openStates.removeFirst()
                val next = buttons.map { state.press(it) }
                next.find { it.lights == lights }?.let { return it.presses }
                openStates.addAll(next)
            }
        }

        val machines = input.map { it.toMachine() }
        return machines.sumOf { it.fewestPresses() }
    }

    fun part2(input: List<String>): Int {
        data class State(val counters: List<Int>, val presses: Int)
        fun State.press(button: List<Int>) =
            State( counters.mapIndexed { idx,c -> if (idx in button) c-1 else c }, presses+1 )

        fun Machine.fewestPresses(): Int {
            val closedStates = mutableSetOf<List<Int>>()
            val openStates = mutableListOf(State( joltage, 0 ) to joltage.sum()+joltage.max()*2)
            while(true) {
                val state = openStates.removeFirst().first
                closedStates.add(state.counters)
                var next = buttons.map { state.press(it) }
                next = next.filter { it.counters.all{ it >= 0 } }
                next = next.filter { it.counters !in closedStates }
                next = next.filter { openStates.firstOrNull { s -> it.counters == s.first.counters }==null }

                next.find { it.counters.sum() == 0 }?.let {
                    return it.presses
                }
                openStates.addAll(next.map { it to it.counters.sum()+it.counters.max()*2+it.presses })
                openStates.sortBy { it.second }
            }
        }

        val machines = input.map { it.toMachine() }
        return machines.sumOf { it.fewestPresses() }
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 33)

    val input = readInput("Day10")
    part1(input).println() // 401
    part2(input).println() // 15017
}
