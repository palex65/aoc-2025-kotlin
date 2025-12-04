fun main() {
    fun part1(input: List<String>): Int {
        var dial = 50
        return input.count {
            val value = it.drop(1).toInt()
            val offset = if (it.first()=='L') -value else +value
            dial = (dial+offset).mod(100)
            dial==0
        }
    }

    data class State(val dial: Int, val zeroes: Int)

    fun part2(input: List<String>): Int =
        input.fold(State(dial = 50, zeroes = 0)) { state, rotation ->
            val value = rotation.drop(1).toInt()
            val clicks = (if (rotation.first()=='L') -value else value) % 100
            State(
                dial = (state.dial + clicks).mod(100),
                zeroes = state.zeroes + value / 100 +
                  (state.dial != 0 && clicks < 0 && state.dial <= -clicks).toInt() +
                  (clicks > 0 && state.dial >= 100 - clicks).toInt()
            )
        }.zeroes

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 3)
    check(part2(testInput) == 6)

    val input = readInput("Day01")
    part1(input).println() // 1132
    part2(input).println() // 6623
}
