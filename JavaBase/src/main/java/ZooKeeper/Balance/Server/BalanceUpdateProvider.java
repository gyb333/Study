package ZooKeeper.Balance.Server;

public interface BalanceUpdateProvider {
	
	public boolean addBalance(Integer step);
	
	public boolean reduceBalance(Integer step);

}
