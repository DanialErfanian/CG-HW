package mutils

import kotlin.math.pow
import kotlin.math.sqrt


class Vector(private val data: List<Double>) {

    private var privateLength: Double? = null
    val length: Double
        get() {
            if (privateLength == null)
                privateLength = sqrt(data.sumOf { it.pow(2) })
            return privateLength!!
        }

    private var privateNormalized: Vector? = null
    val normalized: Vector
        get() {
            if (privateNormalized == null) {
                val normalizedVec = if (length == 0.0) this else this / length
                normalizedVec.privateNormalized = normalizedVec
                normalizedVec.privateLength = 1.0
                this.privateNormalized = normalizedVec
            }
            return privateNormalized!!
        }

    val size = data.size

    fun sum(): Double = data.sum()

    fun copy(): Vector = Vector(data)

    fun toList(): List<Double> = data

    fun toFloatList(): List<Float> = data.map { it.toFloat() }

    fun distanceFrom(other: Vector): Double {
        if (size != other.size)
            error("Trying to get distance between vectors $this, $other with different sizes")
        return (this - other).length
    }

    fun dot(other: Vector): Double {
        if (size != other.size)
            error("Trying to dot two vectors $this, $other with different sizes")
        return (this * other).sum()
    }

    fun cross3(other: Vector): Vector {
        if (size != 3)
            error("Calling cross3 on a non-three element vector $this")
        if (other.size != 3)
            error("Trying to cross3 vector $this into non-three element vector $other")
        return skewSymmetric(this) * other
    }

    fun cross2(other: Vector): Double {
        if (size != 2)
            error("Calling cross3 on a non-three element vector $this")
        if (other.size != 2)
            error("Trying to cross3 vector $this into non-three element vector $other")
        return (Vector(listOf(this[0], this[1], .0)).cross3(Vector(listOf(other[0], other[1], .0))))[2]
    }

    override fun equals(other: Any?): Boolean {
        if (other is Vector)
            return this.data == other.data
        return false
    }

    override fun hashCode(): Int {
        return data.hashCode()
    }

    override fun toString(): String {
        return "Vector: $data"
    }

    operator fun plus(other: Vector): Vector {
        if (size != other.size)
            error("Trying to add two vectors $this, $other with different sizes")
        return Vector(data.mapIndexed { index, d -> d + other[index] })
    }

    operator fun minus(other: Vector): Vector {
        if (size != other.size)
            error("Trying to subtract two vectors $this, $other with different sizes")
        return Vector(data.mapIndexed { index, d -> d - other[index] })
    }

    operator fun times(other: Vector): Vector {
        if (size != other.size)
            error("Trying to multiply two vectors $this, $other with different sizes")
        return Vector(data.mapIndexed { index, d -> d * other[index] })
    }

    operator fun div(other: Vector): Vector {
        if (size != other.size)
            error("Trying to divide two vectors $this, $other with different sizes")
        return Vector(data.mapIndexed { index, d -> d / other[index] })
    }

    operator fun plus(num: Number): Vector {
        val dNum = num.toDouble()
        return Vector(data.map { it + dNum })
    }

    operator fun minus(num: Number): Vector {
        val dNum = num.toDouble()
        return Vector(data.map { it - dNum })
    }

    operator fun times(num: Number): Vector {
        val dNum = num.toDouble()
        return Vector(data.map { it * dNum })
    }

    operator fun div(num: Number): Vector {
        val dNum = num.toDouble()
        if (dNum == 0.0)
            error("Division by zero")
        return Vector(data.map { it / dNum })
    }

    operator fun plus(matrix: Matrix): Matrix = matrix + this

    operator fun minus(matrix: Matrix): Matrix = matrix - this

    operator fun times(matrix: Matrix): Vector = matrix.transpose * this

    operator fun unaryMinus(): Vector = Vector(data.map { -it })

    operator fun unaryPlus(): Vector = copy()

    operator fun get(int: Int): Double = data[int]

    operator fun get(range: IntRange): Vector = Vector(data.slice(range))
}

operator fun Number.plus(vector: Vector) = vector + this

operator fun Number.minus(vector: Vector) = vector - this

operator fun Number.times(vector: Vector) = vector * this
