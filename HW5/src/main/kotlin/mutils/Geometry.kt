package mutils

import kotlin.math.*

fun translate(matrix: Matrix, vector: Vector): Matrix {
    val translateMat = Matrix(
        listOf(
            listOf(1.0, .0, .0, vector[0]),
            listOf(.0, 1.0, .0, vector[1]),
            listOf(.0, .0, 1.0, vector[2]),
            listOf(.0, .0, .0, 1.0)
        )
    )
    return translateMat * matrix
}

fun scale(matrix: Matrix, vector: Vector): Matrix {
    val scaleMat = Matrix(
        listOf(
            listOf(vector[0], .0, .0, .0),
            listOf(.0, vector[1], .0, .0),
            listOf(.0, .0, vector[2], .0),
            listOf(.0, .0, .0, 1.0)
        )
    )
    return scaleMat * matrix
}

fun skewSymmetric(vector: Vector): Matrix {
    return Matrix(
        listOf(
            listOf(.0, -vector[2], vector[1]),
            listOf(vector[2], .0, -vector[0]),
            listOf(-vector[1], vector[0], .0)
        )
    )
}


fun rotateAroundX(matrix: Matrix, theta: Double): Matrix {
    val rotateX = Matrix(
        listOf(
            listOf(1.0, 0.0, 0.0, 0.0),
            listOf(0.0, cos(theta), -sin(theta), 0.0),
            listOf(0.0, sin(theta), cos(theta), 0.0),
            listOf(0.0, 0.0, 0.0, 1.0)
        )
    )
    return rotateX * matrix
}

fun rotateAroundY(matrix: Matrix, theta: Double): Matrix {
    val rotateY = Matrix(
        listOf(
            listOf(cos(theta), 0.0, sin(theta), 0.0),
            listOf(.0, 1.0, .0, 0.0),
            listOf(-sin(theta), .0, cos(theta), 0.0),
            listOf(0.0, 0.0, 0.0, 1.0),
        )
    )
    return rotateY * matrix
}

fun rotateAroundZ(matrix: Matrix, theta: Double): Matrix {
    val rotateZ = Matrix(
        listOf(
            listOf(cos(theta), -sin(theta), .0, 0.0),
            listOf(sin(theta), cos(theta), .0, 0.0),
            listOf(.0, .0, 1.0, 0.0),
            listOf(0.0, 0.0, 0.0, 1.0),
        )
    )
    return rotateZ * matrix
}


