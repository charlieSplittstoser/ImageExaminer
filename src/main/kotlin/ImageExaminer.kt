package main.kotlin

import main.kotlin.model.Operation
import main.kotlin.model.RGB

class ImageExaminer {

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            val imageProcessor = ImageProcessor()

            val ORIGINAL_IMAGE_PATH = "./images/example_photoshop.jpg"

            imageProcessor.processImage(ORIGINAL_IMAGE_PATH, Operation.RANDOMIZE_COLORS, shiftAmount = 40, overwriteColor = RGB(244, 66, 66))
        }
    }

}