package opengl

import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL20.*


class GLProgram(vertexName: String, fragmentName: String) {
    private val programID: Int = glCreateProgram()

    init {
        val classLoader: ClassLoader = Thread.currentThread().contextClassLoader
        val vertexStr = classLoader.getResourceAsStream(vertexName)?.reader()?.readText()
        val fragmentStr = classLoader.getResourceAsStream(fragmentName)?.reader()?.readText()

        val vertexShader = glCreateShader(GL_VERTEX_SHADER)
        glShaderSource(vertexShader, vertexStr!!)
        glCompileShader(vertexShader)
        checkCompileErrors(vertexShader, "SHADER")

        val fragmentShader = glCreateShader(GL_FRAGMENT_SHADER)
        glShaderSource(fragmentShader, fragmentStr!!)
        glCompileShader(fragmentShader)
        checkCompileErrors(fragmentShader, "SHADER")

        glAttachShader(programID, vertexShader)
        glAttachShader(programID, fragmentShader)
        glLinkProgram(programID)
        checkCompileErrors(programID, "PROGRAM")

    }

    fun use() {
        glUseProgram(programID)
    }

    fun setVec3Float(data: FloatArray, name: String) {
        val location = glGetUniformLocation(programID, name)
        glUniform3fv(location, data)
    }

    fun setMat4Float(data: List<Float>, name: String){
        val location = glGetUniformLocation(programID, name)
        glUniformMatrix4fv(location, false, data.toFloatArray())
    }

    private fun checkCompileErrors(shader: Int, type: String) {
        val success = IntArray(1)
        if (type == "PROGRAM") {
            glGetProgramiv(shader, GL_LINK_STATUS, success)
            if (success[0] == GL_FALSE) {
                val info = GL20.glGetProgramInfoLog(shader)
                System.err.println("Error compiling program\n$info\n------------------------------------------")
            }
        } else {
            glGetShaderiv(shader, GL_COMPILE_STATUS, success)
            if (success[0] == GL_FALSE) {
                val info = GL20.glGetShaderInfoLog(shader)
                System.err.println("Error compiling shader of type $type\n$info\n------------------------------------------")
            }
        }
    }
}