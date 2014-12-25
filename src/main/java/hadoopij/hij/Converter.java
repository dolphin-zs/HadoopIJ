package hadoopij.hij;

import ij.ImagePlus;

import java.awt.image.BufferedImage;

import hipi.image.FloatImage;

public class Converter {
	public static ImagePlus floatImage2ImagePlus(String imageName, FloatImage fi) {
		int width = fi.getWidth();
		int height = fi.getHeight();
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (fi.getBands() == 1) {
					bi.setRGB(x, y, (int)(fi.getPixel(x, y, 0) * 255));
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
		ImagePlus ip = new ImagePlus(imageName, bi);
		return ip;
	}

	public static FloatImage imagePlus2FloatImage(ImagePlus ip) {
		int[] conf = ip.getDimensions();
		int width = conf[0];
		int height = conf[1];
		int band = conf[2];
		BufferedImage bi = ip.getBufferedImage();
		FloatImage fi = new FloatImage(width, height, band);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (band == 1) {
					fi.setPixel(x, y, 0, (float) bi.getRGB(x, y) / 255);
				} else if (band == 3) {
					int rgb = bi.getRGB(x, y);
					int mask = (1 << 9) - 1;
					int blue = rgb & mask;
					rgb >>= 8;
					int green = rgb & mask;
					rgb >>= 8;
					int red = rgb & mask;
					fi.setPixel(x, y, 0, (float) red / 255);
					fi.setPixel(x, y, 1, (float) green / 255);
					fi.setPixel(x, y, 2, (float) blue / 255);
				}
			}
		}
		return fi;
	}

}
