package Study.Zookeeper.MasterElect;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import org.I0Itec.zkclient.exception.ZkInterruptedException;

import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.CreateMode;

import Study.Zookeeper.ZkClient.ZkClientFactory;


public class WorkServer {


    private static final String MASTER_PATH = "/Master";

    private volatile boolean running = false;

    private NodeData nodeData;

    private NodeData masterData;

    private IZkDataListener dataListener;

    private ZkClient zkClient;


    private ScheduledExecutorService delayExecutor = Executors.newScheduledThreadPool(1);


    public WorkServer(final NodeData nodeData) {
        zkClient = ZkClientFactory.getInstance();
        this.nodeData = nodeData;
        this.dataListener = new IZkDataListener() {


            public void handleDataDeleted(String dataPath) throws Exception {
                if (masterData != null && masterData.getName().equals(nodeData.getName())) {
                    takeMaster();
                } else {
                    delayExecutor.schedule(new Runnable() {
                        public void run() {
                            takeMaster();
                        }
                    }, 5, TimeUnit.SECONDS);

                }

            }


            public void handleDataChange(String dataPath, Object data) throws Exception {
                // TODO Auto-generated method stub

            }
        };
    }


    protected void finalize() throws Throwable {
        // TODO Auto-generated method stub
        if (zkClient != null) {
            zkClient.close();
            zkClient = null;
        }
        super.finalize();
    }


    public void start() throws Exception {
        if (running) {
            throw new Exception("server has startup.....");
        }
        running = true;
        zkClient.subscribeDataChanges(MASTER_PATH, dataListener);
        takeMaster();
    }

    public void stop() throws Exception {
        if (!running) {
            throw new Exception("server has stoped");
        }
        running = false;
        delayExecutor.shutdown();
        zkClient.unsubscribeDataChanges(MASTER_PATH, dataListener);
        releaseMaster();
    }

    private void takeMaster() {
        if (!running)
            return;
        try {
            String strMsg = zkClient.create(MASTER_PATH, nodeData, CreateMode.PERSISTENT);
            System.out.println(strMsg);
            masterData = nodeData;
            System.out.println(nodeData.getName() + " is Master");
            delayExecutor.schedule(new Runnable() {

                public void run() {
                    if (checkMaster()) {
                        releaseMaster();
                    }

                }
            }, 5, TimeUnit.SECONDS);
        } catch (ZkNodeExistsException e) {
            masterData = zkClient.readData(MASTER_PATH, true);
            if (masterData == null) {
                takeMaster();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block

        }
    }

    private boolean checkMaster() {

        try {
            masterData = zkClient.readData(MASTER_PATH);
            if (masterData.getName().equals(nodeData.getName())) {
                return true;
            }
            return false;
        } catch (ZkInterruptedException e) {
            return checkMaster();
        } catch (Exception e) {
            return false;
        }

    }

    private void releaseMaster() {
        if (checkMaster())
            zkClient.delete(MASTER_PATH);
    }


}
