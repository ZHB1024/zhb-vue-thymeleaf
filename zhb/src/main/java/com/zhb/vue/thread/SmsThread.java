package com.zhb.vue.thread;

public class SmsThread extends Thread {
    
    private Message message;
    private MessageLock messageLock;
    
    
    public SmsThread(Message message,MessageLock lock) {
        this.message = message;
        this.messageLock = lock;
    }
    
    
    @Override
    public void run() {
        //message.smsSender();
        try {
			messageLock.smsSender();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
