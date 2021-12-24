package mutils

import kotlin.math.PI
import kotlin.math.tan

fun createLookAt(eye: Vector, target: Vector, up: Vector): Matrix {
    val forward = (target - eye).normalized
    val side = forward.cross3(up).normalized
    val trueUp = side.cross3(forward).normalized

    return Matrix(listOf(
        listOf(side[0], side[1], side[2], -side.dot(eye)),
        listOf(trueUp[0], trueUp[1], trueUp[2], -trueUp.dot(eye)),
        listOf(-forward[0], -forward[1], -forward[2], forward.dot(eye)),
        listOf(0.0, 0.0, 0.0, 1.0)
    ))
}

fun createPerspectiveProjection(fovY: Double, aspectRatio: Double, near: Double, far: Double): Matrix {
    val yMax = near * tan(fovY * PI / 360.0)
    val xMax = yMax * aspectRatio
    return createPerspectiveProjectionFromBounds(-xMax, xMax, -yMax, yMax, near, far)
}

fun createPerspectiveProjectionFromBounds(
    left: Double,
    right: Double,
    bottom: Double,
    top: Double,
    near: Double,
    far: Double
): Matrix {
    val a = (right + left) / (right - left)
    val b = (top + bottom) / (top - bottom)
    val c = -(far + near) / (far - near)
    val d = -2.0 * far * near / (far - near)
    val e = 2.0 * near / (right - left)
    val f = 2.0 * near / (top - bottom)

    return Matrix(listOf(
        listOf(e, .0, a, .0),
        listOf(.0, f, b, .0),
        listOf(.0, .0, c, d),
        listOf(.0, .0, -1.0, .0)
    ))
}
