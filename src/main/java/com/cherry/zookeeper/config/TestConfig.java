package com.cherry.zookeeper.config;

import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @description:
 * @author: Cherry
 * @create: 2021-11-24 14:49
 **/
public class TestConfig {
    ZooKeeper zk;

    @Before
    public void conn(){
        zk = ZKUtils.getZK();

    }

    @After
    public void close(){
        try {
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getConf(){
        WatchCallBack watchCallBack = new WatchCallBack();
        watchCallBack.setZk(zk);
        Myconf myconf = new Myconf();
        watchCallBack.setConf(myconf);
        watchCallBack.await();

        //节点不存在
        //节点存在

        while (true){
            if(myconf.getConf().equals("")){
                System.out.println("conf diu le。。。。。。");
                watchCallBack.await();
            }else{
                System.out.println(myconf.getConf());
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }



}
