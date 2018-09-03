package Study.Zookeeper.Balance.Server;

 
import org.I0Itec.zkclient.exception.ZkBadVersionException;
import org.apache.zookeeper.data.Stat;

public class DefaultBalanceUpdateProvider implements BalanceUpdateProvider,IZkClientProvider {

	private String serverPath;
 

	public DefaultBalanceUpdateProvider(String serverPath) {
		this.serverPath = serverPath;
 
	}

	@Override
	public boolean addBalance(Integer step) {
		// TODO Auto-generated method stub
		Stat stat = new Stat();
		ServerData sd;

		while (true) {

			try {
				sd = zkClient.readData(this.serverPath, stat);
				sd.setBalance(sd.getBalance() + step);
				zkClient.writeData(this.serverPath, sd, stat.getVersion());
				return true;
			} catch (ZkBadVersionException e) {
				// ignore
			} catch (Exception e) {
				return false;
			}
		}

	}

	@Override
	public boolean reduceBalance(Integer step) {
		// TODO Auto-generated method stub
		Stat stat = new Stat();
		ServerData sd;

		while (true) {

			try {
				sd = zkClient.readData(this.serverPath, stat);
				
				final Integer currBalance = sd.getBalance();
				
				sd.setBalance(currBalance>step?currBalance-step:0);
				
				zkClient.writeData(this.serverPath, sd, stat.getVersion());
				
				return true;
			} catch (ZkBadVersionException e) {
				// ignore
			} catch (Exception e) {
				return false;
			}
		}
	}

}
