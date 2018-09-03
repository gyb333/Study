package Study.Zookeeper.Balance.Server;

 

public class ZooKeeperRegistContext {
	
	private String path;
 
	private Object data;
	
	
	
	public ZooKeeperRegistContext(String path,   Object data) {
		super();
		this.path = path;
  
		this.data = data;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
 
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
	

}
