package cn.dtvalley.chilopod.slave;

import cn.dtvalley.chilopod.core.instance.ClientRequest;
import cn.dtvalley.chilopod.slave.context.SlaveChilopodContext;
import cn.dtvalley.chilopod.slave.register.RegisterConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

/**
 * 自动将自身注册到master
 */
@Component
@Slf4j
public class RegisterThread implements ApplicationListener<ApplicationStartedEvent>, Runnable, DisposableBean {

    @Resource
    private SlaveChilopodContext slaveChilopodContext;
    private final RegisterConfiguration registerConfiguration;

    @Resource
    private RestTemplate restTemplate;

    private Thread thread;
    private boolean flag = true;

    @Autowired
    public RegisterThread(RegisterConfiguration registerConfiguration) {
        this.thread = new Thread(this);
        this.thread.setDaemon(true);
        this.registerConfiguration = registerConfiguration;
    }


    @Override
    public void run() {
        while (flag) {
            try {
                Thread.sleep(30 * 1000);
                List<String> serverIps = registerConfiguration.getServer().getIp();
                serverIps.forEach(it -> {
                    try {
                        URI url = new URI("http://" + it + "/register");
                        ClientRequest request = new ClientRequest();
                        request.setIp(slaveChilopodContext.getIp());
                        request.setPort(slaveChilopodContext.getPort());
                        request.setName(slaveChilopodContext.getName());
                        request.setStatus(slaveChilopodContext.getStatus().name());
                        request.setStartTime(slaveChilopodContext.getStartTime());
                        request.setStartTime(slaveChilopodContext.getStartTime());
                        request.setTasks(TaskManager.getTasks().values());
                        restTemplate.postForEntity(url, request, String.class);
                    } catch (Exception e) {
                        log.error(ExceptionUtils.getMessage(e));
                    }
                });
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
    public void destroy() {
        this.flag = false;
    }
}
