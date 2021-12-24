package opengl

import mutils.Matrix
import mutils.Vector
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER


fun Boolean.toInt() = if (this) 1 else 0

class GLProgram(vertexName: String, fragmentName: String, geometryName: String? = null) {
    private val programID: Int = glCreateProgram()

    init {
        val classLoader: ClassLoader = Thread.currentThread().contextClassLoader
        val vertexStr = classLoader.getResourceAsStream(vertexName)?.reader()?.readText()
        val fragmentStr = classLoader.getResourceAsStream(fragmentName)?.reader()?.readText()
        val geometryStr = geometryName?.let {
            classLoader.getResourceAsStream(it)?.reader()?.readText()
        }

        val vertexShader = glCreateShader(GL_VERTEX_SHADER)
        glShaderSource(vertexShader, vertexStr!!)
        glCompileShader(vertexShader)
        checkCompileErrors(vertexShader, "SHADER")
        glAttachShader(programID, vertexShader)

        val fragmentShader = glCreateShader(GL_FRAGMENT_SHADER)
        glShaderSource(fragmentShader, fragmentStr!!)
        glCompileShader(fragmentShader)
        checkCompileErrors(fragmentShader, "SHADER")
        glAttachShader(programID, fragmentShader)

        geometryStr?.let { str ->
            val geometryShader = glCreateShader(GL_GEOMETRY_SHADER)
            glShaderSource(geometryShader, str)
            glCompileShader(geometryShader)
            checkCompileErrors(geometryShader, "SHADER")
            glAttachShader(programID, geometryShader)
        }

        glLinkProgram(programID)
        checkCompileErrors(programID, "PROGRAM")

    }

    fun use() {
        glUseProgram(programID)
    }

    fun setBool(name: String, value: Boolean) = glUniform1i(glGetUniformLocation(programID, name), value.toInt())

    fun setInt(name: String, value: Int) = glUniform1i(glGetUniformLocation(programID, name), value)

    fun setFloat(name: String, value: Float) = glUniform1f(glGetUniformLocation(programID, name), value)

    fun setVec3Float(name: String, v1: Float, v2: Float, v3: Float) = glUniform3f(glGetUniformLocation(programID, name), v1, v2, v3)

    fun setVec3Float(name: String, value: FloatArray) = glUniform3fv(glGetUniformLocation(programID, name), value)

    fun setVec3Float(name: String, value: Vector) = setVec3Float(name, value.toFloatList().toFloatArray())

    fun setVec4Float(name: String, v1: Float, v2: Float, v3: Float, v4: Float) = glUniform4f(glGetUniformLocation(programID, name), v1, v2, v3, v4)

    fun setVec4Float(name: String, value: FloatArray) = glUniform4fv(glGetUniformLocation(programID, name), value)

    fun setVec4Float(name: String, value: Vector) = setVec4Float(name, value.toFloatList().toFloatArray())

    fun setMat4Float(name: String, value: FloatArray, transpose: Boolean = false) = glUniformMatrix4fv(
        glGetUniformLocation(programID, name), transpose, value)

    fun setMat4Float(name: String, value: Matrix, transpose: Boolean = false) = setMat4Float(name, value.flatColListFloat().toFloatArray(), transpose)

    private fun checkCompileErrors(shader: Int, type: String) {
        val success = IntArray(1)
        if (type == "PROGRAM") {
            glGetProgramiv(shader, GL_LINK_STATUS, success)
            if (success[0] == GL_FALSE) {
                val info = glGetProgramInfoLog(shader)
                System.err.println("Error compiling program\n$info\n------------------------------------------")
            }
        } else {
            glGetShaderiv(shader, GL_COMPILE_STATUS, success)
            if (success[0] == GL_FALSE) {
                val info = glGetShaderInfoLog(shader)
                System.err.println("Error compiling shader of type $type\n$info\n------------------------------------------")
            }
        }
    }
}