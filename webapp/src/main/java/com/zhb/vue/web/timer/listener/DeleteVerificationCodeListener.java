package com.zhb.vue.web.timer.listener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.zhb.vue.web.timer.task.DeleteVerificationCodeTask;

public class DeleteVerificationCodeListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        int perTime = Integer.parseInt(arg0.getServletContext().getInitParameter("deleteVerificationCodePerTime"));
        int beforeTime = Integer.parseInt(arg0.getServletContext().getInitParameter("deleteVerificationCodeBeforeTime"));
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        DeleteVerificationCodeTask task = new DeleteVerificationCodeTask(beforeTime);
        scheduledExecutorService.scheduleAtFixedRate(task, 0, perTime, TimeUnit.MINUTES);
    }

}
