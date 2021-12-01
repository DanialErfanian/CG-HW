import mutils.*
import opengl.GLContext
import opengl.GLImage
import opengl.GLProgram
import org.apache.commons.io.IOUtils
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWCursorPosCallback
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.opengl.GL33.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


fun main() {
    var (context, window) = GLContext.createWindow("Hello World!")
    var state = -1;
    val translateVec = arrayOf(0.0, 0.0, 0.0)
    var dastanX = 0.0;
    var dastanY = 0.0;
    var dastanZ = 0.0;
    context.use()
    context.show()
    glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    var firstMouse = true
    var lastX = 0.0
    var lastY = 0.0

    glfwSetCursorPosCallback(window, GLFWCursorPosCallback.create { window, xpos, ypos ->
        if (firstMouse) {
            lastX = xpos;
            lastY = ypos;
            firstMouse = false;
        }

        var xoffset = xpos - lastX;
        var yoffset = lastY - ypos;
        lastX = xpos;
        lastY = ypos;

        val sensitivity = 0.001f;
        xoffset *= sensitivity;
        yoffset *= sensitivity;

        dastanY -= xoffset;
        dastanX -= yoffset;
//
//        if (pitch > 89.0)
//            pitch = 89.0;
//        if (pitch < -89.0)
//            pitch = -89.0;
//
//        val direction = arrayOf(0.0, 0.0, 0.0);
//        direction[0] = cos(Math.toRadians(yaw)) * cos(Math.toRadians(pitch));
//        direction[1] = sin(Math.toRadians(pitch));
//        direction[2] = sin(Math.toRadians(yaw)) * cos(Math.toRadians(pitch));
//        cameraFront = direction;
    })
    context.setKeyCallback(GLFWKeyCallback.create { l: Long, i: Int, i1: Int, action: Int, i3: Int ->
        println(i)
        if (i == 32 && action == 0) {
            println("Space pressed")
            state = (state + 1) % 2
            println(state)
        }
        val cameraSpeed = 0.03
        if (i == GLFW_KEY_D) {
            translateVec[0] -= cameraSpeed
        }
        if (i == GLFW_KEY_A) {
            translateVec[0] += cameraSpeed
        }
        if (i == GLFW_KEY_W) {
            translateVec[2] += cameraSpeed
        }
        if (i == GLFW_KEY_S) {
            translateVec[2] -= cameraSpeed
        }

    })

    val testProgram = GLProgram("first.vert", "first.frag")
    testProgram.use()

    val data = floatArrayOf(
//        x  ,   y  ,   z  ,     a      normal  ,  s  ,  t  ,
        -0.5f, -0.5f, -0.5f, +0.0f, +0.0f, -1.0f, +0.0f, +0.0f, +0.0f,
        +0.5f, -0.5f, -0.5f, +0.0f, +0.0f, -1.0f, 1.0f, +0.0f, +0.0f,
        +0.5f, +0.5f, -0.5f, +0.0f, +0.0f, -1.0f, 1.0f, 1.0f, +0.0f,
        +0.5f, +0.5f, -0.5f, +0.0f, +0.0f, -1.0f, 1.0f, 1.0f, +0.0f,
        -0.5f, +0.5f, -0.5f, +0.0f, +0.0f, -1.0f, +0.0f, 1.0f, +0.0f,
        -0.5f, -0.5f, -0.5f, +0.0f, +0.0f, -1.0f, +0.0f, +0.0f, +0.0f,

        -0.5f, -0.5f, +0.5f, +0.0f, 0.0f, +1.0f, +0.0f, +0.0f, +0.0f,
        +0.5f, -0.5f, +0.5f, +0.0f, 0.0f, +1.0f, 1.0f, +0.0f, +0.0f,
        +0.5f, +0.5f, +0.5f, +0.0f, 0.0f, +1.0f, 1.0f, 1.0f, +0.0f,
        +0.5f, +0.5f, +0.5f, +0.0f, 0.0f, +1.0f, 1.0f, 1.0f, +0.0f,
        -0.5f, +0.5f, +0.5f, +0.0f, 0.0f, +1.0f, +0.0f, 1.0f, +0.0f,
        -0.5f, -0.5f, +0.5f, +0.0f, 0.0f, +1.0f, +0.0f, +0.0f, +0.0f,

        -0.5f, +0.5f, +0.5f, -1.0f, +0.0f, 0.0f, 1.0f, +0.0f, +0.0f,
        -0.5f, +0.5f, -0.5f, -1.0f, +0.0f, 0.0f, 1.0f, 1.0f, +0.0f,
        -0.5f, -0.5f, -0.5f, -1.0f, +0.0f, 0.0f, +0.0f, 1.0f, +0.0f,
        -0.5f, -0.5f, -0.5f, -1.0f, +0.0f, 0.0f, +0.0f, 1.0f, +0.0f,
        -0.5f, -0.5f, +0.5f, -1.0f, +0.0f, 0.0f, +0.0f, +0.0f, +0.0f,
        -0.5f, +0.5f, +0.5f, -1.0f, +0.0f, 0.0f, 1.0f, +0.0f, +0.0f,

        +0.5f, +0.5f, +0.5f, 1.0f, 1.0f, +0.0f, 1.0f, +0.0f, +0.0f,
        +0.5f, +0.5f, -0.5f, 1.0f, 1.0f, +0.0f, 1.0f, 1.0f, +0.0f,
        +0.5f, -0.5f, -0.5f, 1.0f, 1.0f, +0.0f, +0.0f, 1.0f, +0.0f,
        +0.5f, -0.5f, -0.5f, 1.0f, 1.0f, +0.0f, +0.0f, 1.0f, +0.0f,
        +0.5f, -0.5f, +0.5f, 1.0f, 1.0f, +0.0f, +0.0f, +0.0f, +0.0f,
        +0.5f, +0.5f, +0.5f, 1.0f, 1.0f, +0.0f, 1.0f, +0.0f, +0.0f,

        -0.5f, -0.5f, -0.5f, +0.0f, -1.0f, 0.0f, +0.0f, 1.0f, +0.0f,
        +0.5f, -0.5f, -0.5f, +0.0f, -1.0f, 0.0f, 1.0f, 1.0f, +0.0f,
        +0.5f, -0.5f, +0.5f, +0.0f, -1.0f, 0.0f, 1.0f, +0.0f, +0.0f,
        +0.5f, -0.5f, +0.5f, +0.0f, -1.0f, 0.0f, 1.0f, +0.0f, +0.0f,
        -0.5f, -0.5f, +0.5f, +0.0f, -1.0f, 0.0f, +0.0f, +0.0f, +0.0f,
        -0.5f, -0.5f, -0.5f, +0.0f, -1.0f, 0.0f, +0.0f, 1.0f, +0.0f,

        -0.5f, +0.5f, -0.5f, 0.0f, +1.0f, 0.0f, +0.0f, 1.0f, 1.0f,
        +0.5f, +0.5f, -0.5f, 0.0f, +1.0f, 0.0f, 1.0f, 1.0f, 1.0f,
        +0.5f, +0.5f, +0.5f, 0.0f, +1.0f, 0.0f, 1.0f, +0.0f, 1.0f,
        +0.5f, +0.5f, +0.5f, 0.0f, +1.0f, 0.0f, 1.0f, +0.0f, 1.0f,
        -0.5f, +0.5f, +0.5f, 0.0f, +1.0f, 0.0f, +0.0f, +0.0f, 1.0f,
        -0.5f, +0.5f, -0.5f, 0.0f, +1.0f, 0.0f, +0.0f, 1.0f, 1.0f,

//        -0.5f, +0.5f, -0.5f, 0.0f, +1.0f, 0.0f, +0.0f, 1.0f, 1.0f,
//        -0.5f, +0.5f, -0.5f, 0.0f, +1.0f, 0.0f, +0.0f, 1.0f, 1.0f,
//        -0.5f, +0.5f, -0.5f, 0.0f, +1.0f, 0.0f, +0.0f, 1.0f, 1.0f,

    )

//    val color = floatArrayOf(1.0f, 0.5f, 0.0f)
//
//    testProgram.setVec3Float(color, "uColor")
    val vao = glGenVertexArrays()
    glBindVertexArray(vao)

    val vbo = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER, vbo)
    glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW)



    glVertexAttribPointer(0, 3, GL_FLOAT, false, 9 * Float.SIZE_BYTES, 0L)
    glEnableVertexAttribArray(0)
    glVertexAttribPointer(1, 3, GL_FLOAT, false, 9 * Float.SIZE_BYTES, (3 * Float.SIZE_BYTES).toLong())
    glEnableVertexAttribArray(1)
    glVertexAttribPointer(2, 2, GL_FLOAT, false, 9 * Float.SIZE_BYTES, (6 * Float.SIZE_BYTES).toLong())
    glEnableVertexAttribArray(2)
    glVertexAttribPointer(3, 1, GL_FLOAT, false, 9 * Float.SIZE_BYTES, (8 * Float.SIZE_BYTES).toLong())
    glEnableVertexAttribArray(3)

    val textureID = glGenTextures()
    glActiveTexture(GL_TEXTURE0)
    glBindTexture(GL_TEXTURE_2D, textureID)

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)


    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

    val classLoader: ClassLoader = Thread.currentThread().contextClassLoader
    val imageData = GLImage.loadImage(IOUtils.toByteArray(classLoader.getResourceAsStream("box-texture.jpg")))

    glTexImage2D(
        GL_TEXTURE_2D,
        0,
        GL_RGB,
        imageData.width,
        imageData.height,
        0,
        GL_RGBA,
        GL_UNSIGNED_BYTE,
        imageData.image
    )
    glGenerateMipmap(GL_TEXTURE_2D)

    imageData.free()

    val textureID2 = glGenTextures()
    glActiveTexture(GL_TEXTURE1)
    glBindTexture(GL_TEXTURE_2D, textureID2)

    val imageData2 = GLImage.loadImage(IOUtils.toByteArray(classLoader.getResourceAsStream("texture-2.jpeg")))
    glTexImage2D(
        GL_TEXTURE_2D,
        0,
        GL_RGB,
        imageData2.width,
        imageData2.height,
        0,
        GL_RGBA,
        GL_UNSIGNED_BYTE,
        imageData2.image
    )
    glGenerateMipmap(GL_TEXTURE_2D)
    imageData2.free()

    var angle = 45.0 * PI / 180.0
    var movingPartAngle = 0;
    context.drawLoop {
//        val color = (System.currentTimeMillis() / 2 % 1000) / 1000f
//        glClearColor(1.0f, abs(1 - 2 * color), 0.0f, 1.0f)

        if (state == 0)
            movingPartAngle += 1
        else if (state == 1)
            movingPartAngle -= 1
        movingPartAngle = movingPartAngle.coerceAtLeast(0).coerceAtMost(70)
        val dastan = (movingPartAngle * PI / 2 / 60)
        var transformMovingPartMat = Matrix.identity(4)
        val translateMovingPartVec = Vector(listOf(0.0, 0.5, 0.5)) // TODO Fix this
        transformMovingPartMat = translate(transformMovingPartMat, -translateMovingPartVec)
        transformMovingPartMat = rotateAroundX(transformMovingPartMat, dastan)
        transformMovingPartMat = translate(transformMovingPartMat, +translateMovingPartVec)
        testProgram.setMat4Float(transformMovingPartMat.flatColListFloat(), "movingPartMat")


        var transformMat = Matrix.identity(4)

        val scaleVec = Vector(listOf(1.0, 1.0, 1.0))


        angle += 0.01

        transformMat = scale(transformMat, scaleVec)

        transformMat = rotateAroundY(transformMat, 45.0 * PI / 180.0)

        transformMat = rotateAroundX(transformMat, angle)

//        transformMat = translate(transformMat, Vector(translateVec.toList()))

        testProgram.setMat4Float(transformMat.flatColListFloat(), "model")

        var viewMat = Matrix.identity(4)

        viewMat = translate(viewMat, Vector(listOf(0.0, 0.0, -4.0)))
        viewMat = rotateAroundX(viewMat, dastanX)
        viewMat = rotateAroundY(viewMat, dastanY)
        viewMat = rotateAroundZ(viewMat, dastanZ)
        viewMat = translate(viewMat, Vector(translateVec.toList()))
        testProgram.setMat4Float(viewMat.flatColListFloat(), "view")

        var projectionMat = Matrix.identity(4)

        projectionMat = createPerspectiveProjection(45.0, 1080.0 / 720.0, 0.1, 100.0)

        testProgram.setMat4Float(projectionMat.flatColListFloat(), "projection")

        glEnable(GL_DEPTH_TEST)
        // Clear the framebuffer
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        glDrawArrays(GL_TRIANGLES, 0, 36)
    }

    context.free()
    GLContext.cleanup()
}
