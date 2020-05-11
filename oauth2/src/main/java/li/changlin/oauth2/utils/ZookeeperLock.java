package li.changlin.oauth2.utils;

public interface ZookeeperLock {
    void lock();

    void unlock();
}
