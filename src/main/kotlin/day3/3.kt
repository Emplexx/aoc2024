package day3

import runDay

typealias Instruction = Pair<Int, Int>
typealias Validator = (String) -> String?

fun main() = runDay(3, ::partOne, ::partTwo) { it }

fun partOne(input: String) =
    findInstructions(input) { it.substringAfter("mul(", "") }.sumOf { (a, b) -> a * b }

val validateNumber: Validator = { string ->
    string.takeWhile { it.digitToIntOrNull() != null }.takeUnless { it.isEmpty() }
}

val validateChar: (Char) -> Validator = { char ->
    { string -> string.take(1).takeIf { it.first() == char } }
}

val validateMul = listOf(validateNumber, validateChar(','), validateNumber, validateChar(')'))

fun findInstructions(
    input: String,
    instructions: List<Instruction> = emptyList(),
    beginInstruction: (String) -> String,
): List<Instruction> =
    if (input.isBlank()) instructions
    else beginInstruction(input)
        .let { it to emptyList<String>() }
        .let {
            validateMul.fold(it) { (input, elements), validate ->

                val validated = validate(input)

                if (validated == null) return findInstructions(input, instructions, beginInstruction)
                else input.drop(validated.length) to elements.plus(validated)
            }
        }
        .let { (remaining, elements) ->
            val mul = elements[0].toInt() to elements[2].toInt()
            findInstructions(remaining, instructions + mul, beginInstruction)
        }

fun partTwo(input: String) =
    findInstructions(input) { jumpToNextInstruction(it) }.sumOf { (a, b) -> a * b }

fun jumpToNextInstruction(it: String): String {
    val iMul = it.indexOf("mul(")
    val iDoNot = it.indexOf("don't()")
    val iDo = it.indexOf("do()")

    val i = listOf(iMul, iDo, iDoNot).filter { it != -1 }.minOrNull()

    return when (i) {
        iMul -> it.substringAfter("mul(", "")
        null -> ""
        else -> it.substringAfter("do()", "").let(::jumpToNextInstruction)
    }
}
