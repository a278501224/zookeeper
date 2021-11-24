package com.cherry.zookeeper.config;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * @description:
 * @author: Cherry
 * @create: 2021-11-24 15:09
 **/
public class WatchCallBack implements Watcher, AsyncCallback.StatCallback, AsyncCallback.DataCallback {
   ZooKeeper zk;
    Myconf conf;
CountDownLatch cc = new CountDownLatch(1);


    public Myconf getConf() {
        return conf;
    }

    public void setConf(Myconf conf) {
        this.conf = conf;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    public ZooKeeper getZk() {
        return zk;
    }

    @Override
    public void processResult(int i, String s, Object o, byte[] data, Stat stat) {
            if(data !=null){
                String s1 = new String(data);
                conf.setConf(s1);
                cc.countDown();
            }
    }

    @Override
    public void processResult(int i, String s, Object o, Stat stat) {
        if(stat != null){
                zk.getData("/AppConf",this,this,"sdfs");
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()) {
            case None:
                break;
            case NodeCreated:
                zk.getData("/AppConf",this,this,"sdfs");
                break;
            case NodeDeleted:
                //容忍性
                conf.setConf("");
                cc = new CountDownLatch(1);
                break;
            case NodeDataChanged:
                zk.getData("/AppConf",this,this,"sdfs");
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

    public  void await(){
        zk.exists("/AppConf",this,this,"ABC");
        try {
            cc.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
