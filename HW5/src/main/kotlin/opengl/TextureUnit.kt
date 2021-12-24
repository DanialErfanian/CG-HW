package opengl

import org.apache.commons.io.IOUtils
import org.lwjgl.opengl.GL33.*


class TextureUnit(imageResourceAddress: String) {
    private companion object {
        private var privateCounter = 0

        fun getFreeUnitAndIncrement(): Int = this.privateCounter++
    }

    val unitNumber: Int
    val textureID: Int
    val imageWidth: Int
    val imageHeight: Int

    init {
        val classLoader: ClassLoader = Thread.currentThread().contextClassLoader
        val imageData = GLImage.loadImage(classLoader.getResourceAsStream(imageResourceAddress)!!.readAllBytes())

        imageWidth = imageData.width
        imageHeight = imageData.height

        unitNumber = getFreeUnitAndIncrement()
        textureID = glGenTextures()
        glActiveTexture(GL_TEXTURE0 + unitNumber)
        glBindTexture(GL_TEXTURE_2D, textureID)

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        val internalFormat = if (imageData.channelCnt == 3) GL_RGB else GL_RGBA

        glTexImage2D(
            GL_TEXTURE_2D,
            0,
            internalFormat,
            imageData.width,
            imageData.height,
            0,
            GL_RGBA,
            GL_UNSIGNED_BYTE,
            imageData.image
        )

        imageData.free()
    }
}