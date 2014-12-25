package hadoopij.utils;

import org.apache.hadoop.conf.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.hadoop.fs.*;

/**
 * Created by zhangyang on 14/12/20.
 */
public class LocalHDFSOperations {

    /**
     * Upload a file to HDFS
     * @param source
     * @param destination
     * @throws IOException
     */
    public static void uploadLocal2HDFS(String source, String destination) throws IOException {
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(conf);

        Path src = new Path(source);
        Path dst = new Path(destination);

        hdfs.copyFromLocalFile(src, dst);
        hdfs.close();
    }

    public static void uploadAllLocal2HDFS(String dir, String destination) throws IOException {
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(conf);

        File folder = new File(dir);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && !listOfFiles[i].getName().startsWith(".")) {
                Path src, dst;
                if (dir.endsWith("/")) {
                    src = new Path(dir + listOfFiles[i].getName());
                } else {
                    src = new Path(dir + "/" + listOfFiles[i].getName());
                }
                if (destination.endsWith("/")) {
                    dst = new Path(destination + listOfFiles[i].getName());
                } else {
                    dst = new Path(destination + "/" + listOfFiles[i].getName());
                }
                //System.out.println(src.toString() + dst.toString());
                hdfs.copyFromLocalFile(src, dst);
            }
        }

        hdfs.close();
    }

    /**
     * Delete a file on HDFS
     * @param destination
     * @return
     * @throws IOException
     */
    public static boolean deleteHDFSFile(String destination) throws IOException {
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(conf);

        Path path = new Path(destination);
        boolean isDeleted = hdfs.delete(path, true);

        hdfs.close();
        return isDeleted;
    }

    /**
     * Make a new directory on HDFS
     * @param destination
     * @throws IOException
     */
    public static void createDir(String destination) throws IOException {
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(conf);

        Path path = new Path(destination);
        hdfs.mkdirs(path);

        hdfs.close();
    }

    public static void downloadHDFS2Local(String source, String destination) throws IOException {
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(conf);

        Path src = new Path(source);
        Path dst = new Path(destination);
        if (hdfs.exists(src)) {
            hdfs.copyToLocalFile(false, src, dst);
        } else {
            System.out.println("File does not exist!");
        }
    }

    public static void main(String args[]) throws IOException {
        if (args.length == 3 && args[0].equals("--upload")) {
            String strInPath = args[1];
            String strOutPath = args[2];
            uploadAllLocal2HDFS(strInPath, strOutPath);
        } else if (args.length == 3 && args[0].equals("--download")) {
            String strInPath = args[1];
            String strOutPath = args[2];
            downloadHDFS2Local(strInPath, strOutPath);
        } else {
            System.out.println("Usage: LocalHDFSOperations --upload <input dir> <output dir> or Usage: LocalHDFSOperations --download <input dir.hib> <output dir.hib>");
            System.exit(0);
        }
    }
}
