package com.zhb.vue.thread.spider.mtsqom.disruptor;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年12月18日下午6:27:30
*/

public class ProducerRunnable implements Runnable {
    
    private final ProducerDisruptor producer;

    public ProducerRunnable(ProducerDisruptor producer) {
        this.producer = producer;
    }

    @Override
    public void run() {
        producer.produce();
    }

}


