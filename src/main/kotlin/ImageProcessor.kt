package main.kotlin

import main.kotlin.model.Pixel
import main.kotlin.model.RGB
import java.awt.Color
import java.io.File
import java.util.concurrent.atomic.AtomicInteger
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

class ImageProcessor {

    fun processImage(imagePath: String) = try {
        val file = File(imagePath)
        val image = ImageIO.read(file)
        val colorCountMap = HashMap<String, Int>()
        val pixelMap = HashMap<String, ArrayList<Pixel>>()
        val pixelList = ArrayList<Pixel>()
        var highestColorCount = 0
        var mostCommonColor = RGB(0, 0, 0)

        println("IMAGE INFO:")
        println("Path: ${file.absolutePath}")
        println("File size: ${file.length() / 1024}kb")
        println("Image size: ${image.width} x ${image.height}\n")

        println("Scanning image...")

        /* Read all pixel rgb values */
        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                val pixelColor = image.getRGB(x, y)
                val red = pixelColor and 0x00ff0000 shr 16
                val green = pixelColor and 0x0000ff00 shr 8
                val blue = pixelColor and 0x000000ff

                val pixel = Pixel(x, y, RGB(red, green, blue))
                val key = "rgb(${red}, ${green}, ${blue})"

                /* Store counts of each color in a map */
                val value = 0
                if (colorCountMap[key] != null) {
                    val value = colorCountMap[key]
                    colorCountMap[key] = AtomicInteger(value!!).incrementAndGet()
                } else {
                    colorCountMap[key] = 1
                }

                /* If one color count surpasses the old highest, reset the most common color and highest count */
                if (colorCountMap[key]!! > highestColorCount) {
                    highestColorCount = colorCountMap[key]!!
                    mostCommonColor = RGB(red, green, blue)
                }

                /* Store all original pixel values in order to recreate the image later */
                if (!pixelMap.contains(key)) {
                    pixelMap[key] = ArrayList()
                }

                pixelMap[key]?.add(pixel)
                pixelList.add(pixel)


                //println("[${x}, ${y}] rgb(${red}, ${green}, ${blue})")
                //println("COUNT: ${colorCountMap[key]}")

            }
        }
        println("MOST COMMON COLOR: rgb(${mostCommonColor.red}, ${mostCommonColor.green}, ${mostCommonColor.blue}): $highestColorCount")

        val remasteredImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB)

        println("Rebuilding original image...")
        /* Rebuild the image identical to the original image */
        for (i in 0 until pixelList.size) {
            val color = Color(pixelList[i].rgb.red, pixelList[i].rgb.green, pixelList[i].rgb.blue).rgb
            remasteredImage.setRGB(pixelList[i].x, pixelList[i].y, color)
        }

        println("Overwriting the most common color...")
        /* Overwrite the most common color pixels with red */
        val pixelMapKey = "rgb(${mostCommonColor.red}, ${mostCommonColor.green}, ${mostCommonColor.blue})"
        for (i in 0 until pixelMap[pixelMapKey]!!.size) {
            val color = Color(252, 3, 3).rgb
            remasteredImage.setRGB(pixelMap[pixelMapKey]!![i].x, pixelMap[pixelMapKey]!![i].y, color)
        }

        val remasteredImagePath = "${imagePath.substring(0, imagePath.length - 4)}remix.png"
        println("Saving remastered image: $remasteredImagePath")
        /* Save remastered image */
        val remasteredImageFile = File(remasteredImagePath)
        ImageIO.write(remasteredImage, "png", remasteredImageFile)

        println("COMPLETE!")

    } catch (e: Exception) {
        println(e.message)
    }
}