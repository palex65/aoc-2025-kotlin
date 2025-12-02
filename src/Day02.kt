
fun main() {
    fun invalidIdsOf(range: LongRange): List<Long> =
        range.filter {
            val s = it.toString()
            s.length % 2 == 0 && s.drop(s.length/2)==s.take(s.length/2)
        }
    fun invalidIdsOf2(range: LongRange): List<Long> =
        range.filter {
            val s = it.toString()
            (2..s.length).any{ n ->
              if (s.length % n != 0) false
              else {
                  val size = s.length/n
                  val part = s.take(size)
                  s.drop(size).windowed(size,size).all { it == part }
              }
            }
        }
    fun String.toRange(): LongRange {
        val (from,to) = split('-').map { it.toLong() }
        return from..to
    }

    fun part1(input: List<String>): Long {
        val invalids = input.map{ it.toRange() }.flatMap { invalidIdsOf(it) }
        return invalids.sum()
    }

    fun part2(input: List<String>): Long {
        val invalids = input.map{ it.toRange() }.flatMap { invalidIdsOf2(it) }
        return invalids.sum()
    }

    val testInput = readInput("Day02_test",",")
    check(part1(testInput) == 1227775554L)
    check(part2(testInput) == 4174379265L)

    val input = readInput("Day02",",")
    part1(input).println() // 29940924880
    part2(input).println() // 48631958998
}
