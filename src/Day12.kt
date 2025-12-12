
/*
  All region of big problem is on of thise two cases:
   - either there is not enough area to place all shapes
   - or there is enough area to place all shapes anyway
*/

typealias Shape = CharMatrix  // Always 3x3 !!!

fun main() {
    data class Region(val cols: Int, val rows: Int, val shapesQuantity: List<Int>)
    fun String.toRegion() = Region(
        cols = substringBefore('x').toInt(),
        rows = substringBefore(':').substringAfter('x').toInt(),
        shapesQuantity = substringAfter(": ").split(' ').map { it.toInt() }
    )

    fun part1(input: List<String>): Int {
        val parts = input.splitByEmptyLine()
        val shapes = parts.dropLast(1).map { it.drop(1).toCharMatrix() }
        check( shapes.all { it.size==3 && it[0].size==3 } )
        val regions = parts.last().map{ it.toRegion() }
        val shapesArea = shapes.map { s -> s.limits().allPositions().count { s[it]=='#' } }
        val insufficientAreaRegions = regions.count {
            val area = it.cols * it.rows
            val total = it.shapesQuantity.mapIndexed { idx, qt ->  qt * shapesArea[idx] }.sum()
            total > area
        }
        val anywayCombinationRegions = regions.count {
            val anywayShapes = (it.rows/3) * (it.cols/3)
            it.shapesQuantity.sum() <= anywayShapes
        }
        check(insufficientAreaRegions + anywayCombinationRegions == regions.size)
        return anywayCombinationRegions
    }

    val testInput = readInput("Day12_test")
    //check(part1(testInput) == 2) // Example is not the same conditions

    val input = readInput("Day12")
    part1(input).println() // 510
}
