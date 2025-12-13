import kotlin.math.abs

typealias Matrix = Array<IntArray>

tailrec fun gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)

fun Matrix.checkSolution(sol: IntArray): Boolean = all{ row ->
    sol.indices.sumOf { row[it] * sol[it] } == row.last()
}

fun Matrix.gaussElimination(): Matrix {
    val rows = size
    val cols = this[0].size
    val a = Array(rows) { this[it].clone() }

    var r = 0
    for (c in 0..<cols) {
        // Find pivot
        var pivot = r
        while (pivot < rows && a[pivot][c] == 0) pivot++
        if (pivot == rows) continue  // Not found in this column
        // Swap pivot row
        if (r!=pivot)
            a[r] = a[pivot].also { a[pivot] = a[r] }
        // Put zeros below pivot
        for (i in (r + 1)..<rows) if (a[i][c] != 0) {
            val ai = a[i][c]
            val ar = a[r][c]
            val g = gcd(abs(ai), abs(ar))
            val mi = ar / g  // Line i multiplier
            val mr = ai / g  // Line r multiplier
            for (j in c..<cols)
                a[i][j] = a[i][j] * mi - a[r][j] * mr
        }
        r++
        if (r == rows) break
    }
    return a
}

fun Matrix.findBestSolutions(freeVars: List<Int>, ranges: List<IntRange>): List<Int> {
    val cols = this[0].size - 1
    var bestSum = Int.MAX_VALUE
    var result = emptyList<Int>()

    fun process(current: IntArray) {
        val fixed = BooleanArray(cols) { it in freeVars }
        repeat(cols - freeVars.size) {
            val fixedCols = fixed.indices.filter { fixed[it] } + cols
            var col = -1
            val row = first { r ->
                val idxs = r.indices.filter { r[it] != 0 } - fixedCols
                if (idxs.size == 1) {
                    col = idxs[0]; true
                } else false
            }
            var sum = row.last()
            for (j in 0..<cols)
                if (j != col) sum -= row[j] * current[j]
            current[col] = sum / row[col]
            if (current[col] < 0) return // invalid solution
            fixed[col] = true
        }
        if (this.checkSolution(current)) {
            val sum = current.sum()
            if (sum < bestSum) {
                bestSum = sum
                result = current.toList()
            }
        }
    }

    fun recurse(idx: Int, current: IntArray) {
        if (idx == freeVars.size)
            process(current)
        else
            for (v in ranges[idx]) {
                current[freeVars[idx]] = v
                recurse(idx + 1, current)
            }
    }

    recurse(0, IntArray(cols))
    return result
}

fun pivotsOf(mat: Matrix): List<Int> = buildList {
    for (row in mat) {
        val c = row.indexOfFirst { it != 0 }
        if (c >= 0) add(c)
    }
}

fun Matrix.solve(): List<Int> {
    val m = this
    val cols = m[0].size
    val v = IntArray(size) { r -> m[r][cols-1] }
    val maxs = IntArray(cols-1) { Int.MAX_VALUE }
    for(r in m.indices) for(c in 0..cols-2)
        if (m[r][c] == 1 && maxs[c] > v[r]) maxs[c] = v[r]
    val reduced = m.gaussElimination()
    //reduced.forEach { println(it.contentToString()) }
    val pivots = pivotsOf(reduced)
    val freeVars = (0..(cols-2)) - pivots
    val ranges = freeVars.map { 0..maxs[it] }
    //println("Pivots:$pivots, FreeVars:$freeVars, Ranges:$ranges")
    val best = reduced.findBestSolutions(freeVars, ranges)
    return best
}

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

    fun Machine.toMatrix(): Matrix {
        val matrix = Array(joltage.size) { IntArray(buttons.size+1) { 0 } }
        buttons.forEachIndexed { j, b -> b.forEach { i -> matrix[i][j] = 1 } }
        joltage.forEachIndexed { i, j -> matrix[i][buttons.size] = j }
        return matrix
    }

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

    fun part2Old(input: List<String>): Int {
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

    fun part2(input: List<String>): Int {
        val machines = input.map { it.toMachine() }
        return machines.sumOf {
            val sol = it.toMatrix().solve()
            //println("Solution: $sol = ${sol.sum()}")
            sol.sum()
        }
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 33)

    val input = readInput("Day10")
    part1(input).println() // 401
    part2(input).println() // 15017
}
