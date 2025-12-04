import kotlin.time.measureTime

fun main() {
    fun String.maxJoltage(size: Int, from: Int, digits: String): Long? {
        if (size == 0) return digits.toLong()
        for (d in '9' downTo '1') {
            val idx = indexOf(d, from).takeIf { it != -1 } ?: continue
            return maxJoltage(size - 1, idx + 1, digits + d) ?: continue
        }
        return null
    }
    fun String.maxJoltage(size: Int) = maxJoltage(size,0,"") ?: error("Joltage not found")

    fun part1(input: List<String>) = input.sumOf { it.maxJoltage(2) }

    fun part2(input: List<String>) = input.sumOf { it.maxJoltage(12) }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 357L)
    check(part2(testInput) == 3121910778619)

    val input = readInput("Day03")
    measureTime { part1(input).println() }.println() // 16887
    measureTime { part2(input).println() }.println() // 167302518850275
}
