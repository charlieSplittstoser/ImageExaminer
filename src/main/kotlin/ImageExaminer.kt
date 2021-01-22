package main.kotlin

class ImageExaminer {

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            val imageProcessor = ImageProcessor()

            imageProcessor.processImage("./images/canvasimage.jpg")
        }
    }

}