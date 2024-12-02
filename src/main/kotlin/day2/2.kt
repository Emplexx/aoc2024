package day2

import runDay
import kotlin.math.abs

typealias Report = List<Int>
typealias Input = List<Report>

fun main() = runDay(2, ::partOne) { input ->
    input.lines().map { it.split(" ").map(String::toInt) }
}

fun partOne(input: Input) = input.count { it.isSameDirection() && it.isWithinSafeDifference() }

fun Report.isSameDirection() = zipWithNext().run {
    all { (a, b) -> a > b } || all { (a, b) -> a < b }
}

fun Report.isWithinSafeDifference() =
    zipWithNext().all { (a, b) -> abs(a - b) in 1..3 }

fun partTwo(input: Input) {
    val (safe, other) = input.partition { it.isSameDirection() && it.isWithinSafeDifference() }
    TODO()
}