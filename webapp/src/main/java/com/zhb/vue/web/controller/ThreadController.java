package com.zhb.vue.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhb.forever.framework.util.MessageUtil;
import com.zhb.forever.framework.util.StringUtil;
import com.zhb.vue.thread.HandleRunnable;
import com.zhb.vue.thread.MailThread;
import com.zhb.vue.thread.Message;
import com.zhb.vue.thread.MessageLock;
import com.zhb.vue.thread.SmsThread;
import com.zhb.vue.thread.SubmitCallable;

public class ThreadController {
    
    private static Logger logger = LoggerFactory.getLogger(ThreadController.class);
    
    
    @RequestMapping("/test")
    public String test(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String message = MessageUtil.getMessage("0001", new Object[] { "张会彬" });
        logger.info(message);
        return "test.body.index";
    }
    
 
//------------------------------------------------------------------------------------------------------------------

    
  //----------------extends Thread-----------------------------------------------
    public static void extendsThread(){
        Message message = new Message("message");
        MessageLock messageLock = new MessageLock("messageLock");
        
        SmsThread smsThread = new SmsThread(message,messageLock);
        MailThread mailThread = new MailThread(message,messageLock);
        
        smsThread.start();
        mailThread.start();
    }
    
  //----------------implements Runnable----------------------------------------------- 
    public static void implementsRunnable(){
        HandleRunnable handleRunnable = new HandleRunnable("runnable111");
        Thread thread1 = new Thread(handleRunnable);
        Thread thread2 = new Thread(handleRunnable);
        thread1.start();
        thread2.start();
    }
    
