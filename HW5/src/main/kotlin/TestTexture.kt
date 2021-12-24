import opengl.*
import org.lwjgl.opengl.GL43.*

fun main() {
    val context = GLContext.createWindow("Hello Texture", ViewPort(800, 800))

    context.use()
    context.show()

    val program = GLProgram("texture.vert", "texture.frag")
    program.use()

    val vertices: FloatArray = floatArrayOf(
        // positions          // texture coords
        0.5f, 0.5f, 0.0f,     1.0f, 1.0f, // top right
        0.5f, -0.5f, 0.0f,    1.0f, 0.0f, // bottom right
        -0.5f, -0.5f, 0.0f,   0.0f, 0.0f, // bottom left
        -0.5f, 0.5f, 0.0f,    0.0f, 1.0f  // top left
    )
    val indices = intArrayOf(
        0, 1, 3, // first triangle
        1, 2, 3  // second triangle
    )

    val vbo = glGenBuffers()
    val ebo = glGenBuffers()
    val vao = glGenVertexArrays()

    glBindVertexArray(vao)

    glBindBuffer(GL_ARRAY_BUFFER, vbo)
    glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)

    glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.SIZE_BYTES, 0L)
    glEnableVertexAttribArray(0)
    glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.SIZE_BYTES, 3L * Float.SIZE_BYTES)
    glEnableVertexAttribArray(1)

    val texture1 = TextureUnit("shrek.jpg")
    val texture2 = TextureUnit("awesome_face.png")
    program.setInt("texture1", texture1.unitNumber)
    program.setInt("texture2", texture2.unitNumber)

    glClearColor(0.2f, 0.3f, 0.3f, 1.0f)

    context.drawLoop {
        glClear(GL_COLOR_BUFFER_BIT)
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0)
    }

    context.free()
    GLContext.cleanup()
}