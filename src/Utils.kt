
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText

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
