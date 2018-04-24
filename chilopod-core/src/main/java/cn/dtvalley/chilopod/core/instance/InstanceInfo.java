package cn.dtvalley.chilopod.core.instance;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public enum InstanceInfo {
    instanceInfo;

    private static final Map<String, Instance> map = new ConcurrentHashMap<>();

    public Instance getInstance(String ip) {
        return map.get(ip);
    }

    public Map<String, Instance> getInstance() {
        return map;
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
