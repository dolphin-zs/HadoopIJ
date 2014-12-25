package imagej.unittest;

import org.junit.Before;
import org.junit.Test;

import ij.IJ;
import ij.ImagePlus;

public class ImageJRunTest {
	private ImagePlus imp;
	
	@Before
	public void readImagePlus() {
		String pathJpg = System.getProperty("user.dir") + "/data/test/0.jpg";
		System.out.println("Test " + pathJpg);
		imp = IJ.openImage(pathJpg);
	}
	
	@Test
	public void run() {
		IJ.run(imp, "8-bit", "");
		IJ.run(imp, "Add Noise", "");
		IJ.run(imp, "Gaussian Blur...", "sigma=2");
		IJ.run(imp, "Size...", "width=32 height=16 average interpolation=Bilinear");
		//imp.show();
		IJ.saveAs(imp, "PNG", "/tmp/out0.png");
		System.out.println("Test Successfully!");
	}
	
	public static void main(String[] args) {
		ImageJRunTest irt = new ImageJRunTest();
		irt.readImagePlus();
		irt.run();
	}
}
