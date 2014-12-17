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
				int red = (int)(fi.getPixel(x, y, 0) * 255);
				int green = (int)(fi.getPixel(x, y, 1) * 255);
				int blue = (int)(fi.getPixel(x, y, 2) * 255);
				int rgb = red;
				rgb = (rgb << 8) + green;
				rgb = (rgb << 8) + blue;
				bi.setRGB(x, y, rgb);
			}
		}
		ImagePlus ip = new ImagePlus(imageName, bi);
		
		return ip;
	}

}
