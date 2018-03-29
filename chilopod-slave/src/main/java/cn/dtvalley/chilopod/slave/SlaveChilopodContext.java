package cn.dtvalley.chilopod.slave;

import cn.dtvalley.chilopod.core.ChilopodContext;
import cn.dtvalley.chilopod.core.Environment;
import cn.dtvalley.chilopod.core.common.utils.NetUtil;
import cn.dtvalley.chilopod.slave.register.RegisterConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;


@Component
public final class SlaveChilopodContext implements ChilopodContext {

    private final RegisterConfiguration registerConfiguration;


    private Instance instance;//本服务信息

    public Boolean isWord;//是否正在运行

    private TaskManager taskManager;//执行任务列表

    @Autowired
    public SlaveChilopodContext(RegisterConfiguration registerConfiguration) {
        this.registerConfiguration = registerConfiguration;
    }

    @Override
    public Environment getEnvironment() {
        return Environment.SLAVE;
    }

    void init() {
        isWord = false;
        instance = new Instance();
        try {
            String ip = NetUtil.getIpAddr();
            instance.setIp(ip);
            instance.setMasterIp(registerConfiguration.getIp());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
