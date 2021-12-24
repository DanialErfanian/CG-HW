package mutils

import kotlin.math.*

fun skewSymmetric(vector: Vector): Matrix {
    return Matrix(listOf(
        listOf(.0, -vector[2], vector[1]),
        listOf(vector[2], .0, -vector[0]),
        listOf(-vector[1], vector[0], .0)
    ))
}

fun rotateAroundX(vector: Vector, theta: Double): Vector {
    val rotMat = Matrix(listOf(
        listOf(1.0, 0.0, 0.0),
        listOf(0.0, cos(theta), -sin(theta)),
        listOf(0.0, sin(theta), cos(theta))
    ))
    return rotMat * vector
}

fun rotateAroundY(vector: Vector, theta: Double): Vector {
    val rotMat = Matrix(listOf(
        listOf(cos(theta), 0.0, sin(theta)),
        listOf(.0, 1.0, .0),
        listOf(-sin(theta), .0, cos(theta))
    ))
    return rotMat * vector
}

fun rotateAroundZ(vector: Vector, theta: Double): Vector {
    val rotMat = Matrix(listOf(
        listOf(cos(theta), -sin(theta), .0),
        listOf(sin(theta), cos(theta), .0),
        listOf(.0, .0, 1.0)
    ))
    return rotMat * vector
}

fun rotate2DVec(vector: Vector, theta: Double): Vector {
    val rotMat = Matrix(listOf(
        listOf(cos(theta), -sin(theta)),
        listOf(sin(theta), cos(theta))
    ))
    return rotMat * vector
}

fun calculate2DVectorAngle(refVec: Vector, otherVec: Vector, clockwise: Boolean = false): Double {
    val ref = Vector(listOf(refVec[0], refVec[1], 0.0))
    val other = Vector(listOf(otherVec[0], otherVec[1], 0.0))
    var baseAngle = acos(ref.dot(other))
    val testVector = if (clockwise)
        rotateAroundZ(ref, -baseAngle)
    else
        rotateAroundZ(ref, baseAngle)
    if (testVector.dot(other) < 0.9999)
        baseAngle = 2 * PI - baseAngle
    return baseAngle
}

fun applyPitch(vector: Vector, pitchDegrees: Double): Vector {
    val pitch = pitchDegrees * PI / 180
    val currentPitch = asin(vector[2])
    val finalPitch = minOf(maxOf(currentPitch + pitch, -PI / 2 + epsilon), PI / 2 - epsilon)
    val currentYaw = calculate2DVectorAngle(Vector(listOf(1.0, 0.0)), vector[0..1])
    return rotateAroundZ(rotateAroundY(Vector(listOf(1.0, 0.0, 0.0)), -finalPitch), currentYaw)
}
