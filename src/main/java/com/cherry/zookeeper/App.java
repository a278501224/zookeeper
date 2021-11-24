package com.cherry.zookeeper;


import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!");
        //zk是有session概念的，没有线程池的概念
        //watch观察，回调
        //第一类:new zk的时候，传入的watch，这个watch是session级别的，跟path，node没有关系

        //线程安全的
        CountDownLatch cd = new CountDownLatch(1);
        ZooKeeper zk = new ZooKeeper("localhost:2181,localhost:2182,localhost:2183", 3000, new Watcher() {
            //watch的回调方法
            @Override
            public void process(WatchedEvent watchedEvent) {
                Event.KeeperState state = watchedEvent.getState();
                Event.EventType type = watchedEvent.getType();
                String path = watchedEvent.getPath();
                System.out.println("new zk watch:" + watchedEvent.toString());

                switch (state) {
                    case Unknown:
                        break;
                    case Disconnected:
                        break;
                    case NoSyncConnected:
                        break;
                    case SyncConnected:
                        System.out.println("connected");
                        cd.countDown();
                        break;
                    case AuthFailed:
                        break;
                    case ConnectedReadOnly:
                        break;
                    case SaslAuthenticated:
                        break;
                    case Expired:
                        break;
                    case Closed:
                        break;
                }

                switch (type) {
                    case None:
                        break;
                    case NodeCreated:
                        break;
                    case NodeDeleted:
                        break;
                    case NodeDataChanged:
                        break;
                    case NodeChildrenChanged:
                        break;
                    case DataWatchRemoved:
                        break;
                    case ChildWatchRemoved:
                        break;
                    case PersistentWatchRemoved:
                        break;
                }
            }
        });

        cd.await();
        ZooKeeper.States state = zk.getState();
        switch (state) {
            case CONNECTING:
                System.out.println("ing........");
                break;
            case ASSOCIATING:
                break;
            case CONNECTED:
                System.out.println("ed.........");
                break;
            case CONNECTEDREADONLY:
                break;
            case CLOSED:
                break;
            case AUTH_FAILED:
                break;
            case NOT_CONNECTED:
                break;
        }

        String pathName = zk.create("/ooxx", "oldata".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        Stat stat = new Stat();
        byte[] node = zk.getData("/ooxx", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("getData watch:" + watchedEvent.toString());
                try {
                    //为true时 default watch 被重新注册 new zk的那个watch
                    zk.getData("/ooxx", this, stat);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, stat);
        System.out.println(new String(node));
        //触发回调
        Stat stat1 = zk.setData("/ooxx", "newdata".getBytes(), 0);
        //还会触发吗
        Stat stat2 = zk.setData("/ooxx", "newdata01".getBytes(), stat1.getVersion());


        System.out.println("-----async start------");
        zk.getData("/ooxx", false, new AsyncCallback.DataCallback() {

            @Override
            public void processResult(int i, String s, Object o, byte[] data, Stat stat) {
                System.out.println("-----async call back-----");
                System.out.println(o.toString());
                System.out.println(new String(data));
            }
        }, "abc");

        System.out.println("-------async over-------");

        Thread.sleep(22222222);
    }


}
