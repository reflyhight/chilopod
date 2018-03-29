package cn.dtvalley.chilopod.core.instance;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class InstanceInfo {
    private static final Map<String, Instance> map = new ConcurrentHashMap<>();
    private static final InstanceInfo instanceInfo = new InstanceInfo();

    private InstanceInfo() {
    }

    public static InstanceInfo getInstance() {
        return instanceInfo;
    }

    public Instance getInstance(String ip) {
        return map.get(ip);
    }

    public void removeInstance(String ip) {
        map.remove(ip);
    }

    public void addInstance(String ip) {
        Instance instance = map.get(ip);
        if (!Objects.isNull(instance)) {
            instance.setLastRefreshTime(LocalDateTime.now());
            instance.setRegisterTime(LocalDateTime.now());
        } else {
            instance = new Instance();
            instance.setLastRefreshTime(LocalDateTime.now());
            instance.setRegisterTime(LocalDateTime.now());
            instance.setIp(ip);
            map.put(ip, instance);
        }
    }

    @Data
    public class Instance {
        private String ip;
        private LocalDateTime registerTime;//注册时间
        private LocalDateTime LastRefreshTime;//最后一次刷新时间
        private String instanceName;//实例名
    }
}
