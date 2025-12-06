

fun main() {
    data class Problem(val oper: Char, val values:List<Int>)
    fun Problem.result(): Long = when(oper) {
        '+' -> values.sum().toLong()
        else -> values.fold(1L) { acc, i -> acc * i }
    }

    fun part1(input: List<String>): Long {
        val numbers = input.dropLast(1)
            .map{ it.split(' ').filter{ it.isNotBlank() }.map{ it.toInt() }}
        val opers = input.last().split(' ').filter { it.isNotBlank() }.map{ it[0] }
        val problems = opers.indices.map{ p -> Problem(opers[p],List(numbers.size){ numbers[it][p]})}
        return problems.sumOf{ it.result() }
    }


    fun part2(input: List<String>): Long {
        val maxLen = input.maxOf{ it.length }
        val columns = input.last().mapIndexedNotNull { idx, c -> if (c!=' ') idx else null }
        val numbers = input.dropLast(1).map { line ->
            columns.mapIndexed { idx, col ->
                if (idx==columns.lastIndex) "$line   ".substring(col,maxLen)
                else line.substring(col, columns[idx+1]-1)
            }
        }
        val opers = columns.map { input.last()[it] }
        val problems = opers.indices.map{ p ->
            Problem(opers[p], List(numbers[0][p].length){ n ->
                numbers.indices.joinToString("") { numbers[it][p][n].toString() }.trim().toInt()
            } )
        }
        return problems.sumOf { it.result() }
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 4277556L)
    check(part2(testInput) == 3263827L)

    val input = readInput("Day06")
    part1(input).println() // 3785892992137
    part2(input).println() // 7669802156452
}
