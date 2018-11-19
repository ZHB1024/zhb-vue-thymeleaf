package com.zhb.vue.thread;


public class MailThread extends Thread {

    Message message;
    private MessageLock messageLock;

    public MailThread(Message message,MessageLock lock) {
        this.message = message;
        this.messageLock = lock;
    }

    @Override
    public void run() {
        //message.mailSender();
        try {
            messageLock.mailSender();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
