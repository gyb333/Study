package RPC.Hadoop;

import java.io.IOException;

import org.apache.hadoop.ipc.ProtocolSignature;
import org.apache.hadoop.ipc.VersionedProtocol;

public class ClientProtocolImpl implements ClientProtocol{
	
	private int id;
	
	public int getId() {
		 
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;

	@Override
	public String toString() {
		return "DataModel [id=" + id + ", name=" + name + "]";
	}

	 
	
	public String Hello(String strMsg) {
		System.out.print("server.......");
		return strMsg+" Server";
	}

	public ProtocolSignature getProtocolSignature(String protocol, long clientVersion, int clientMethodsHash)
			throws IOException {
		// TODO Auto-generated method stub
		return new ProtocolSignature(versionID, null);
	}

	public long getProtocolVersion(String protocol, long clientVersion) throws IOException {
		// TODO Auto-generated method stub
		return versionID;
	}
	
	
	
	
}
