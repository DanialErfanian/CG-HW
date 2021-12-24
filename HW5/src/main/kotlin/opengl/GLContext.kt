package opengl

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWCursorPosCallback
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.opengl.GL
import org.lwjgl.system.MemoryUtil.NULL


class GLContext private constructor(private val window: Long, val viewPort: ViewPort) {
    companion object {
        private val DEFAULT_WINDOW_SIZE = ViewPort(1080, 720)

        private var initialized = false

        private val defaultKeyCallback: GLFWKeyCallback by lazy {
            object : GLFWKeyCallback() {
                override fun invoke(
                    window: Long,
                    key: Int,
                    scancode: Int,
                    action: Int,
                    mods: Int
                ) {

                    if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                        glfwSetWindowShouldClose(window, true)
                    }

                }
            }
        }

        private val errorCallBack: GLFWErrorCallback by lazy { GLFWErrorCallback.createPrint(System.err) }

        private fun init() {
            if (!initialized) {
                // Initialize GLFW. Most GLFW functions will not work before doing this.
                if (!glfwInit()) {
                    throw IllegalStateException("Unable to initialize GLFW")
                }
                initialized = true
            }
        }

        fun createWindow(title: String, windowSize: ViewPort = DEFAULT_WINDOW_SIZE): GLContext {
            init()

            // Configure our window
            glfwDefaultWindowHints()
            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
            glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE)

            // Create the window
            val window = glfwCreateWindow(windowSize.iWidth, windowSize.iHeight, title, NULL, NULL)
            if (window == NULL) {
                throw RuntimeException("Failed to create the GLFW window")
            }

            // Get the resolution of the primary monitor
            val vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())

            // Center our window
            glfwSetWindowPos(
                window,
                (vidMode!!.width() - windowSize.iWidth) / 2,
                (vidMode.height() - windowSize.iHeight) / 2
            )
            val context = GLContext(window, windowSize)
            context.setKeyCallback(defaultKeyCallback)
            return context
        }

        fun cleanup() {
            glfwTerminate()
            errorCallBack.free()
        }
    }

    private var keyCallback: GLFWKeyCallback? = null
    private var cursorPosCallback: GLFWCursorPosCallback? = null

    fun setKeyCallback(keyCallback: GLFWKeyCallback) {
        this.keyCallback = glfwSetKeyCallback(window, keyCallback)
    }

    fun setCursorPosCallback(cursorPosCallback: GLFWCursorPosCallback) {
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
        this.cursorPosCallback = glfwSetCursorPosCallback(window, cursorPosCallback)
    }

    fun use() {
        // Make the OpenGL context current
        glfwMakeContextCurrent(window)
        // Enable v-sync
        glfwSwapInterval(1)

        GL.createCapabilities()
    }

    fun drawLoop(drawMethod: () -> Unit) {
        while (!glfwWindowShouldClose(window)) {
            drawMethod()
            glfwSwapBuffers(window)
            glfwPollEvents()
        }
    }

    fun show() {
        glfwShowWindow(window)
    }

    fun hide() {
        glfwHideWindow(window)
    }

    fun free() {
        glfwDestroyWindow(window)
        keyCallback?.free()
        cursorPosCallback?.free()
    }
}

class ViewPort(val width: Float, val height: Float) {
    constructor(width: Int, height: Int) : this(width.toFloat(), height.toFloat())
    val iWidth = width.toInt()
    val iHeight = height.toInt()
}