package main.kotlin

import main.kotlin.model.ProcessType

class ImageExaminer {

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            val imageProcessor = ImageProcessor()

            val IMAGE_PATH = "./images/Night_Time.jpg"

            imageProcessor.processImage(IMAGE_PATH, ProcessType.SHIFT, shiftAmount = 40)
        }
    }

}