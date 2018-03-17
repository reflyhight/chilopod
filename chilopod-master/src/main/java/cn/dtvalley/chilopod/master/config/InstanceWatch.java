package cn.dtvalley.chilopod.master.config;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class InstanceWatch implements ApplicationListener<ApplicationStartedEvent>, Runnable,DisposableBean {
    private Thread thread;
    private int time = 30;
    private boolean flag = true;

    InstanceWatch() {
        this.thread = new Thread(this);
        this.thread.setDaemon(true);
    }


    @Override
    public void run() {
        while (flag){
            try {
                Thread.sleep(30*1000);
                System.out.println("....");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        thread.start();
    }

    @Override
    public void destroy() throws Exception {
        this.flag = false;
    }
}
