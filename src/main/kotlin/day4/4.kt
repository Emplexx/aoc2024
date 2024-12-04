package day4

import runDay

data class Matrix(val list: List<String>) : List<String> by list {
    private val lastXIndex = first().lastIndex
    private val lastYIndex = lastIndex
    private val xRange = 0..lastXIndex
    private val yRange = 0..lastYIndex

    operator fun get(location: Location) = this[location.y][location.x]
    operator fun contains(location: Location) = location.x in xRange && location.y in yRange

    fun locations() = list.flatMapIndexed { y: Int, s ->
        s.mapIndexed { x, _ -> Location(x, y) }
    }

}

data class Location(val x: Int, val y: Int) {

    fun plusX(amount: Int) = copy(x = x + amount)
    fun minusX(amount: Int) = copy(x = x - amount)
    fun plusY(amount: Int) = copy(y = y + amount)
    fun minusY(amount: Int) = copy(y = y - amount)

    operator fun plus(amount: Int) = copy(x = x + amount, y = y + amount)
    operator fun minus(amount: Int) = copy(x = x - amount, y = y - amount)

}

fun main() = runDay(4, ::partOne, ::partTwo) { input -> Matrix(input.lines()) }

fun partOne(matrix: Matrix) = matrix.locations().sumOf { location ->
    allDirections.count { matrix.wordAlong(it(location)) == "XMAS" }
}

fun partTwo(matrix: Matrix) = matrix.locations().count { location ->
    xDirections.all {
        val word = matrix.wordAlong(it(location))
        word == "MAS" || word == "SAM"
    }
}

fun Matrix.wordAlong(locations: List<Location>) =
    if (locations.any { it !in this }) ""
    else locations.fold("") { word, loc -> word + this[loc] }

fun locationsEast(location: Location) =
    listOf(location, location.plusX(1), location.plusX(2), location.plusX(3))

fun locationsWest(location: Location) =
    listOf(location, location.minusX(1), location.minusX(2), location.minusX(3))

fun locationsSouth(location: Location) =
    listOf(location, location.plusY(1), location.plusY(2), location.plusY(3))

fun locationsNorth(location: Location) =
    listOf(location, location.minusY(1), location.minusY(2), location.minusY(3))

fun locationsNw(location: Location) =
    listOf(
        location,
        location - 1,
        location - 2,
        location - 3
    )

fun locationsSe(location: Location) =
    listOf(
        location,
        location + 1,
        location + 2,
        location + 3
    )

fun locationsNe(location: Location) =
    listOf(
        location,
        location.plusX(1).minusY(1),
        location.plusX(2).minusY(2),
        location.plusX(3).minusY(3)
    )

fun locationsSw(location: Location) =
    listOf(
        location,
        location.minusX(1).plusY(1),
        location.minusX(2).plusY(2),
        location.minusX(3).plusY(3)
    )

val allDirections = listOf(
    ::locationsNorth,
    ::locationsNe,
    ::locationsEast,
    ::locationsSe,
    ::locationsSouth,
    ::locationsSw,
    ::locationsWest,
    ::locationsNw
)

fun locationsDiagonal1(location: Location) =
    listOf(
        location - 1,
        location,
        location + 1,
    )

fun locationsDiagonal2(location: Location) =
    listOf(
        location.plusX(1).minusY(1),
        location,
        location.minusX(1).plusY(1)
    )

val xDirections = listOf(
    ::locationsDiagonal1,
    ::locationsDiagonal2
)
