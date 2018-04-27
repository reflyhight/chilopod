package cn.dtvalley.chilopod.slave;

import cn.dtvalley.chilopod.core.instance.SlaveTask;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class TaskManager {
    private static final Map<String, SlaveTask> tasks = new ConcurrentHashMap<>();

    public static Map<String, SlaveTask> getTasks() {
        return tasks;
    }
}
