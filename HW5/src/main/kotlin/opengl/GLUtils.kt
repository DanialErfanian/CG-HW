package opengl

import org.lwjgl.opengl.GL33

fun checkGLError() {
    val err = GL33.glGetError()
    if (err != GL33.GL_NO_ERROR)
        error("GL error: $err")
}