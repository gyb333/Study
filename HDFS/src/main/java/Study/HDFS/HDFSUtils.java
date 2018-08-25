package Study.HDFS;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
 

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.io.IOUtils;
import org.apache.log4j.Logger;

import com.jcraft.jsch.ChannelSftp.LsEntry;
 
public class HDFSUtils {

	private static final Logger logger = Logger.getLogger(ConfigUtil.class);

	private static Configuration conf = null;

	static {
		try {

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

	public static synchronized Configuration getConfiguration() throws MalformedURLException {
		Configuration conf = new Configuration();
		String path=HDFSUtils.class.getResource("/").getPath();  
	
		path =System.getProperty("user.dir")+"\\Resources\\";
		 
		
		conf.addResource(path+"core-site.xml");
		conf.addResource(path+"hdfs-site.xml");
		conf.addResource(path+"yarn-site.xml");
		return conf;
	}

	public static void CreateFolder(String strPath) throws IOException {
		FileSystem fs = null;
		try {
			fs = FileSystem.get(conf);
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

	public static void UplodeFile(String strPath, String strLocalPath) throws IOException, FileNotFoundException {
		FileSystem fs = null;
		FSDataOutputStream out = null;
		FileInputStream in = null;
		try {
			fs = FileSystem.get(conf);
			out = fs.create(new Path(strPath));
			in = new FileInputStream(strLocalPath);
			IOUtils.copyBytes(in, out, 1024, true);
		} finally {
			IOUtils.closeStream(out);
			IOUtils.closeStream(in);

			if (fs != null) {
				fs.close();
			}

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

	/**
	 * HDFS之间文件的复制
	 * 
	 * @param strPath
	 * @param strFolder
	 * @throws IOException
	 */
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

	// 查看某个文件在HDFS集群中的位置
	/**
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	public void GetLocation(String strPath) throws IOException {

		FileSystem fs = null;
		try {
			fs = FileSystem.get(conf);
			Path path = new Path(strPath);
			FileStatus fsFileStatus = fs.getFileStatus(path);

			// 获得所有的块所在的位置信息包括：主机名，块名称、块大小etc
			BlockLocation[] blockLocations = fs.getFileBlockLocations(fsFileStatus, 0, fsFileStatus.getLen());

			for (BlockLocation blockLocation : blockLocations) {
				String[] hostStrings = blockLocation.getHosts();// 获取所在的主机
				for (String host : hostStrings)
					System.out.println(host);
			}

		} finally {
			if (fs != null)
				fs.close();
		}

	}

	/**
	 * 获取集群中所有的节点的名称信息
	 * 
	 * @return
	 * @throws IOException
	 */
	public DatanodeInfo[] GetCluster() throws IOException {

		FileSystem fs = null;
		try {
			fs = FileSystem.get(conf);
			// 将文件系统强制转换为分布式文件系统
			DistributedFileSystem dfs = (DistributedFileSystem) fs;
			// 获取文件系统中数据状态信息
			DatanodeInfo[] datanodeInfos = dfs.getDataNodeStats();
			return datanodeInfos;
		} finally {
			if (fs != null)
				fs.close();
		}
	}

}
