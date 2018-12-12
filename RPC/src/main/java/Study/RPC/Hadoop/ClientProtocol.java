package Study.RPC.Hadoop;

import org.apache.hadoop.ipc.VersionedProtocol;

public interface ClientProtocol extends  VersionedProtocol {
	public static final long versionID=123456789L;
	public abstract String Hello(String strMsg) ;
}
