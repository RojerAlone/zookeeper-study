package cn.rojeralone;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * Created by RojerAlone on 2017-11-13
 */
public class HelloZooKeeper {

    private static final String HOST = "localhost";
    private static final int PORT = 2181;
    private static final int TIMEOUT = 5000;
    // CountDownLatch 用来表示已经连上服务器
    private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(1);

    public static void main(String[] args) {
        try {
            ZooKeeper zk = new ZooKeeper(HOST + ":" + PORT, TIMEOUT, new Watcher() {
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                        System.out.println("触发 " + watchedEvent.getType() + " 事件");
                        COUNT_DOWN_LATCH.countDown(); // 连接完成后才能执行相关操作
                    }
                }
            });
            COUNT_DOWN_LATCH.await(); // 在这里等待连接上 zk 服务器，不然后面执行的操作会抛出 ConnectionLossException
            // 创建一个目录结点，create 操作会返回创建的结点完整名称
            System.out.println(zk.create("/testPath", "testData".getBytes(), Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT));
            // 创建一个子目录结点
            System.out.println(zk.create("/testPath/testChildPath", "testChildData".getBytes(),
                    Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT));
            System.out.println(new String(zk.getData("/testPath", false, null))); // 获取结点信息
            System.out.println(zk.getChildren("/testPath", true)); // 获取子目录信息
            zk.setData("/testPath/testChildPath", "modifyChildData".getBytes(), -1); // 修改子结点信息
            zk.delete("/testPath/testChildPath", -1);
            zk.delete("/testPath", -1);
            zk.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
