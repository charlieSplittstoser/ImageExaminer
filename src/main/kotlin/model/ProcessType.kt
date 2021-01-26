package main.kotlin.model


enum class ProcessType {
    SHIFT,                      // Shift all pixels' RGB colors by x
    OVERWRITE_COMMON_COLOR,     // Overwrites the most common RGB value in an image with a new color
    RANDOMIZE_COLORS,           // Randomizes all colors in an image (Pixels which were previously the same color will share a new random color value)
    RANDOMIZE_PIXELS,           // Assigns a random color value to each pixel, completely blurring the image
}