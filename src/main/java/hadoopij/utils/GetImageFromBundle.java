package hadoopij.utils;

import hadoopij.hij.Converter;
import hipi.image.FloatImage;
import hipi.image.ImageHeader;
import hipi.imagebundle.AbstractImageBundle;
import hipi.imagebundle.HipiImageBundle;
import ij.IJ;
import ij.ImagePlus;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.awt.*;
import java.io.IOException;

/**
 * Created by zhangyang on 14/12/17.
 */
public class GetImageFromBundle {
    public static void saveAsImage(String hibFile, String imgType, String outdir) throws IOException {
        Configuration conf = new Configuration();
        HipiImageBundle hib = new HipiImageBundle(new Path(hibFile), conf);
        hib.open(AbstractImageBundle.FILE_MODE_READ, true);
        int cnt = 0;
        while(hib.hasNext()){
            ImageHeader ih = hib.next();
            FloatImage fi = hib.getCurrentImage();
            ImagePlus ip = Converter.floatImage2ImagePlus(ih.toString(), fi, true);
            try {
                cnt++;
                IJ.saveAs(ip, imgType, outdir + ih.toString() + "." + imgType.toLowerCase());
                System.out.println("file " + cnt + " " + ih.toString());
            } catch (HeadlessException he) {
                System.out.println(he.getMessage());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage: GetImageFromBundle <input dir.hib> <output dir>");
            System.exit(0);
        }
        String strInPath = args[0];
        String strOutFile = args[1];
        saveAsImage(strInPath, "JPG", strOutFile);
    }

}
