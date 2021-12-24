import opengl.GLContext
import opengl.GLProgram
import opengl.ViewPort
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL43.*

fun main() {
    val context = GLContext.createWindow("Geometry Test", ViewPort(600, 600))

    context.use()
    context.show()

    val testProgram = GLProgram("geom_test.vert", "geom_test.frag", "geom_test.geom")
    testProgram.use()

    testProgram.setFloat("eyeSize", 0.2f)

    val pointsData = floatArrayOf(
        -0.6f,  0.6f, 1.0f, 0.0f, 0.0f, // top-left
        -0.4f,  0.4f, 0.0f, 0.0f, 1.0f,
        0.4f,  0.5f, 0.0f, 1.0f, 0.0f,  // top-right
        0.6f,  0.5f, 1.0f, 1.0f, 0.0f,
        0.5f, -0.3f, 0.0f, 1.0f, 1.0f,  // bottom-right
        0.5f, -0.7f, 1.0f, 0.0f, 1.0f,
        -0.4f, -0.4f, 1.0f, 1.0f, 1.0f, // bottom-left
        -0.6f, -0.6f, 0.0f, 0.0f, 0.0f,
    )

    val vao = glGenVertexArrays()
    glBindVertexArray(vao)

    val vbo = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER, vbo)
    glBufferData(GL_ARRAY_BUFFER, pointsData, GL_STATIC_DRAW)

    glEnableVertexAttribArray(0)
    GL20.glVertexAttribPointer(0, 2, GL_FLOAT, false, 5 * Float.SIZE_BYTES, 0L)
    glEnableVertexAttribArray(1)
    GL20.glVertexAttribPointer(1, 3, GL_FLOAT, false, 5 * Float.SIZE_BYTES, (2 * Float.SIZE_BYTES).toLong())

    glLineWidth(5f)
    glClearColor(0.6f, 0.6f, 0.6f, 1.0f)
    context.drawLoop {
        // Clear the framebuffer
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        glDrawArrays(GL_LINES, 0, 8)
    }

    context.free()
    GLContext.cleanup()
}
