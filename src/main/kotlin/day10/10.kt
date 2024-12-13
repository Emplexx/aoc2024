package day10

import runDay

// finally
import util.IntMatrix
import util.Matrix
import util.XY

fun main() = runDay(10, ::partOne, ::partTwo) { input ->
    Matrix(input.lines().map { row -> row.map(Char::digitToInt) })
}

fun partOne(input: IntMatrix) = solution(input) { it.get9HeightPositionCount(input) }
fun partTwo(input: IntMatrix) = solution(input) { it.getUniqueTrailCount(input) }

fun solution(input: IntMatrix, scoreTrailStart: (XY) -> Int) = input.locations()
    .filter { input[it] == 0 }
    .associateWith(scoreTrailStart)
    .values
    .sum()

private fun XY.get9HeightPositionCount(input: IntMatrix): Int {
    
    var locations = listOf(this); repeat(9) {
        locations = locations.flatMap { input.getNextHeights(it) }.distinct()
    }

    return locations.size
}

fun XY.getUniqueTrailCount(input: IntMatrix): Int {

    var trails = listOf(listOf(this)); repeat(9) {
        trails = trails.flatMap { trail ->
            val lastTrailLocation = trail.last()
            val continues = input.getNextHeights(lastTrailLocation)
            continues.map { xy -> trail + xy }
        }
    }

    return trails.size
}

fun IntMatrix.getNextHeights(location: XY): List<XY> {
    val currentHeight = this[location]
    return getSurroundingLocations(location).filter { this[it] - currentHeight == 1 }
}

fun IntMatrix.getSurroundingLocations(location: XY): List<XY> {
    val outOfBounds = { it: XY -> it !in this }
    val top = location.minusY(1).takeUnless(outOfBounds)
    val right = location.plusX(1).takeUnless(outOfBounds)
    val bottom = location.plusY(1).takeUnless(outOfBounds)
    val left = location.minusX(1).takeUnless(outOfBounds)
    return listOfNotNull(top, right, bottom, left)
}
