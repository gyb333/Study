package Study.Zookeeper.Curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;

public class CuratorFactory {

	private static final String connectString = "Master:2181,Second:2181,slave:2181";
	private static final int sessionTimeoutMs = 5 * 1000;
	private static final int connectionTimeoutMs = 5 * 1000;

	private static CuratorFramework curatorFramework;

	static {
		curatorFramework = getInstance(true);
	}

	public static CuratorFramework getInstance(boolean isNew) {
		if (isNew) {
			// RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
			// RetryPolicy retryPolicy = new RetryNTimes(5, 1000);
			RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);			
			return  getInstance(retryPolicy);
		}
		return curatorFramework;

	}

	public static CuratorFramework getInstance(RetryPolicy retryPolicy) {

		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString(connectString)
				.sessionTimeoutMs(sessionTimeoutMs)
				.connectionTimeoutMs(connectionTimeoutMs)
				.authorization("digest", "root:root".getBytes())
				 
				.retryPolicy(retryPolicy)
				.build();

		client.start();
		return client;
	}

}
