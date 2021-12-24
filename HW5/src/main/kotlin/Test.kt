import mutils.*
import opengl.GLContext
 import opengl.GLProgram
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL33.*
import kotlin.math.PI
import kotlin.math.abs


fun main() {
    val context = GLContext.createWindow("Hello World!")

    context.use()
    context.show()

    val testProgram = GLProgram("first.vert", "first.frag")
    testProgram.use()

    val data = floatArrayOf(
        -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
        0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
        0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
        0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
        -0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
        -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,

        -0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,
        0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,
        0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,
        0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,
        -0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,
        -0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,

        -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,
        -0.5f,  0.5f, -0.5f, -1.0f,  0.0f,  0.0f,
        -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,
        -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,
        -0.5f, -0.5f,  0.5f, -1.0f,  0.0f,  0.0f,
        -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,

        0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,
        0.5f,  0.5f, -0.5f,  1.0f,  0.0f,  0.0f,
        0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,
        0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,
        0.5f, -0.5f,  0.5f,  1.0f,  0.0f,  0.0f,
        0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,

        -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,
        0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,
        0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,
        0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,
        -0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,
        -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,

        -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,
        0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,
        0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,
        0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,
        -0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,
        -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f
    )

    val color = floatArrayOf(1.0f, 0.5f, 0.3f)

    testProgram.setVec3Float(color, "uColor")
    val vao = glGenVertexArrays()
    glBindVertexArray(vao)

    val vbo = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER, vbo)
    glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW)



    glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.SIZE_BYTES, 0L)
    glEnableVertexAttribArray(0)
    glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.SIZE_BYTES, (3 * Float.SIZE_BYTES).toLong())
    glEnableVertexAttribArray(1)

    glBindVertexArray(0)
    glBindVertexArray(vao)

    var angle = 45.0 * PI / 180.0

    context.drawLoop {
//        val color = (System.currentTimeMillis() / 2 % 1000) / 1000f
//        glClearColor(1.0f, abs(1 - 2 * color), 0.0f, 1.0f)

        var transformMat = Matrix.identity(4)

        val scaleVec = Vector(listOf(1.0, 1.0,1.0))


        angle += 0.01

        transformMat = scale(transformMat, scaleVec)

        transformMat = rotateAroundY(transformMat, 45.0*PI/180.0)

        transformMat = rotateAroundX(transformMat, angle)

        val translateVec = Vector(listOf(0.5, 0.5, 0.0))

        transformMat = translate(transformMat, translateVec)

        testProgram.setMat4Float(transformMat.flatColListFloat(), "model")

        var viewMat = Matrix.identity(4)

        viewMat = translate(viewMat, Vector(listOf(0.0, 0.0, -4.0)))

        testProgram.setMat4Float(viewMat.flatColListFloat(), "view")

        var projectionMat = Matrix.identity(4)

        projectionMat = createPerspectiveProjection(45.0, 1080.0/720.0, 0.1, 100.0)

        testProgram.setMat4Float(projectionMat.flatColListFloat(), "projection")

        glEnable(GL_DEPTH_TEST)
        // Clear the framebuffer
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        glDrawArrays(GL_TRIANGLES,0, 36)
    }

    context.free()
    GLContext.cleanup()
}
