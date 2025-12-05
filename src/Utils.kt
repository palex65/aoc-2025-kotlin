
import kotlin.io.path.Path
import kotlin.io.path.readLines

typealias Lines = List<String>

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

fun readInput(name: String, separator: String) = readInput(name)
    .flatMap { it.split(separator) }.filter { it.isNotEmpty() }

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/**
 * Converts true to 1 (one) and false to 0 (zero)
 */
fun Boolean.toInt() = if (this) 1 else 0

/**
 * Splits a list into multiple lists separated by the elements that match the predicate.
 */
fun <T> List<T>.splitBy( predicate: (T)->Boolean ): List<List<T>> = buildList {
    val input = this@splitBy
    var from = 0
    var to = 0
    while (to < input.size) {
        while(to < input.size && !predicate(input[to])) ++to
        add(input.subList(from, to))
        from = ++to
    }
}

/**
 * Splits a list of lines into multiple lists separated by empty lines.
 * Use: val (part1, part2, part3) = readInput("DayXX").splitByEmptyLine()
 */
fun Lines.splitByEmptyLine(): List<Lines> = splitBy { it.isEmpty() }
