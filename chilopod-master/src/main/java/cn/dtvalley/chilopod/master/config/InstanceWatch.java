package cn.dtvalley.chilopod.master.config;

import cn.dtvalley.chilopod.core.instance.InstanceInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InstanceWatch implements ApplicationListener<ApplicationStartedEvent>, Runnable, DisposableBean {
    private Thread thread;
    private int time = 30;
    private boolean flag = true;
    private InstanceInfo instanceInfo = InstanceInfo.instanceInfo;

    InstanceWatch() {
        this.thread = new Thread(this);
        this.thread.setDaemon(true);
    }


    @Override
    public void run() {
        while (flag) {
            try {
                Thread.sleep(30 * 1000);
                LocalDateTime now = LocalDateTime.now();
                List<String> ips = instanceInfo.getInstance().values().stream().filter(instance -> {
                    LocalDateTime refreshTime = instance.getLastRefreshTime();
                    return refreshTime.plusSeconds(90).isBefore(now);
                }).map(InstanceInfo.Instance::getIp).collect(Collectors.toList());
                ips.forEach(it -> instanceInfo.getInstance().remove(it));
            } catch (InterruptedException e) {
                log.error(ExceptionUtils.getMessage(e));
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
