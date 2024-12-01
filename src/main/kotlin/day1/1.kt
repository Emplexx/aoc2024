package day1

import runDay
import kotlin.math.absoluteValue

const val separator = "   "

data class Input(
    val leftList: List<Int> = emptyList(),
    val rightList: List<Int> = emptyList(),
)

fun main() = runDay(1, ::partOne, ::partTwo) { input ->
    input.lines()
        .fold(Input()) { (l, r), line ->
            val (left, right) = line.split(separator)
            Input(l + left.toInt(), r + right.toInt())
        }
        .let { (l, r) -> Input(l.sorted(), r.sorted()) }
}

val Pair<Int, Int>.distance get() = (first - second).absoluteValue

private fun partOne(input: Input) =
    input.leftList.zip(input.rightList).sumOf { it.distance }

private fun partTwo(input: Input) =
    input.leftList.sumOf { id ->
        val count = input.rightList.count { it == id }
        id * count
    }
