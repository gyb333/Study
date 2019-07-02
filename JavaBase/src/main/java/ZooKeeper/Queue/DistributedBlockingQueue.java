package ZooKeeper.Queue;

 




import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.IZkChildListener;



public class DistributedBlockingQueue<T> extends DistributedSimpleQueue<T>{      
    
	
    public DistributedBlockingQueue(String root) {
    	super(root);

	}
    

    @Override
	public T poll() throws Exception {

		while (true){
			
			final CountDownLatch    latch = new CountDownLatch(1);
			final IZkChildListener childListener = new IZkChildListener() {
				public void handleChildChange(String parentPath, List<String> currentChilds)
						throws Exception {
					latch.countDown();
					
				}
			};
			zkClient.subscribeChildChanges(root, childListener);
			try{
				T node = super.poll();
	            if ( node != null ){
	                return node;
	            }else{
	            	latch.await();
	            }
			}finally{
				zkClient.unsubscribeChildChanges(root, childListener);
				
			}
			
		}
	}

	
	

}
