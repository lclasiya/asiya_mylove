package li.changlin.oauth2.utils;

import org.I0Itec.zkclient.ZkClient;

import java.util.concurrent.CountDownLatch;

public abstract class AbstractZookeeperLock implements ZookeeperLock{

    protected String lockPath;
    protected String zk_address = "192.168.30.164:2181";
    protected ZkClient zkClient = new ZkClient(zk_address);
    protected CountDownLatch countDownLatch;

    @Override
    public final void lock() {
        if (tryLock()){
            System.out.println("获取锁成功");
        }else {
            waitLock();
            lock();
        }
    }

    @Override
    public final void unlock() {
        if (zkClient != null){
            zkClient.close();
            System.out.println("解锁成功");
        }
    }
    protected abstract boolean tryLock();
    protected abstract void waitLock();
}
