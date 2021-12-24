package opengl

import mutils.*
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWCursorPosCallback
import org.lwjgl.glfw.GLFWKeyCallback
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class FPSCamera(windowViewPort: ViewPort, startPos: Vector, private val worldUP: Vector) {
    companion object {
        private const val sensitivity: Double = 0.001
        private const val epsilon = 0.001
    }

    var moveSpeed = 20.0
    private var lastMillis: Long = System.currentTimeMillis()

    private var xAxis = 0.0
    private var zAxis = 0.0
    private var yAxis = 0.0
    private var yaw = 0.0
    private var pitch = 0.0
    private var lastX = windowViewPort.width / 2.0
    private var lastY = windowViewPort.height / 2.0

    var position = startPos
    private var front = Vector(listOf(0.0, 1.0, 0.0))
    private var right = front.cross3(worldUP).normalized
    private var up = right.cross3(front).normalized

    val cursorPosCallback: GLFWCursorPosCallback = GLFWCursorPosCallback.create { _, xPos, yPos ->
        val xOffset = lastX - xPos
        val yOffset = lastY - yPos
        if ((xOffset != 0.0) or (yOffset != 0.0)) {
            yaw += xOffset * sensitivity
            pitch += yOffset * sensitivity
            pitch = minOf(PI / 2 - epsilon, pitch)
            pitch = maxOf(pitch, -PI / 2 + epsilon)

            front = Vector(listOf(cos(yaw) * cos(pitch), sin(yaw) * cos(pitch), sin(pitch)))
            right = front.cross3(worldUP).normalized
            up = right.cross3(front).normalized

            lastX = xPos
            lastY = yPos
        }
    }

    val keyCallback: GLFWKeyCallback = GLFWKeyCallback.create { window, key, _, action, _ ->
        if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
            GLFW.glfwSetWindowShouldClose(window, true)
        } else if (action == GLFW.GLFW_PRESS) {
            when(key) {
                GLFW.GLFW_KEY_A -> xAxis = -1.0
                GLFW.GLFW_KEY_D -> xAxis = 1.0
                GLFW.GLFW_KEY_W -> yAxis = 1.0
                GLFW.GLFW_KEY_S -> yAxis = -1.0
                GLFW.GLFW_KEY_E -> zAxis = 1.0
                GLFW.GLFW_KEY_Q -> zAxis = -1.0
            }
        } else if (action == GLFW.GLFW_RELEASE) {
            when(key) {
                GLFW.GLFW_KEY_A -> xAxis = .0
                GLFW.GLFW_KEY_D -> xAxis = .0
                GLFW.GLFW_KEY_W -> yAxis = .0
                GLFW.GLFW_KEY_S -> yAxis = .0
                GLFW.GLFW_KEY_E -> zAxis = .0
                GLFW.GLFW_KEY_Q -> zAxis = .0
            }
        }
    }

    fun getViewMat(): Matrix {
        val currentMillis = System.currentTimeMillis()
        if (xAxis != 0.0 || yAxis != 0.0 || zAxis != 0.0) {
            val deltaT = (currentMillis - lastMillis).toDouble() / 1000.0
            val xDiff = xAxis * moveSpeed * deltaT
            val yDiff = yAxis * moveSpeed * deltaT
            val zDiff = zAxis * moveSpeed * deltaT
            position += xDiff * right + yDiff * front + zDiff * up
        }
        lastMillis = currentMillis

        return createLookAt(position, position + front, worldUP)
    }

}