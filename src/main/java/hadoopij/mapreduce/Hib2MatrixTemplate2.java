package hadoopij.mapreduce;

import hadoopij.hij.Converter;
import hipi.image.FloatImage;
import hipi.image.ImageHeader;
import hipi.imagebundle.mapreduce.ImageBundleInputFormat;
import ij.IJ;
import ij.ImagePlus;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by zhengshun on 30/12/14.
 */
public class Hib2MatrixTemplate2 {

    public static class H2MMapper extends Mapper<ImageHeader, FloatImage, IntWritable, Text> {

        @Override
        public void map(ImageHeader key, FloatImage value, Context context)
                throws IOException, InterruptedException {
            if (value != null) {
                ImagePlus imp = Converter.floatImage2ImagePlus(key.toString(), value, true);

                IJ.run(imp, "8-bit", "");
                //IJ.run(imp, "Add Noise", "");
                //IJ.run(imp, "Gaussian Blur...", "sigma=2");
                IJ.run(imp, "Size...", "width=32 height=16 average interpolation=Bilinear");
                //imp.show();
                IJ.saveAs(imp, "PNG", "/tmp/out0.png");

                FloatImage fimg = Converter.imagePlus2FloatImage(imp, false);
                String floatArrayStr = Arrays.toString(fimg.getData());
                String textStr = floatArrayStr.substring(1, floatArrayStr.length() - 1);
                context.write(new IntWritable(1), new Text(textStr));
            }
        }
    }

    public static class H2MReducer extends Reducer<IntWritable, Text, IntWritable, Text> {
        @Override
        public void reduce(IntWritable key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            for (Text value : values) {
                context.write(null, value);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        if (args.length < 2) {
            System.out.println("Usage: Hib2MatrixTemplate2 <input hib> <output dir>");
            System.exit(0);
        }
        String inputPath = args[0];
        String outputPath = args[1];

        Job job = new Job(conf, "Hib2MatrixTemplate2");
        job.setJarByClass(Hib2MatrixTemplate2.class);

        // Set Mapper Reducer
        job.setMapperClass(H2MMapper.class);
        job.setReducerClass(H2MReducer.class);

        // Set input-output formats
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(Text.class);
        job.setInputFormatClass(ImageBundleInputFormat.class);

        // Set input-output paths
        FileInputFormat.setInputPaths(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        // Set reduce task number
        //job.setNumReduceTasks(1);

        System.exit(job.waitForCompletion(true) ? 0 : 1);

    }
}
