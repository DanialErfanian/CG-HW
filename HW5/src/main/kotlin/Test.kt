import opengl.GLContext
import opengl.GLProgram
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL33.*
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin


fun main() {
    val context = GLContext.createWindow("Hello World!")

    context.use()
    context.show()

    val testProgram = GLProgram("first.vert", "first.frag")
    testProgram.use()

    // Rectangle
    val rectangleData = floatArrayOf(
        -0.5f, -0.5f, 0.0f,         1.0f, 0.0f, 0.0f,
        0.0f, -0.5f, 0.0f,          0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 0.0f,           0.0f, 0.0f, 1.0f,
        -0.5f, 0.0f, 0.0f,          1.0f, 1.0f, 0.0f,
    )

    val rectangleIndices = intArrayOf(
        0, 1, 2,
        3, 0, 2,
    )

    val rVao = glGenVertexArrays()
    glBindVertexArray(rVao)

    val rVbo = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER, rVbo)
    glBufferData(GL_ARRAY_BUFFER, rectangleData, GL_STATIC_DRAW)

    val rEbo = glGenBuffers()
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, rEbo)
    GL15.glBufferData(GL_ELEMENT_ARRAY_BUFFER, rectangleIndices, GL_STATIC_DRAW)

    glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.SIZE_BYTES, 0L)
    glEnableVertexAttribArray(0)
    glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.SIZE_BYTES, (3 * Float.SIZE_BYTES).toLong())
    glEnableVertexAttribArray(1)

    // Circle Lines
    val pointCount = 300
    val radius = 0.3f
    val centerX = 0.5f
    val centerY = 0.5f
    val circleList = mutableListOf<Float>()
    for (i in 0 until pointCount) {
        val ratio = i / pointCount.toFloat()
        val angle = (ratio * 2 * PI).toFloat()
        val xPos = cos(angle) * radius + centerX
        val yPos = sin(angle) * radius + centerY
        val colorR = maxOf(abs(ratio * 6 - 3) - 1, 0f) / 6f
        val colorG = maxOf(2 - abs(ratio * 6 - 2), 0f) / 6f
        val colorB = maxOf(2 - abs(ratio * 6 - 4), 0f) / 6f
        circleList.addAll(listOf(xPos, yPos, 0f, colorR, colorG, colorB))
    }
    val circleData = circleList.toFloatArray()

    val cVao = glGenVertexArrays()
    glBindVertexArray(cVao)

    val cVbo = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER, cVbo)
    GL15.glBufferData(GL_ARRAY_BUFFER, circleData, GL_STATIC_DRAW)

    GL20.glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.SIZE_BYTES, 0L)
    glEnableVertexAttribArray(0)
    glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.SIZE_BYTES, (3 * Float.SIZE_BYTES).toLong())
    glEnableVertexAttribArray(1)

    glBindVertexArray(0)

    glLineWidth(5f)
    context.drawLoop {
        // Clear the framebuffer
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        // Draw the rectangle
        glBindVertexArray(rVao)
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0L)

        // Draw the Circle
        glBindVertexArray(cVao)
        glDrawArrays(GL_LINE_LOOP, 0, pointCount)
    }

    context.free()
    GLContext.cleanup()
}
