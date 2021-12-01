package opengl

import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer
import java.nio.ByteOrder


class GLImage internal constructor(val width: Int, val height: Int, val channelCnt: Int, val image: ByteBuffer?) {

    companion object {
        fun loadImage(byteArr: ByteArray): GLImage {
            val byteBuf = ByteBuffer.allocateDirect(byteArr.size).apply {
                order(ByteOrder.nativeOrder())
                put(byteArr)
                position(0)
            }
            var image: ByteBuffer?
            var width: Int
            var height: Int
            var channelCnt: Int
            STBImage.stbi_set_flip_vertically_on_load(true)
            MemoryStack.stackPush().use { stack ->
                val comp = stack.mallocInt(1)
                val w = stack.mallocInt(1)
                val h = stack.mallocInt(1)
                image = STBImage.stbi_load_from_memory(byteBuf, w, h, comp, 4)
                if (image == null) {
                    System.err.println("Couldn't load $byteArr")
                }
                width = w.get()
                height = h.get()
                channelCnt = comp.get()
            }
            return GLImage(width, height, channelCnt, image)
        }
    }

    fun free() {
        image?.clear()
    }
}