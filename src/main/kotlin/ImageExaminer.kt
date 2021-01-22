package main.kotlin

class ImageExaminer {

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            val imageProcessor = ImageProcessor()

            val IMAGE_PATH = "./images/canvasimage.jpg"

            imageProcessor.processImage(IMAGE_PATH)
        }
    }

}