package hadoopij.unittest;

import hadoopij.hij.Converter;
import hipi.image.FloatImage;
import hipi.image.ImageHeader;
import hipi.image.ImageHeader.ImageType;
import hipi.imagebundle.AbstractImageBundle;
import hipi.imagebundle.HipiImageBundle;
import ij.IJ;
import ij.ImagePlus;

import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.junit.Before;
import org.junit.Test;

public class ConverterTest {

	@Before
	public void setUp() throws IOException {
		Configuration conf = new Configuration();
		HipiImageBundle hib = new HipiImageBundle(new Path("/tmp/bundle-iiis.hib"), conf);
		hib.open(AbstractImageBundle.FILE_MODE_WRITE, true);
		hib.addImage(new FileInputStream("data/test/0.jpg"), ImageType.JPEG_IMAGE);
		hib.addImage(new FileInputStream("data/test/1.jpg"), ImageType.JPEG_IMAGE);
		hib.close();
	}

	@Test
	public void testFloatImage2ImagePlus() throws IOException {
		Configuration conf = new Configuration();
		HipiImageBundle hib = new HipiImageBundle(new Path("/tmp/bundle-iiis.hib"), conf);
		hib.open(AbstractImageBundle.FILE_MODE_READ, true);
		while(hib.hasNext()){
			ImageHeader ih = hib.next();
			FloatImage fi = hib.getCurrentImage();
			ImagePlus ip = Converter.floatImage2ImagePlus(ih.toString(), fi);
			System.out.println(ih.toString() + " ByteArray Length: " + fi.getData().length);
		}
	}

	//@Test
	//public void testIJ() throws IOException {
	//	Configuration conf = new Configuration();
	//	HipiImageBundle hib = new HipiImageBundle(new Path("/tmp/bundle-iiis.hib"), conf);
	//	hib.open(AbstractImageBundle.FILE_MODE_READ, true);
	//	while(hib.hasNext()){
	//		ImageHeader ih = hib.next();
	//		FloatImage fi = hib.getCurrentImage();
	//		ImagePlus ip = Converter.floatImage2ImagePlus(ih.toString(), fi);
	//		FloatImage tempfi = Converter.imagePlus2FloatImage(ip);
	//		ImagePlus ip2 = Converter.floatImage2ImagePlus(ih.toString(), tempfi);
	//		try {
	//			IJ.run(ip, "8-bit", "");
	//			IJ.saveAs(ip, "PNG", "/tmp/" + ih.toString() + ".png");
	//			IJ.saveAs(ip2, "PNG", "/tmp/" + ih.toString() + "_conv.png");
	//		} catch (HeadlessException he) {
	//			System.out.println(he.getMessage());
	//		}
	//		System.out.println(ih.toString() + " ByteArray Length: " + fi.getData().length);
	//		System.out.println(ih.toString() + "ByteArray Length: " + tempfi.getData().length);
	//	}
  	//}

	@Test
	public void testResult() throws IOException {
		Configuration conf = new Configuration();
		HipiImageBundle hib = new HipiImageBundle(new Path("/tmp/bundle-iiis.hib"), conf);
		hib.open(AbstractImageBundle.FILE_MODE_READ, true);
		while(hib.hasNext()){
			ImageHeader ih = hib.next();
			FloatImage fi = hib.getCurrentImage();
			ImagePlus ip = Converter.floatImage2ImagePlus(ih.toString(), fi);
			int width = ip.getWidth();
			int height = ip.getHeight();
			int channel = ip.getChannel();
			System.out.println("width: " + width + " height: " + height + " channel: " + channel);
			BufferedImage bi = ip.getBufferedImage();
			//BufferedImage bi = Converter.floatImage2BufferedImage(fi);
			//int width = bi.getWidth();
			//int height = bi.getHeight();
			int[] biData = bi.getRGB(0, 0, width, height, null, 0, width);
			//IJ.run(ip, "Size...", "width=32 height=16 average interpolation=Bilinear");
			FloatImage tempfi = Converter.imagePlus2FloatImage(ip);
			//try {
//			//	IJ.run(ip, "8-bit", "");
			//	IJ.saveAs(ip, "PNG", "/tmp/" + ih.toString() + ".png");
			//	IJ.saveAs(ip2, "PNG", "/tmp/" + ih.toString() + "_conv.png");
			//} catch (HeadlessException he) {
			//	System.out.println(he.getMessage());
			//}
			String rawStr = Arrays.toString(Arrays.copyOfRange(fi.getData(), 0, 10));
			String midStr = Arrays.toString(Arrays.copyOfRange(biData, 0, 10));
			String resStr = Arrays.toString(Arrays.copyOfRange(tempfi.getData(), 0, 10));
			System.out.println("Raw FloatImage: " + rawStr);
			System.out.println("mid int data: " + midStr);
			System.out.println("Converter Result: " + resStr);
		}
	}
}
