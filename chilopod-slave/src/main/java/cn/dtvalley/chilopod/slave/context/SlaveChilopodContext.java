package cn.dtvalley.chilopod.slave.context;

import cn.dtvalley.chilopod.core.ChilopodContext;
import cn.dtvalley.chilopod.core.Environment;
import cn.dtvalley.chilopod.core.common.utils.NetUtil;
import cn.dtvalley.chilopod.core.instance.SlaveTask;
import cn.dtvalley.chilopod.slave.TaskManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;
import java.time.LocalDateTime;

/**
 * slave context
 */
@Component
@Slf4j
@Getter
public final class SlaveChilopodContext implements ChilopodContext {

    private String ip;
    private String name;
    private int port;
    private LocalDateTime startTime;

    /**
     * 通过遍历所以的task,查看每个task状态
     */
    public Status getStatus() {
        long count = TaskManager.getTasks().values().stream().filter(it -> it.getStatus() == SlaveTask.Status.RUNNING).count();
        return count > 0 ? Status.RUNNING : Status.WAITING;
    }

    @Override
    public Environment getEnvironment() {
        return Environment.SLAVE;
    }

    /**
     * context 启动初始化
     */
    @Component
    class RegisterListener implements ApplicationListener<ServletWebServerInitializedEvent> {
        @Override
        public void onApplicationEvent(ServletWebServerInitializedEvent event) {
            log.info("context init ... ");
            startTime = LocalDateTime.now();
            try {
                ip = NetUtil.getIpAddr();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            port = event.getWebServer().getPort();
            name =event.getApplicationContext().getId();
        }
    }



    public enum Status {
        RUNNING,//运行中
        WAITING,//等待中
        ERROR //异常
    }
}
