package Study.Zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import junit.framework.TestCase;

public class DistributedSimpleQueueTest extends TestCase {
	
	
	
	public void testApp()
    {
		ZkClient zkClient = new ZkClient("Master:2181,Second:2181,slave:2181", 5000, 5000, new SerializableSerializer());
		DistributedSimpleQueue<User> queue = new DistributedSimpleQueue<User>(zkClient,"/Queue");
		
		User user1 = new User();
		user1.setId("1");
		user1.setName("xiao wang");
		
		User user2 = new User();
		user2.setId("2");
		user2.setName("xiao wang");		
		
		try {
			queue.offer(user1);
			queue.offer(user2);
			User u1 = (User) queue.poll();
			User u2 = (User) queue.poll();
			
			if (user1.getId().equals(u1.getId()) && user2.getId().equals(u2.getId())){
				System.out.println("Success!");
				 assertTrue( true );
				
			}else {
				assertTrue( false );
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		
       
    }
	

}
