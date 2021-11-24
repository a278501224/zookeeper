package com.cherry.zookeeper.config;

import com.sun.org.apache.xml.internal.security.Init;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @description:
 * @author: Cherry
 * @create: 2021-11-24 14:50
 **/
public class ZKUtils {
    private static ZooKeeper zk;

    private static String adress = "localhost:2181,localhost:2182,localhost:2183/testConf";

    private static DefaultWatch watch = new DefaultWatch();

    private static CountDownLatch cd = new CountDownLatch(1);

    public static ZooKeeper getZK(){
        try {
            zk = new ZooKeeper(adress, 1000, watch);
            watch.setCc(cd);
            try {
                cd.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } ;

        return zk;
    }
}
