package cn.dtvalley.chilopod.core.instance;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InstanceInfo {
    private static final Map<String, Group> map = new ConcurrentHashMap<>();
    private static final InstanceInfo instanceInfo = new InstanceInfo();
    private InstanceInfo() {
    }

    public static InstanceInfo getInstance() {
        return instanceInfo;
    }

    public List<Instance> getInstance(String name) {
        Group group = map.get(name);
        if (Objects.isNull(group)) {
            return null;
        } else {
            return group.getInstances();
        }
    }

    public void removeInstance(String name, String ip) {
        Group group = map.get(name);
        if (Objects.isNull(group)) {
            throw new RuntimeException("instance 不存在");
        } else {
            Optional<Instance> instance = group.getInstances().stream().filter(it -> Objects.equals(it.getIp(), ip)).findAny();
            if (!instance.isPresent())
                throw new RuntimeException("instance 不存在");
            group.getInstances().remove(instance.get());
        }
    }

    public void addInstance(String name, String ip) {
        Group group = map.get(name);
        if (!Objects.isNull(group)) {
            Optional<Instance> instance = group.getInstances().stream().filter(it -> Objects.equals(it.getIp(), ip)).findAny();
            if (instance.isPresent()) {
                Instance instance1 = instance.get();
                instance1.setLastRefreshTime(LocalDateTime.now());
                instance1.setRegisterTime(LocalDateTime.now());
            } else {
                Instance instance1 = new Instance();
                instance1.setLastRefreshTime(LocalDateTime.now());
                instance1.setRegisterTime(LocalDateTime.now());
                instance1.setIp(ip);
                group.getInstances().add(instance1);
            }
        } else {
            group = new Group();
            List<Instance> instances = new ArrayList<>();
            Instance instance1 = new Instance();
            instance1.setLastRefreshTime(LocalDateTime.now());
            instance1.setRegisterTime(LocalDateTime.now());
            instance1.setIp(ip);
            instances.add(instance1);
            group.setInstances(instances);
            map.put(name, group);
        }
    }

    @Data
    private class Group {
        private String name;
        private List<Instance> instances;//ip
    }

    @Data
    public class Instance {
        private String ip;
        private LocalDateTime registerTime;//注册时间
        private LocalDateTime LastRefreshTime;//最后一次刷新时间
        private String instanceName;//实例名
    }
}
