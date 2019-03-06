package com.zhb.vue.web.controller.test.threadpool;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2019年3月6日上午11:32:33
*/

public class ThreadPoolRunnable implements Runnable {
    
    private int num;

    public ThreadPoolRunnable(int num) {
        this.num = num;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(100);
            System.out.println(Thread.currentThread().getName() + "***" + num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}


