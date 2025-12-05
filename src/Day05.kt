
fun LongRange.canCombineWith(b: LongRange) = (last + 1 >= b.first && b.last + 1 >= first)

operator fun LongRange.plus(b: LongRange) = minOf(first, b.first) .. maxOf(last, b.last)

fun main() {
    fun part1(input: List<String>): Int {
        val (input0,input1) = input.splitByEmptyLine()
        val freshRanges = input0.map {
            val (from,to) = it.split('-').map { it.toLong() }
            from..to
        }
        val ingredients = input1.map { it.toLong() }
        return ingredients.count { ing -> freshRanges.any{ ing in it } }
    }

    fun part2(input: List<String>): Long {
        val freshRanges = input.splitByEmptyLine().first().map {
            val (from,to) = it.split('-').map { it.toLong() }
            from..to
        }
        val ranges = mutableListOf<LongRange>()
        for (r in freshRanges) {
            var u = r
            for(idx in ranges.size-1 downTo 0)
                if (r.canCombineWith(ranges[idx])) {
                    u += ranges[idx]
                    ranges.removeAt(idx)
                }
            ranges.add(u)
        }
        return ranges.sumOf { it.last - it.first + 1 }
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 3)
    check(part2(testInput) == 14L)

    val input = readInput("Day05")
    part1(input).println() // 712
    part2(input).println() // 332998283036769
}
