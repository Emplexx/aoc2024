package util

data class XY(val x: Int, val y: Int) {

    fun plusX(amount: Int) = copy(x = x + amount)
    fun minusX(amount: Int) = copy(x = x - amount)
    fun plusY(amount: Int) = copy(y = y + amount)
    fun minusY(amount: Int) = copy(y = y - amount)

    operator fun plus(amount: Int) = copy(x = x + amount, y = y + amount)
    operator fun minus(amount: Int) = copy(x = x - amount, y = y - amount)

}
