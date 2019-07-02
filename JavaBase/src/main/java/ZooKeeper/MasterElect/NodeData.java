package ZooKeeper.MasterElect;

import java.io.Serializable;

public class NodeData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8328958972841805280L;

	 
	private long cid;
	
	public long getCid() {
		return cid;
	}

	public void setCid(long cid) {
		this.cid = cid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;

}
