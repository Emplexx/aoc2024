package day8

import runDay

// atp i should just extract this into a separate package istg
data class Location(val x: Int, val y: Int) {

    fun plusX(amount: Int) = copy(x = x + amount)
    fun minusX(amount: Int) = copy(x = x - amount)
    fun plusY(amount: Int) = copy(y = y + amount)
    fun minusY(amount: Int) = copy(y = y - amount)

    operator fun plus(amount: Int) = copy(x = x + amount, y = y + amount)
    operator fun minus(amount: Int) = copy(x = x - amount, y = y - amount)

}

data class XYRange(val x: IntRange, val y: IntRange) {
    operator fun contains(it: Location): Boolean {
        return it.x in x && it.y in y
    }
}

fun main() = runDay(8, ::partOne, ::partTwo) { input ->
    Input(getRange(input), getFrequenciesAndAntennae(input))
}

fun partOne(input: Input) = solution(input, ::getAntinodes)
fun partTwo(input: Input) = solution(input, ::getAntinodes2)

data class Input(
    val range: XYRange,
    val antennae: Map<Char, List<Location>>
)

fun solution(input: Input, getAntinodes: (Location, Location, XYRange) -> List<Location>): Int = input.antennae
    .flatMap { (_, locations) ->
        locations
            .asSequence()
            .flatMap { first ->
                locations.map { second ->
                    setOf(first, second)
                }
            }
            .filter { it.size == 2 }
            .distinct() // discard non-unique pairs
            .map { Pair(it.elementAt(0), it.elementAt(1)) }
            .flatMap { (first, second) -> getAntinodes(first, second, input.range) }
            .toList()
    }
    .distinct()
    .size

fun getRange(input: String) = run {
    val maxX = input.lines()[0].lastIndex
    val maxY = input.lines().lastIndex
    val xRange = 0..maxX
    val yRange = 0..maxY
    XYRange(xRange, yRange)
}

fun getFrequenciesAndAntennae(input: String): Map<Char, List<Location>> =
    buildMap<Char, MutableList<Location>> {

        input.lines()
            .flatMap { line -> line.toList().filterNot { it == '.' || it == '\n' } }
            .distinct()
            .forEach { this[it] = mutableListOf() }

        input.lines().forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                if (c != '.') this[c]?.add(Location(x, y))
            }
        }

    }.mapValues { it.value.toList() }

fun getAntinodes(a1: Location, a2: Location, range: XYRange): List<Location> {
    val diffX = a2.x - a1.x
    val diffY = a2.y - a1.y
    return listOfNotNull(
        a1.minusX(diffX).minusY(diffY).takeIf { it in range },
        a2.plusX(diffX).plusY(diffY).takeIf { it in range }
    )
}

fun getAntinodes2(a1: Location, a2: Location, range: XYRange): List<Location> {

    val diffX = a2.x - a1.x
    val diffY = a2.y - a1.y

    val prev = mutableListOf(a1)
    while (true) {
        val node = prev.last().minusX(diffX).minusY(diffY)
        if (node in range) prev.add(node) else break
    }

    val next = mutableListOf(a2)
    while (true) {
        val node = next.last().plusX(diffX).plusY(diffY)
        if (node in range) next.add(node) else break
    }

    return (prev + next)
}
