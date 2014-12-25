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
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.junit.Before;
import org.junit.Test;

public class ConverterTest {

	/*
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
	*/

	@Test
	public void testIJ() throws IOException {
		Configuration conf = new Configuration();
		HipiImageBundle hib = new HipiImageBundle(new Path("/tmp/bundle-iiis.hib"), conf);
		hib.open(AbstractImageBundle.FILE_MODE_READ, true);
		while(hib.hasNext()){
			ImageHeader ih = hib.next();
			FloatImage fi = hib.getCurrentImage();
			ImagePlus ip = Converter.floatImage2ImagePlus(ih.toString(), fi);
			FloatImage tempfi = Converter.imagePlus2FloatImage(ip);
			ImagePlus ip2 = Converter.floatImage2ImagePlus(ih.toString(), tempfi);
			try {
//				IJ.run(ip, "8-bit", "");
				IJ.saveAs(ip, "PNG", "/tmp/" + ih.toString() + ".png");
				IJ.saveAs(ip2, "PNG", "/tmp/" + ih.toString() + "_conv.png");
			} catch (HeadlessException he) {
				System.out.println(he.getMessage());
			}
			System.out.println(ih.toString() + " ByteArray Length: " + fi.getData().length);
			System.out.println(ih.toString() + "ByteArray Length: " + tempfi.getData().length);
		}
  }
}
