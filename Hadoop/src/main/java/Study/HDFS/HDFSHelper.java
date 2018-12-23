package Study.HDFS;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.*;

public class HDFSHelper {

    private static final Logger logger = Logger.getLogger(ConfigUtil.class);


    public static void CreateFolder(FileSystem fs, String strPath) throws IOException {


        Path path = new Path(strPath);

        if (!fs.exists(path)) {
            fs.mkdirs(path);
        }


    }


    public static void UploadFile(FileSystem fs, String strPath, String strLocalPath) throws IOException {
        FSDataOutputStream out = null;
        FileInputStream in = null;
        try {
            out = fs.create(new Path(strPath));
            in = new FileInputStream(strLocalPath);
            IOUtils.copyBytes(in, out, 1024, true);
        } finally {
            IOUtils.closeStream(out);
            IOUtils.closeStream(in);
        }
    }

    public static void DownloadFile(FileSystem fs, String strPath, OutputStream out) throws IOException {

        FSDataInputStream is = null;
        try {
            is = fs.open(new Path(strPath));
            IOUtils.copyBytes(is, out, 1024, true);
            IOUtils.closeStream(is);
        } finally {
            IOUtils.closeStream(is);


        }
    }


    public static void DownloadFile(FileSystem fs, String strPath, String strLocalPath) throws IOException {
        FSDataInputStream is = null;
        File file = new File(strLocalPath);
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(strLocalPath);
        try {
            is = fs.open(new Path(strPath));
            IOUtils.copyBytes(is, fos, 1024, true);
            IOUtils.closeStream(is);
        } finally {
            IOUtils.closeStream(is);
        }
    }


    public static void DeleteFile(FileSystem fs, String strPath) throws IOException {


        Path path = new Path(strPath);

        if (fs.deleteOnExit(path)) {
            fs.delete(path, true);
        }


    }

    public static FileStatus[] ls(FileSystem fs, String strFolder) throws IOException {


        Path path = new Path(strFolder);
        FileStatus[] fileList = fs.listStatus(path);
        return fileList;

    }


    public static void Copy(FileSystem fs, String strInPath, String strOutPath) throws IOException {

        FSDataInputStream hdfsIn = null;
        FSDataOutputStream hdfsOut = null;
        try {

            hdfsIn = fs.open(new Path(strInPath));
            hdfsOut = fs.create(new Path(strOutPath));
            IOUtils.copyBytes(hdfsIn, hdfsOut, 1024 * 1024 * 64, false);
        } finally {
            IOUtils.closeStream(hdfsIn);
            IOUtils.closeStream(hdfsOut);

        }
    }


    public static void GetLocation(FileSystem fs, String strPath) throws IOException {

        Path path = new Path(strPath);
        FileStatus fsFileStatus = fs.getFileStatus(path);


        BlockLocation[] blockLocations = fs.getFileBlockLocations(fsFileStatus, 0, fsFileStatus.getLen());

        for (BlockLocation blockLocation : blockLocations) {
            String[] hostStrings = blockLocation.getHosts();
            for (String host : hostStrings)
                System.out.println(host);
        }


    }


    public static DatanodeInfo[] GetCluster(FileSystem fs) throws IOException {


        DistributedFileSystem dfs = (DistributedFileSystem) fs;

        DatanodeInfo[] datanodeInfos = dfs.getDataNodeStats();
        return datanodeInfos;

    }


    public static void uploadFile2HDFS(FileSystem fs, String strInput, String strLocalInput, boolean isFile) throws IOException {
        Path pathInput = new Path(strInput);
        if (fs.exists(pathInput)) {
            fs.delete(pathInput, true);
        }
        if (isFile) {
            HDFSHelper.UploadFile(fs, strInput, strLocalInput);
        } else {
            File[] files = FileUtils.GetFiles(strLocalInput);
            for (File file : files) {
                if (!file.isDirectory())
                    HDFSHelper.UploadFile(fs, strInput + "/" + file.getName(), file.getPath());

            }
        }


    }

}
