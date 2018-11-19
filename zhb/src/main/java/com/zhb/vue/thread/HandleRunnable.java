package com.zhb.vue.thread;

import java.util.Date;

public class HandleRunnable implements Runnable {

	private String name;

	public HandleRunnable(String name) {
		this.name = "thread" + name;
	}

	@Override
	public void run() {
		System.out.println(name + " Start. Time = " + new Date());
		processCommand();
		System.out.println(name + " End. Time = " + new Date());
	}

	private void processCommand() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return this.name;
	}

}
