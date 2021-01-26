# Image Examiner

This was a project I worked on because I was curious how to manipulate the individual pixels of an image through code. I ended up discovering a couple things pretty quickly:

- Image brightness is easily manipulated through increasing or decreasing the RGB values of each image (This can also have a filter effect on the photo)
- Through randomizing all of the unique RGB color values in an image, photoshop edits of an image can be discovered rather easily

## Operations

### Shift
This operation shifts all RGB color values by some value *delta* Δ

Each pixel undergoes the following transformation:
	
	RGB(x, y, z) ---> RGB(x + Δ, y + Δ, z + Δ)

This shift can be used to brighten or darken an image

### Overwrite Most Common RGB Value
This operation will find the most frequent RGB pixel value and replace it with some user-defined value.

Each pixel undergoes the following transformation:
	
	RGB(x, y, z) ---> RGB(x1, y1, z1)

### Randomize Image Colors
This operation will randomize all colors of an image. Any pixels that originally had the same RGB value will also share the same random RGB value after the operation. Depending on the image, it may be possible to faintly see the outlines of the original image.

	RGB(x, y, z) ---> RGB(r1, r2, r3)

This operation allows certain types of photoshop edits to be seen. For example, here is a before and after image of a photoshopped woman:

Before:
![alt text](https://i.imgur.com/C0Doq7V.png)

After:
![alt text](https://i.imgur.com/GxspuqZ.png)

The areas that are photoshopped become more clear in the randomized color version of this image. This is because someone used a brush or pencil tool to repeatedly color the same RGB value into one sepcific area.

### Randomize Image Pixels

This operation will assign each pixel in an image a completely random color. This essentially blurs the image and there is no way to recognize what the original image was.

	RGB(x, y, z) ---> RGB(r1, r2, r3)
