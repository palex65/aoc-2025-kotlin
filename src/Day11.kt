
fun main() {
    data class Device(val name: String, val outputs: List<String>)
    fun String.toDevice() = Device(
        name = substringBefore(": "),
        outputs = substringAfter(": ").split(' ')
    )

/*
typealias Path = List<Int>
typealias Links = Map<Int, Path>

    fun Links.pathsOf(from: Int, to: Int): Set<Path> {
        val paths = mutableSetOf<Path>()
        val open = mutableListOf(from to listOf(from))
        while(open.isNotEmpty()) {
            val curr = open.removeFirst()
            getValue(curr.first).forEach { dev ->
                val path = curr.second + dev
                if (dev == to) paths.add(path)
                else open.add(dev to path)
            }
        }
        return paths
    }

    fun part1(input: List<String>): Int {
        val devices = input.map { it.toDevice() }
        val links = devices.associate { devices.indexOf(it) to it.outputs.map { name ->
            devices.indexOfFirst { it.name==name }
        } }
        val start = devices.indexOfFirst { it.name=="you" }
        val target = -1
        val paths = links.pathsOf(start,target)
        return paths.size
    }
*/
    fun part1(input: List<String>): Int {
        val devices = input.map { it.toDevice() }
        val links = devices.associate { devices.indexOf(it) to it.outputs.map { name ->
            devices.indexOfFirst { it.name==name }
        } }
        val start = devices.indexOfFirst { it.name=="you" }
        val target = -1

        val open = mutableListOf(start)
        val table = mutableMapOf(start to 1)
        while (open.isNotEmpty()) {
            val curr = open.removeFirst()
            if (curr == target) continue
            val counter = table.remove(curr) ?: continue
            links.getValue(curr).forEach { dev ->
                if (dev !in table) {
                    table[dev] = 0
                    open.add(dev)
                }
                val devCounter = table.getValue(dev)
                table[dev] = devCounter + counter
            }
        }
        return table.getValue(target)
    }

    class PathCounters(val simple: Long=0, val fft: Long=0, val dac: Long=0, val both: Long=0)

    fun PathCounters.simplePath(c: PathCounters) =
        PathCounters(simple+c.simple, fft+c.fft, dac+c.dac, both+c.both)
    fun PathCounters.fftPath(c: PathCounters) =
        PathCounters(simple, fft+c.simple+c.fft, dac, both+c.dac+c.both)
    fun PathCounters.dacPath(c: PathCounters) =
        PathCounters(simple, fft, dac+c.dac+c.simple, both+c.fft+c.both)

    fun part2(input: List<String>): Long {
        val devices = input.map { it.toDevice() }
        val links = devices.associate { devices.indexOf(it) to it.outputs.map { name ->
            devices.indexOfFirst { it.name==name }
        } }
        val svr = devices.indexOfFirst { it.name=="svr" }
        val fft = devices.indexOfFirst { it.name=="fft" }
        val dac = devices.indexOfFirst { it.name=="dac" }
        val out = -1

        val open = mutableListOf(svr)
        val table = mutableMapOf(svr to PathCounters(1))
        while (open.isNotEmpty()) {
            val curr = open.removeFirst()
            if (curr == out) continue
            val counters = table.remove(curr) ?: continue
            links.getValue(curr).forEach { dev ->
                if (dev !in table) {
                    table[dev] = PathCounters()
                    open.add(dev)
                }
                val devCounters = table.getValue(dev)
                table[dev] = when(dev) {
                    fft -> devCounters.fftPath(counters)
                    dac -> devCounters.dacPath(counters)
                    else -> devCounters.simplePath(counters)
                }
            }
        }
        return table.getValue(out).both
    }

    val testInput1 = readInput("Day11_test")
    check(part1(testInput1) == 5)
    val testInput2 = readInput("Day11_2test")
    check(part2(testInput2) == 2L)

    val input = readInput("Day11")
    part1(input).println() // 571
    part2(input).println() // 511378159390560
}