    //-------------------------callable futureTask--------------------------
    public static void futureTask(){
        Callable<Integer> call = new Callable<Integer>() {
             public Integer call() throws Exception {
                 return new Random().nextInt(100);
             }
        };
        
        FutureTask<Integer> ft = new FutureTask<Integer>(call);
        new Thread(ft).start();
        try{
            Thread.currentThread().sleep(500);
            logger.info(ft.get() + "" );
        }catch(InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
    }
    
    //------------------------------------ThreadPoolExecutor------------------------------
    public static void threadPoolExecutor(){
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(5, 5, 0, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
        
        List<Future<Object>> results = new ArrayList<>();
        
        for (int i = 0; i < 10; i++) {  
            Callable<Object> c = new SubmitCallable(i + " ");  
            // 执行任务并获取Future对象  
            Future<Object> f = tpe.submit(c); 
            results.add(f);  
        }  
        
        // 关闭线程池  
        tpe.shutdown();  
        
        // 获取所有并发任务的运行结果  
        for (Future<Object> f : results) {  
            // 从Future对象上获取任务的返回值，并输出到控制台  
            try {
                logger.info(">>>" + f.get().toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }  
        }  
    }
    
  //------------------------------------ThreadPoolExecutor----自定义 线程池，记录线程信息 日志--------------------------
    public static void threadPoolExecutorByMyself(){
        int corePoolSize = 5;
        int maxPoolSize = 5;
        long keepAliveTime = 0;
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();
        
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, queue,threadFactory,handler){
            @Override
            protected void beforeExecute(Thread t, Runnable r) { 
                logger.info(t.getName() + "---------before----------------");
            }
            
            @Override
            protected void afterExecute(Runnable r, Throwable t) { 
                logger.info(((HandleRunnable)r).toString() + "-------------------after----------------");
            }
            
            @Override
            protected void terminated() { 
                logger.info("-------------------terminated----------------");
            }
        };
        
        for (int i = 0; i < 10; i++) {  
            HandleRunnable hr = new HandleRunnable(i+ " ");
            tpe.execute(hr);
        }  
        
        // 关闭线程池  
        tpe.shutdown();  
    }
    
    
  //---------------------------------------Executors Future--------------------------------  
    public static void executorService() throws InterruptedException, ExecutionException{
        int taskSize = 5;
        List<Future<Object>> results = new ArrayList<>();
        
        ExecutorService executorService = Executors.newFixedThreadPool(taskSize);
        for (int i = 0; i < taskSize; i++) {  
            Callable<Object> c = new SubmitCallable(i + " ");  
            // 执行任务并获取Future对象  
            Future<Object> f = executorService.submit(c); 
            results.add(f);  
        }  
        
        // 关闭线程池  
        executorService.shutdown();  
  
        // 获取所有并发任务的运行结果  
        for (Future<Object> f : results) {  
            // 从Future对象上获取任务的返回值，并输出到控制台  
            logger.info(">>>" + f.get().toString());  
        }  
    }
    
    
//---------------------------------------------------------------锁------------------------------------    
    
  //----------------------------synchronized-----同步------阻塞-------内置锁-----------
    public static void synchronizedTest() throws InterruptedException{
        Message message = new Message("test");
        Thread thread01 = new Thread(new Runnable() {
            @Override
            public void run() {
                message.smsSender();
            }
        });
        
        Thread thread02 = new Thread(new Runnable() {
            @Override
            public void run() {
                message.mailSender();
            }
        });
        
        Thread thread03 = new Thread(new Runnable() {
            @Override
            public void run() {
                message.send();
            }
        });
        
        thread01.start();
        thread02.start();
        Thread.currentThread().sleep(500);
        thread03.start();
    }
    
    
  //------------------------------------lock--------显示锁---------非阻塞-------读写锁--------------------
    public static void lockTest(){
        MessageLock messageLock = new MessageLock("lockTest");
        Thread thread01 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    messageLock.smsSender();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        
        Thread thread02 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    messageLock.mailSender();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        
        thread01.start();
        thread02.start();
        /*try {
            Thread.currentThread().sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        
    }
    
  //-------------------------------countDownLatch-------不可重用---------例如：发射火箭，前期准备完成后，才能发射---------- 
    public static void countDownLatch(){
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Lock lock = new ReentrantLock();
        AtomicInteger num = new AtomicInteger(0);
        //三个线程同时调用一个接口，谁先获得值，就返回，其他线程不在执行。
        for(int i = 0 ; i < 3 ; i++){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        logger.info( Thread.currentThread().getName() + "进入了run");  
                        countDownLatch.await();//等待countDownLatch为0
                        logger.info( Thread.currentThread().getName() + "开始执行,time: " + System.currentTimeMillis());
                        if(lock.tryLock()){
                            try{
                                if (num.get() == 0) {
                                    logger.info( Thread.currentThread().getName() + "进入" );
                                    int temp =num.incrementAndGet();
                                    if (temp == 1) {
                                        logger.info( Thread.currentThread().getName() + ":" + temp);  
                                    }
                                }
                            }finally {
                                lock.unlock();
                            }
                        }
                        
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        countDownLatch.countDown();//将countDownLatch减为0，上面3个线程同时开始执行
        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("num:" + num);
        
    }
    
    //------------------------------------CyclicBarrier -----------循环栅栏--------可以重复使用------------
    public static void cyclicBarrier(){
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
        for(int i=0; i<3; i++){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        logger.info(Thread.currentThread().getName() + "进入,time:" + System.currentTimeMillis() + " 等待 .......");
                        cyclicBarrier.await();//等待线程个数等于 CyclicBarrier初始化的3
                        logger.info(Thread.currentThread().getName() + ",time:" + System.currentTimeMillis());
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    //--------------------------------semaphore------------信号量-------可重用--------------------
    public static void semaphore(){
        Semaphore semaphore = new Semaphore(2);
        for(int i=0; i<3; i++){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean flag = false;
                    try {
                        semaphore.acquire();//获取信号量，没有则等待
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        logger.info(Thread.currentThread().getName() + "进入,time:" + System.currentTimeMillis() );
                        flag = true;
                    } finally {
                        logger.info(Thread.currentThread().getName() + "离开,time:" + System.currentTimeMillis());
                        if (flag) {
                            semaphore.release();//释放信号量
                        }
                    }
                    
                }
            });
            thread.start();
        }
        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    //------------------------blockingQueue-------------------
    public static void blockingQueue(){
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(8);
        Thread thread01 = new Thread(new Runnable() {
            @Override
            public void run() {
                logger.info("向队列里添加------");
                for(int i=0; i< 10;i++){
                    while (!queue.offer(i+"num")) {
                        logger.info("队列已满------等待消费-------");
                        try {
                            Thread.currentThread().sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    logger.info(i+"num," + "添加成功------");
                }
            }
        });
        Thread thread02 = new Thread(new Runnable() {
            @Override
            public void run() {
                logger.info("从队列里取------");
                for(int i=0; i< 10;i++){
                    String value = null;
                    while(StringUtil.isBlank(value=queue.poll())){
                        logger.info("队列已空------等待添加-------------");
                        try {
                            Thread.currentThread().sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    logger.info("取出的值为： " + value);
                }
            }
        });
        
        thread02.start();
        thread01.start();
        
    }

    //------------------------------------------------几个线程同时从某个接口中获取资源，谁先得到就返回谁--------------------------------------
    public static void cyclicBarry(){
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
        AtomicReference<String> value = new AtomicReference<String>();
        String threadName = null;
        List<Future<String>> results = new ArrayList<>();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    cyclicBarrier.await();
                    if (StringUtil.isNotBlank(value.get())){
                        return;
                    }
                    String tempValue = getSource();
                    if (!value.compareAndSet(null,tempValue)){
                        return ;
                    }
                    //threadName = Thread.currentThread().getName();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }

            }
        };

        Callable<String> call = new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    cyclicBarrier.await();
                    if (StringUtil.isNotBlank(value.get())){
                        return null;
                    }
                    String tempValue = getSource();
                    if (!value.compareAndSet(null,tempValue)){
                        return null;
                    }
                    return Thread.currentThread().getName();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                return Thread.currentThread().getName();
            }
        };

        ExecutorService es = Executors.newFixedThreadPool(3);
        for (int i =0 ;i < 3;i++){
            Future<String> future = es.submit(call);
            results.add(future);
        }
        es.shutdown();

        try {
            Thread.currentThread().sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.info(value.get());

        for (Future<String> future:results) {
            try {
                logger.info(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }


    }

    private static String getSource(){
        return "hello world";
    }
    
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        
        //extendsThread();
        //implementsRunnable();
        //threadPoolExecutor();
        //threadPoolExecutorByMyself();
        //futureTask();
        //synchronizedTest();
        //executorService();
        //countDownLatch();
        //cyclicBarrier();
        //cyclicBarrier();
        //semaphore();
        //blockingQueue();
        //lockTest();
        cyclicBarry();
        
    }

}
