import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int {
        var point = 50
        return input.count {
            val ofs = it.drop(1).toInt()
            val offset =if (it.first()=='L') -ofs else +ofs
            point = (point+offset).mod(100)
            point==0
        }
    }

    fun part2(input: List<String>): Int {
        val clicks = input.map{
            val ofs = it.drop(1).toInt()
            if (it.first()=='L') -ofs else +ofs
        }
        var zeroes = clicks.sumOf { it.absoluteValue / 100 }
        var point = 50
        for(offset in clicks) {
            val n = offset % 100
            if (point!=0) {
                if (n < 0 && point <= -n) zeroes++
                if (n > 0 && point + n >= 100) zeroes++
            }
            point = (point + n).mod(100)
        }
        return zeroes
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 3)
    check(part2(testInput) == 6)

    val input = readInput("Day01")
    part1(input).println() // 1132
    part2(input).println() // 6623
}
