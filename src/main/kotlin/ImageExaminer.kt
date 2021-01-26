package main.kotlin

import main.kotlin.model.ProcessType

class ImageExaminer {

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            val imageProcessor = ImageProcessor()

            val IMAGE_PATH = "./images/ps.jpg"

            imageProcessor.processImage(IMAGE_PATH, ProcessType.RANDOMIZE_COLORS, shiftAmount = 40)
        }
    }

}