package util

data class Matrix<A>(val rows: List<List<A>>) : List<List<A>> by rows {

    val lastXIndex = first().lastIndex
    val lastYIndex = rows.lastIndex
    val xRange = 0..lastXIndex
    val yRange = 0..lastYIndex

    operator fun get(x: Int, y: Int) = this[y][x]
    operator fun get(xy: XY) = this[xy.y][xy.x]

    operator fun contains(xy: XY) =
        xy.y in rows.indices && xy.x in first().indices

    fun locations() = rows.flatMapIndexed { y: Int, s ->
        s.mapIndexed { x, _ -> XY(x, y) }
    }

    companion object {
        operator fun invoke(lines: List<String>): Matrix<Char> = Matrix(lines.map(String::toList))
    }

}

typealias IntMatrix = Matrix<Int>
