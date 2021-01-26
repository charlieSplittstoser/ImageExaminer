package main.kotlin

import main.kotlin.model.Pixel
import main.kotlin.model.ProcessType
import main.kotlin.model.RGB
import java.awt.Color
import java.io.File
import java.util.concurrent.atomic.AtomicInteger
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

class ImageProcessor {

    fun processImage(imagePath: String, processType: ProcessType, shiftAmount: Int = 0, overwriteColor:RGB = RGB(244,66 ,66)) = try {
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
                val originalRed = pixelColor and 0x00ff0000 shr 16
                val originalGreen = pixelColor and 0x0000ff00 shr 8
                val originalBlue = pixelColor and 0x000000ff

                val pixel = Pixel(x, y, RGB(originalRed, originalGreen, originalBlue))
                val key = "rgb(${originalRed}, ${originalGreen}, ${originalBlue})"

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
                    mostCommonColor = RGB(originalRed, originalGreen, originalBlue)
                }

                /* Store all original pixel values to recreate the image later */
                if (!pixelMap.contains(key)) {
                    pixelMap[key] = ArrayList()
                }
                pixelMap[key]?.add(pixel)
                pixelList.add(pixel)

            }
        }
        println("MOST COMMON COLOR: rgb(${mostCommonColor.red}, ${mostCommonColor.green}, ${mostCommonColor.blue}): $highestColorCount")

        val remasteredImage = if (processType == ProcessType.SHIFT) {
            shiftRGB(image, pixelList, shiftAmount)
        } else if (processType == ProcessType.RANDOMIZE_COLORS) {
            randomizeColors(image, pixelMap)
        } else if (processType == ProcessType.RANDOMIZE_PIXELS) {
            randomizePixels(image, pixelList)
        } else {
            overwriteMostCommonColor(image, pixelList, pixelMap, mostCommonColor, overwriteColor)
        }

        saveRemasteredImage(remasteredImage, imagePath)


        println("COMPLETE!")

    } catch (e: Exception) {
        println(e.message)
    }

    private fun rebuildOriginalImage(image: BufferedImage, pixelList: ArrayList<Pixel>): BufferedImage {
        println("Rebuilding original image...")
        val remasteredImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB)
        for (i in 0 until pixelList.size) {
            val color = Color(pixelList[i].rgb.red, pixelList[i].rgb.green, pixelList[i].rgb.blue).rgb
            remasteredImage.setRGB(pixelList[i].x, pixelList[i].y, color)
        }

        return remasteredImage
    }

    private fun overwriteMostCommonColor(image: BufferedImage, pixelList: ArrayList<Pixel>,
                                         pixelMap: HashMap<String, ArrayList<Pixel>>,
                                         mostCommonColor: RGB,
                                         overwriteColor: RGB): BufferedImage {
        println("Overwriting the most common color...")
        val remasteredImage = rebuildOriginalImage(image, pixelList)
        val pixelMapKey = "rgb(${mostCommonColor.red}, ${mostCommonColor.green}, ${mostCommonColor.blue})"
        for (i in 0 until pixelMap[pixelMapKey]!!.size) {
            val color = Color(overwriteColor.red, overwriteColor.blue, overwriteColor.green).rgb
            remasteredImage.setRGB(pixelMap[pixelMapKey]!![i].x, pixelMap[pixelMapKey]!![i].y, color)
        }

        return remasteredImage
    }

    private fun shiftRGB(image: BufferedImage, pixelList: ArrayList<Pixel>, shiftAmount: Int): BufferedImage {
        val remasteredImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB)
        for (i in 0 until pixelList.size) {
            val color = Color(shiftRGBValue(pixelList[i].rgb.red, shiftAmount), shiftRGBValue(pixelList[i].rgb.green, shiftAmount), shiftRGBValue(pixelList[i].rgb.blue, shiftAmount)).rgb
            remasteredImage.setRGB(pixelList[i].x, pixelList[i].y, color)
        }

        return remasteredImage
    }

    private fun randomizeColors(image: BufferedImage, pixelMap: HashMap<String, ArrayList<Pixel>>): BufferedImage {
        val remasteredImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB)

        for ((rgbKey, pixels) in pixelMap) {
            val color = Color(getRandomRGBValue(), getRandomRGBValue(), getRandomRGBValue()).rgb
            for (i in 0 until pixels.size) {
                remasteredImage.setRGB(pixelMap[rgbKey]!![i].x, pixelMap[rgbKey]!![i].y, color)
            }
        }

        return remasteredImage
    }
    private fun randomizePixels(image: BufferedImage, pixelList: ArrayList<Pixel>): BufferedImage {
        val remasteredImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB)

        for (i in 0 until pixelList.size) {
            val color = Color(getRandomRGBValue(), getRandomRGBValue(), getRandomRGBValue()).rgb
            remasteredImage.setRGB(pixelList[i].x, pixelList[i].y, color)

        }

        return remasteredImage
    }


    private fun shiftRGBValue(value: Int, shift: Int): Int {
        return when {
            value + shift > 255 -> {
                255
            }
            value + shift < 0 -> {
                0
            }
            else -> {
                shift + value
            }
        }
    }

    private fun saveRemasteredImage(remasteredImage: BufferedImage, imagePath: String) {
        val remasteredImagePath = "${imagePath.substring(0, imagePath.length - 4)}remix.png"
        println("Saving remastered image: $remasteredImagePath")
        val remasteredImageFile = File(remasteredImagePath)
        ImageIO.write(remasteredImage, "png", remasteredImageFile)
    }

    private fun getRandomRGBValue(): Int {
        return (0..255).random()
    }

}