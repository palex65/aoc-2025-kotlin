
fun main() {
    fun String.toRange(): LongRange =
        split('-').map { it.toLong() }.let { it[0]..it[1] }

    fun part1(input: List<String>): Long =
        input.map{ it.toRange() }.flatMap {
            it.filter {
                val s = it.toString()
                s.length % 2 == 0 && (s.length / 2).let { s.drop(it) == s.take(it) }
            }
        }.sum()

    fun part2(input: List<String>): Long =
        input.map{ it.toRange() }.flatMap {
            it.filter { with(it.toString()) {
                (2..length).any { n ->
                    if (length % n != 0) false
                    else {
                        val size = length / n
                        val part = take(size)
                        drop(size).chunked(size).all { it == part }
                    }
                }
            } }
        }.sum()

    val testInput = readInput("Day02_test",",")
    check(part1(testInput) == 1227775554L)
    check(part2(testInput) == 4174379265L)

    val input = readInput("Day02",",")
    part1(input).println() // 29940924880
    part2(input).println() // 48631958998
}
