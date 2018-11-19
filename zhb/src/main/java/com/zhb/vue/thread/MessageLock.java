package com.zhb.vue.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class MessageLock {
    
    private ReentrantLock lock = new ReentrantLock();
    
    private String name;

    public MessageLock(String name) {
        this.name = name;
    }

    public void smsSender() throws InterruptedException {
        if(lock.tryLock(500,TimeUnit.MILLISECONDS)) {
            try {
                System.out.println(Thread.currentThread().getName() + "-获取到锁了---smsSender-----------");
                System.out.println(Thread.currentThread().getName() + "--锁住2s--smsSender-----------");
                Thread.sleep(2000);
            }finally {
                lock.unlock();
            }
        }else {
            System.out.println(Thread.currentThread().getName() + "--等待0.5s没获取到锁--smsSender-----------");
        }
        
    }

    public void mailSender() throws InterruptedException {
        if(lock.tryLock(500,TimeUnit.MILLISECONDS)) {
            try {
                System.out.println(Thread.currentThread().getName() + "-获取到锁了---mailSender-----------");
                System.out.println(Thread.currentThread().getName() + "--锁住2s--smsSender-----------");
                Thread.sleep(2000);
            }finally {
                lock.unlock();
            }
        }else {
            System.out.println(Thread.currentThread().getName() + "--等待0.5s没获取到锁--mailSender-----------");
        }
        
    }

}
