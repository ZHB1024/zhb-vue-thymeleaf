package com.zhb.vue.thread;

public class Message {
    
    String name ;
    
    public Message(){}
    
    public Message(String name) {
        this.name = name;
    }
    
    public void send(){
        System.out.println(name + "----send------非同步代码块不受限制----");
    }
    
    public synchronized void smsSender() {
        System.out.println(name + "----smsSender----------");
        try {
            Thread.sleep(2000);
            System.out.println(name + "----smsSender-----休眠2秒结束------");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public synchronized void mailSender() {
        System.out.println(name + "----mailSender-----------");
        try {
            Thread.sleep(2000);
            System.out.println(name + "----mailSender-----休眠2秒结束------");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
