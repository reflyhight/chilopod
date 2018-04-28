package cn.dtvalley.chilopod.slave;

import cn.dtvalley.chilopod.core.instance.SlaveTask;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public final class TaskManager {
    private TaskManager(){

    }

    public  static  final ExecutorService threadPoolExecutor = Executors.newCachedThreadPool();

    private static final Map<String, SlaveTask> tasks = new ConcurrentHashMap<>();

    public static Map<String, SlaveTask> getTasks() {
        return tasks;
    }
}
