package day5

import runDay

typealias PrintingRules = List<Pair<Int, Int>>
typealias PrintingUpdate = List<Int>
data class Input(
    val rules: PrintingRules,
    val updates: List<PrintingUpdate>
)

fun PrintingRules.pagesBefore(page: Int) = filter { it.second == page }.map { it.first }

fun <A> List<A>.getMid() = get(size / 2)

fun main() = runDay(5, ::partOne, ::partTwo) { input ->
    input.lines()
        .filterNot { it.isBlank() }
        .partition { it.contains("|") }
        .let { (rules, sequences) ->
            Input(
                rules.map { it.split("|").map(String::toInt).zipWithNext().single() },
                sequences.map { it.split(",").map(String::toInt) }
            )
        }
}

fun partOne(input: Input) =
    input.updates.filter { it.isCorrect(input.rules) }.sumOf { it.getMid() }

fun PrintingUpdate.isCorrect(rules: PrintingRules) =
    foldIndexed(true) { index, acc, page ->
        acc && rules.pagesBefore(page)
            .filter { it in this }
            .let { it.isEmpty() || subList(0, index).containsAll(it) }
    }

fun partTwo(input: Input) =
    input.updates.filterNot { it.isCorrect(input.rules) }
        .map { it.fixOrder(input.rules) }
        .sumOf { it.getMid() }

fun PrintingUpdate.fixOrder(rules: PrintingRules) = sortedWith { a, b -> compare(a, b, rules) }

fun compare(a: Int, b: Int, rules: PrintingRules) =
    rules.find { (it.first == a && it.second == b) }.let { ruleCorrect ->
        if (ruleCorrect != null) -1
        else rules.find { (it.first == b && it.second == a) }.let { ruleInverse ->
            if (ruleInverse != null) 1 else 0
        }
    }
