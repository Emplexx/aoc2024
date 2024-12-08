package day7

import runDay

data class Equation(val expect: Long, val numbers: List<Long>)
typealias Input = List<Equation>

enum class Operator(val append: (Long, Long) -> Long) {
    Add(Long::plus), Multiply(Long::times), Concat(::concat)
}

fun concat(x: Long, y: Long): Long {
    var xScale = 1
    while (xScale <= y) {
        xScale *= 10
    }
    return x * xScale + y
}

fun main() = runDay(7, ::partOne, ::partTwo) { input ->
    input.lines().map {
        val (expect, tail) = it.split(":")
        val numbers = tail.trim().split(" ").map(String::toLong)
        Equation(expect.toLong(), numbers)
    }
}

fun partOne(input: Input) = solution(input, listOf(Operator.Add, Operator.Multiply))
fun partTwo(input: Input) = solution(input, Operator.entries)

fun solution(input: Input, operatorsInUse: List<Operator>) =
    input.sumOf { (expect, numbers) ->

        val combinations = getAllOperatorCombinations(operatorsInUse, numbers)
        var equals = false

        while (!equals && combinations.hasNext()) {
            val operators = combinations.next()
            equals = applyOperators(numbers, operators) == expect
        }

        if (equals) expect else 0
    }


fun getAllOperatorCombinations(operators: List<Operator>, numbers: List<Long>) =
    nestedForEachSequence(operators, numbers.lastIndex - 1, emptyList()).iterator()

fun applyOperators(numbers: List<Long>, operators: List<Operator>) =
    numbers.reduceIndexed { i, acc, next ->
        operators[i - 1].append(acc, next)
    }

fun nestedForEachSequence(
    list: List<Operator>,
    repeat: Int,
    values: List<Operator>,
): Sequence<List<Operator>> = sequence {

    suspend fun SequenceScope<List<Operator>>.getNextLazy(
        list: List<Operator>,
        repeat: Int,
        values: List<Operator>,
    ): Unit =
        if (repeat < 1) list.forEach { op -> yield(values + op) }
        else list.forEach { getNextLazy(list, repeat - 1, values + it) }


    getNextLazy(list, repeat, values)
}
