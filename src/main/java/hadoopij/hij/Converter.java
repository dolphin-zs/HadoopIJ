package hadoopij.hij;

import ij.ImagePlus;

import java.awt.image.BufferedImage;

import hipi.image.FloatImage;

public class Converter {
	static int rgbMax = 256 * 256 * 256;

	public static ImagePlus floatImage2ImagePlus(String imageName, FloatImage fi, boolean flagRGB) {
		int width = fi.getWidth();
		int height = fi.getHeight();

		BufferedImage bi = null;
		System.out.println("FloatImage -> ImagePlus: flagRGB = " + flagRGB + " bands = " + fi.getBands());
		if (flagRGB) {
			bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					if (fi.getBands() == 1) {
						bi.setRGB(x, y, (int) (fi.getPixel(x, y, 0) * rgbMax));
					} else if (fi.getBands() == 3) {
						int red = (int) (fi.getPixel(x, y, 0) * 255);
						int green = (int) (fi.getPixel(x, y, 1) * 255);
						int blue = (int) (fi.getPixel(x, y, 2) * 255);
						int rgb = red;
						rgb = (rgb << 8) + green;
						rgb = (rgb << 8) + blue;
						bi.setRGB(x, y, rgb);
					}
				}
			}
		} else {
			bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					if (fi.getBands() == 1) {
						int base = (int) (fi.getPixel(x, y, 0) * 255);
						int rgb = base;
						rgb = (rgb << 8) + base;
						rgb = (rgb << 8) + base;
						//int base = (int) fi.getPixel(x, y, 0) * rgbMax;
						bi.setRGB(x, y, rgb);
					} else {
						System.out.println("Error in FloatImage dimension");
					}
				}
			}
		}
		ImagePlus ip = new ImagePlus(imageName, bi);
		return ip;
	}

	public static FloatImage imagePlus2FloatImage(ImagePlus ip, boolean flagRGB) {
		int[] conf = ip.getDimensions();
		int width = conf[0];
		int height = conf[1];
		int band = conf[2];
		BufferedImage bi = ip.getBufferedImage();
		FloatImage fi = new FloatImage(width, height, band);
		System.out.println("ImagePlus -> FloatImage: flagRGB = " + flagRGB + " bands = " + fi.getBands());
		if (flagRGB) {
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					if (band == 1) {
						int rgb = bi.getRGB(x, y) & 0xffffff;
						fi.setPixel(x, y, 0, (float) rgb / rgbMax);
					} else if (band == 3) {
						System.out.println("Error in ImagePlus dimension");
					}
				}
			}
		} else {
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					if (band == 1) {
						int rgb = bi.getRGB(x, y) & 0xff;
						fi.setPixel(x, y, 0, (float) rgb / 255);
					} else {
						System.out.println("Error in ImagePlus dimension");
					}
				}
			}
		}
		return fi;
	}

}
