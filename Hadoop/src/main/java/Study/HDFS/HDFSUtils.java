package Study.HDFS;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;


import org.apache.hadoop.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.*;

public class HDFSUtils {

    private static final Logger logger = Logger.getLogger(ConfigUtil.class);

    private static Configuration conf = null;

    public static void setConf(Configuration conf) {
        HDFSUtils.conf = conf;
    }

    static {
        try {
            String path = HDFSUtils.class.getResource("/").getPath();
            System.out.println(path);
            path = System.getProperty("user.dir") + "\\Resources\\";
            System.out.println(path);

            conf = new Configuration();
            conf.set("fs.defaultFS", HdfsConfig.FS_DEFAULTFS);
            conf.set("dfs.nameservices", HdfsConfig.DFS_NAMESERVICE);
            conf.set("dfs.ha.namenodes." + HdfsConfig.DFS_NAMESERVICE, HdfsConfig.DFS_HA_NAMENODES_NS);
            conf.set("dfs.namenode.rpc-address." + HdfsConfig.DFS_NAMESERVICE + ".nn1",
                    HdfsConfig.DFS_NAMENODE_RPC_ADDRESS_NS_NN1);
            conf.set("dfs.namenode.rpc-address." + HdfsConfig.DFS_NAMESERVICE + ".nn2",
                    HdfsConfig.DFS_NAMENODE_RPC_ADDRESS_NS_NN2);
            conf.set("dfs.client.failover.proxy.provider." + HdfsConfig.DFS_NAMESERVICE,
                    HdfsConfig.DFS_CLIENT_FAILOVER_PROXY_PROVIDER_NS);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public static synchronized Configuration getConfiguration() {
        Configuration conf = new Configuration();
        conf.addResource("core-site.xml");
        conf.addResource("hdfs-site.xml");
        conf.addResource("yarn-site.xml");
        return conf;
    }

    public static void CreateFolder(String strPath) throws IOException {
        FileSystem fs = null;

        try {
            fs = FileSystem.get(getConfiguration());
            Path path = new Path(strPath);

            if (!fs.exists(path)) {
                fs.mkdirs(path);
            }

        } finally {
            if (fs != null) {
                fs.close();
            }
        }
    }

    public static void UploadFile(String strPath, String strLocalPath) throws IOException {
        FileSystem fs = null;
        try {
            fs = FileSystem.get(conf);
            UploadFile(fs, strPath, strLocalPath);
        } finally {
            if (fs != null) {
                fs.close();
            }

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

    public static void DownloadFile(String strPath, OutputStream out) throws IOException {
        FileSystem fs = null;
        FSDataInputStream is = null;
        try {
            fs = FileSystem.get(conf);
            is = fs.open(new Path(strPath));
            IOUtils.copyBytes(is, out, 1024, true);
            IOUtils.closeStream(is);
        } finally {
            IOUtils.closeStream(is);
            if (fs != null) {
                fs.close();
            }

        }
    }

    public static void DownloadFile(String strPath, String strLocalPath) throws IOException {
        FileSystem fs = null;

        try {
            fs = FileSystem.get(conf);
            DownloadFile(fs,strPath,strLocalPath);
        } finally {
            if (fs != null) {
                fs.close();
            }

        }
    }

    public static void DownloadFile(FileSystem fs,String strPath, String strLocalPath) throws IOException {
        FSDataInputStream is = null;
        File file = new File(strLocalPath);
        File fileParent = file.getParentFile();
        if(!fileParent.exists()){
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


    public static void DeleteFile(String strPath) throws IOException {
        FileSystem fs = null;
        try {
            Path path = new Path(strPath);
            fs = FileSystem.get(conf);
            if (fs.deleteOnExit(path)) {
                fs.delete(path, true);
            }
        } finally {
            if (fs != null) {
                fs.close();
            }

        }
    }

    public static FileStatus[] ls(String strFolder) throws IOException {
        FileSystem fs = null;
        try {
            fs = FileSystem.get(conf);
            Path path = new Path(strFolder);
            FileStatus[] fileList = fs.listStatus(path);
            return fileList;
        } finally {
            if (fs != null)
                fs.close();
        }
    }


    public static void Copy(String strInPath, String strOutPath) throws IOException {
        FileSystem fs = null;
        FSDataInputStream hdfsIn = null;
        FSDataOutputStream hdfsOut = null;
        try {
            fs = FileSystem.get(conf);
            hdfsIn = fs.open(new Path(strInPath));
            hdfsOut = fs.create(new Path(strOutPath));
            IOUtils.copyBytes(hdfsIn, hdfsOut, 1024 * 1024 * 64, false);
        } finally {
            IOUtils.closeStream(hdfsIn);
            IOUtils.closeStream(hdfsOut);
            if (fs != null)
                fs.close();
        }
    }


    public static void GetLocation(String strPath) throws IOException {

        FileSystem fs = null;
        try {
            fs = FileSystem.get(conf);
            Path path = new Path(strPath);
            FileStatus fsFileStatus = fs.getFileStatus(path);


            BlockLocation[] blockLocations = fs.getFileBlockLocations(fsFileStatus, 0, fsFileStatus.getLen());

            for (BlockLocation blockLocation : blockLocations) {
                String[] hostStrings = blockLocation.getHosts();// 鑾峰彇鎵�鍦ㄧ殑涓绘満
                for (String host : hostStrings)
                    System.out.println(host);
            }

        } finally {
            if (fs != null)
                fs.close();
        }

    }


    public static DatanodeInfo[] GetCluster() throws IOException {

        FileSystem fs = null;
        try {
            fs = FileSystem.get(conf);

            DistributedFileSystem dfs = (DistributedFileSystem) fs;

            DatanodeInfo[] datanodeInfos = dfs.getDataNodeStats();
            return datanodeInfos;
        } finally {
            if (fs != null)
                fs.close();
        }
    }


    public static void uploadFile2HDFS(FileSystem fs,  String strInput, String strLocalInput, boolean isFile) throws IOException {
        Path pathInput = new Path(strInput);
        if (fs.exists(pathInput)) {
            fs.delete(pathInput, true);
        }
        if (isFile) {
            HDFSUtils.UploadFile(fs, strInput, strLocalInput);
        } else {
            File[] files = FileUtils.GetFiles(strLocalInput);
            for (File file : files) {
                if (!file.isDirectory())
                    HDFSUtils.UploadFile(fs, strInput + "/" + file.getName(), file.getPath());

            }
        }


    }

}
