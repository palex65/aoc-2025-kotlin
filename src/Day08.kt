import java.sql.Connection
import kotlin.math.sqrt

typealias DistanceTable = List<MutableList<Pair<Int, Float>>>

fun main() {
    data class Box(val x: Int, val y: Int, val z: Int)

    fun String.toBox(): Box =
        split(',').map { it.toInt() }.let { (x,y,z) -> Box(x,y,z) }

    fun Box.distanceTo(b: Box): Float {
        val dx = (x-b.x).toLong()
        val dy = (y-b.y).toLong()
        val dz = (z-b.z).toLong()
        return sqrt((dx * dx + dy * dy + dz * dz).toFloat())
    }

    fun List<Box>.toDistanceTable() = mapIndexed { idx, b ->
        List(size-idx-1){
            val i = idx+it+1
            i to get(i).distanceTo(b)
        }.toMutableList()
    }

    fun DistanceTable.minDistancePair(): Pair<Int,Int> {
        var minDistance = Float.MAX_VALUE
        var pair = 0 to 0
        forEachIndexed { a, distances ->
            if (distances.isNotEmpty()) {
                val min = distances.minBy { it.second }
                if (min.second < minDistance) {
                    minDistance = min.second
                    pair = a to min.first
                }
            }
        }
        return pair
    }

    fun process(input: List<String>, max: Int? = null): Int {
        val boxes = input.map { it.toBox() }
        val dt = boxes.toDistanceTable()
        val circuits = mutableListOf<MutableSet<Int>>()
        var lastPair = 0 to 0
        var count = 0
        while(circuits.size!=1 || circuits[0].size < boxes.size) {
            lastPair = dt.minDistancePair()
            val (a, b) = lastPair
            val ca = circuits.indexOfFirst { a in it }
            val cb = circuits.indexOfFirst { b in it }
            when {
                ca != -1 && cb != -1 ->
                    if (ca!=cb) {
                        circuits[ca].addAll( circuits[cb] )
                        circuits.removeAt(cb)
                    }
                ca != -1 -> circuits[ca].add(b)
                cb != -1 -> circuits[cb].add(a)
                else -> circuits.add(mutableSetOf(a,b))
            }
            dt[a].removeIf { it.first==b }
            if (max != null && ++count==max) {
                circuits.sortBy { it.size }
                return circuits.takeLast(3).fold(1){ acc, c -> c.size * acc }
            }
        }
        return boxes[lastPair.first].x * boxes[lastPair.second].x
    }

    fun part1(input: List<String>, connections: Int): Int = process(input, connections)
    fun part2(input: List<String>): Int = process(input)

    val testInput = readInput("Day08_test")
    check(part1(testInput,10) == 40)
    check(part2(testInput) == 25272)

    val input = readInput("Day08")
    part1(input,1000).println() // 164475
    part2(input).println() // 169521198
}
