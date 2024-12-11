package day9

import runDay

fun main() = runDay(9, ::partOne, ::partTwo) { it }

fun partOne(input: String) = solution(
    input,
    manipulateIntDrive = { drive ->
        var firstFreeBlock = drive.indexOfFirst { it == null }
        var lastBlock = drive.indexOfLast { it != null }

        while (firstFreeBlock < lastBlock) {
            val id = drive[lastBlock]
            drive[lastBlock] = null
            drive[firstFreeBlock] = id
            firstFreeBlock = drive.indexOfFirstNullFrom(firstFreeBlock)
            lastBlock = drive.indexOfLastNotNullFrom(lastBlock)
        }
    }
)

fun partTwo(input: String) = solution(
    input,
    manipulateTypedDrive = { drive ->

        var nextFileIndex = run {
            val highestId = drive.maxOf { if (it is File) it.id else -1 }
            drive.indexOfLast { it is File && it.id == highestId }
        }

        while (nextFileIndex > 0) {

            val file = drive[nextFileIndex] as File

            val firstSpaceLargeEnough = drive
                .indexOfFirst { it is Space && file.size <= it.size }
                .takeUnless { it == -1 }

            if (firstSpaceLargeEnough != null && firstSpaceLargeEnough < nextFileIndex /* is to the left*/) {

                run { // update the space with size after moving file

                    val newSpace = drive[firstSpaceLargeEnough]
                        .let { it as Space }
                        .let { it.copy(size = it.size - file.size) }

                    drive[firstSpaceLargeEnough] = newSpace
                }

                run { // replace file with free space and/or merge spaces

                    val leftOfFile = nextFileIndex - 1
                    val rightOfFile: Int

                    if (drive[leftOfFile] is Space) {
                        drive[leftOfFile] = drive[leftOfFile]
                            .let { it as Space }
                            .let { it.copy(size = it.size + file.size) }
                        drive.removeAt(nextFileIndex)

                        rightOfFile = nextFileIndex
                    } else {
                        drive[nextFileIndex] = Space(file.size)

                        rightOfFile = nextFileIndex + 1
                    }

                    if (drive.getOrNull(rightOfFile) is Space) {

                        val left = drive[rightOfFile - 1] as Space
                        val right = drive[rightOfFile] as Space

                        drive[rightOfFile - 1] = Space(left.size + right.size)
                        drive.removeAt(rightOfFile)
                    }

                }

                drive.add(firstSpaceLargeEnough, file)
            }

            nextFileIndex = drive.indexOfLast { it is File && it.id < file.id }
        }
    }
)

fun solution(
    input: String,
    manipulateTypedDrive: (MutableList<DriveSegment>) -> Unit = {},
    manipulateIntDrive: (Array<Int?>) -> Unit = {}
) = inputToTypedDrive(input)
    .toMutableList()
    .also(manipulateTypedDrive)
    .toIntDrive()
    .also(manipulateIntDrive)
    .checksum()

fun Array<Int?>.indexOfFirstNullFrom(index: Int): Int {
    for (i in index..lastIndex) if (this[i] == null) return i
    return -1
}

fun Array<Int?>.indexOfLastNotNullFrom(index: Int): Int {
    for (i in index downTo 0) if (this[i] != null) return i
    return -1
}

sealed interface DriveSegment {
    val size: Int
}

data class File(val id: Int, override val size: Int) : DriveSegment
data class Space(override val size: Int) : DriveSegment

fun inputToTypedDrive(input: String): List<DriveSegment> = buildList {
    var nextId = 0
    input.forEachIndexed { index, c ->
        val isFile = index % 2 == 0
        val size = c.digitToInt()

        if (isFile) {
            this += File(nextId, size)
            nextId++
        } else {
            this += Space(size)
        }
    }
}

fun List<DriveSegment>.toIntDrive(): Array<Int?> {
    val integerDrive = arrayOfNulls<Int>(sumOf { it.size })
    var nextBlockToWrite = 0


    for (segment in this) when (segment) {
        is File -> for (block in nextBlockToWrite until (nextBlockToWrite + segment.size)) {
            integerDrive[block] = segment.id
            nextBlockToWrite++
        }

        is Space -> for (block in nextBlockToWrite until (nextBlockToWrite + segment.size)) {
            nextBlockToWrite++
        }
    }

    return integerDrive
}

fun Array<Int?>.checksum() =
    mapIndexed { index, fileId -> if (fileId == null) 0L else index.toLong() * fileId }.sum()
