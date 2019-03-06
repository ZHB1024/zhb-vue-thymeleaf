package com.zhb.vue.web.controller.test.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2019年3月6日上午11:29:19
*/

public class ThreadPoolTest {
    
    public static void main(String[] args) {
        threadPoolExecutor();
    }
    
    public static void threadPoolExecutor() {
        int coreSize = 5;
        int maxSize = 6;
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(10);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();
        ThreadPoolExecutor tye = new ThreadPoolExecutor(coreSize, maxSize, 0, TimeUnit.MILLISECONDS, queue,threadFactory,handler);
        for(int i=0;i<11;i++) {
            tye.execute(new ThreadPoolRunnable(i+1));
        }
        tye.shutdown();
        Executors.newSingleThreadExecutor();
        Executors.newFixedThreadPool(2);
        Executors.newCachedThreadPool();
        Executors.newScheduledThreadPool(2);
    }

}


