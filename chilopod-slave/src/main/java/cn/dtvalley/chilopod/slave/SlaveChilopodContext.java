package cn.dtvalley.chilopod.slave;

import cn.dtvalley.chilopod.core.ChilopodContext;
import cn.dtvalley.chilopod.core.Environment;
import cn.dtvalley.chilopod.core.common.utils.NetUtil;
import cn.dtvalley.chilopod.core.instance.SlaveTask;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.net.UnknownHostException;
import java.time.LocalDateTime;


@Component
public final class SlaveChilopodContext implements ChilopodContext {

    private String ip;
    private String name;
    private int port;
    private LocalDateTime startTime;


    private Status status; //状态,


    public Status getStatus() {
        long count = TaskManager.getTasks().values().stream().filter(it -> it.getStatus() == SlaveTask.Status.RUNNING).count();
        return count > 0 ? Status.RUNNING : Status.WAITING;
    }

    @Override
    public Environment getEnvironment() {
        return Environment.SLAVE;
    }

    void init() {
        status = Status.WAITING;
        startTime = LocalDateTime.now();
        try {
            ip = NetUtil.getIpAddr();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    @Component
    class RegisterListener implements ApplicationListener<ServletWebServerInitializedEvent> {
        @Override
        public void onApplicationEvent(ServletWebServerInitializedEvent event) {
            port = event.getWebServer().getPort();
            name =event.getApplicationContext().getId();
        }
    }



    enum Status {
        RUNNING,//运行中
        WAITING,//等待中
        ERROR //异常
    }

    public String getIp() {
        return ip;
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

}
