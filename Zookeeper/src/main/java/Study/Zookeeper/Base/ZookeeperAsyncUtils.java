package Study.Zookeeper.Base;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.ZooDefs.Ids;

public class ZookeeperAsyncUtils {
    private static boolean IsExisted = false;
    private static ZooKeeper zookeeper;

    static {

        try {
            zookeeper = ZookeeperFactory.getInstance();
            zookeeper.addAuthInfo("digest", "root:root".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void createZNodeAsync(String path, String data, CreateMode mode, StringCallback cb, Object ctx) {
        try {
            if (zookeeper.exists(path, true) == null) {
                zookeeper.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, mode, new StringCallback() {

                    public void processResult(int rc, String path, Object ctx, String name) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("rc=" + rc).append("\n");
                        sb.append("path" + path).append("\n");
                        sb.append("ctx=" + ctx).append("\n");
                        sb.append("name=" + name).append("\n");
                        System.out.println(sb.toString());

                    }
                }, ctx);
                IsExisted = true;
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
