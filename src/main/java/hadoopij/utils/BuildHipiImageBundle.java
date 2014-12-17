package hadoopij.utils;

import hipi.image.ImageHeader.ImageType;
import hipi.imagebundle.AbstractImageBundle;
import hipi.imagebundle.HipiImageBundle;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;


public class BuildHipiImageBundle {
	private static ArrayList<String> fileList = new ArrayList<String>();
	private static void displayFileList() {
		for(int i=0;i<fileList.size();i++) {
			System.out.println((i+1) + " => " + fileList.get(i));
		}
	}

  private static boolean isJPEGFile(String fn) {
    if(fn.endsWith(".jpg") || fn.endsWith(".jpe") || fn.endsWith(".jpeg")){
      return true;
    } else {
      return false;
    }
  }

	private static void getFile(String filePath) {
		String JPEGImageType = ".jpg";
		File croot = new File(filePath);
		if(!croot.isDirectory()){
			System.out.println(filePath + " is not a directory");
			System.exit(0);
		}
		File[] files = croot.listFiles();
		for(File file:files) {
			String tmpPath = file.getAbsolutePath();
			if(isJPEGFile(tmpPath)){
				fileList.add(tmpPath);
			} else {
				System.out.println("warning: "+tmpPath+" is not a jpg file");
				System.out.println("......neglected");
			}
		}
	}

	private static void createBundle(String fileName) throws IOException {
		Configuration conf = new Configuration();
		System.out.println("--->Create HipiImageBundle " + fileName);
		HipiImageBundle hib = new HipiImageBundle(new Path(fileName), conf);
		hib.open(AbstractImageBundle.FILE_MODE_WRITE, true);
		for(String tP:fileList) {
			hib.addImage(new FileInputStream(tP), ImageType.JPEG_IMAGE);
		}
		hib.close();
		System.out.println("--->SUCCESS");
	}

	public static void main(String[] args) throws IOException {
		if(args.length < 2) {
			System.out.println("Usage: BuildHipiImageBundle <input dir> <output_name.hib>");
			System.exit(0);
		}
		String strInPath = args[0];
		String strOutFile = args[1];
		//String strInPath = "/Users/zhengshun/Current_Space/Berkeley_Project/Img/";
		//String strOutFile = "./Files/bundle-dzs.hib";
		getFile(strInPath);
		displayFileList();
		createBundle(strOutFile);
	}
}
