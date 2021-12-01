package mutils

import kotlin.math.abs

const val epsilon = 0.0000001

fun Double.equalsEps(b:Double): Boolean {
    if (b == 0.0)
        return abs(this) < epsilon
    if (this == 0.0)
        return abs(b) < epsilon
    return abs(this - b) < epsilon
}

fun Double.compareEps(b: Double): Int {
    if (this.equalsEps(b))
        return 0
    return this.compareTo(b)
}