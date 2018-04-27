package cn.dtvalley.chilopod.core.instance;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
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

    public void addInstance(ClientRequest request) {
        Instance instance = map.get(request.getIp());
        if (!Objects.isNull(instance)) {
            instance.setLastRefreshTime(LocalDateTime.now());
            instance.setRegisterTime(LocalDateTime.now());
            instance.setIp(request.getIp());
            instance.setInstanceName(request.getName());
            instance.setPort(request.getPort());
            instance.setStartTime(request.getStartTime());
            instance.setTasks(request.getTasks());
            instance.setStatus(request.getStatus());
        } else {
            instance = new Instance();
            instance.setLastRefreshTime(LocalDateTime.now());
            instance.setRegisterTime(LocalDateTime.now());
            instance.setIp(request.getIp());
            instance.setInstanceName(request.getName());
            instance.setPort(request.getPort());
            instance.setStartTime(request.getStartTime());
            instance.setTasks(request.getTasks());
            instance.setStatus(request.getStatus());
            map.put(request.getIp(), instance);
        }
    }


    @Data
    public class Instance {
        private String ip;
        private String status;
        private LocalDateTime startTime;
        private Collection<SlaveTask> tasks;
        private int port;
        private LocalDateTime registerTime;//注册时间
        private LocalDateTime LastRefreshTime;//最后一次刷新时间
        private String instanceName;//实例名
    }
}
