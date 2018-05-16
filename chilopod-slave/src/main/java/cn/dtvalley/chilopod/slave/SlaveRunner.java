package cn.dtvalley.chilopod.slave;

import cn.dtvalley.chilopod.core.SlaveRun;
import cn.dtvalley.chilopod.core.instance.SlaveTask;
import cn.dtvalley.chilopod.slave.dao.TaskDomain;
import cn.dtvalley.chilopod.slave.dao.TaskRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Date;
import java.util.List;

/**
 * 启动加载已有jar
 */
@Component
@Slf4j
public class SlaveRunner implements ApplicationRunner {
    @Resource
    private TaskRepository taskRepository;
    @Resource
    private ObjectMapper objectMapper;

    @Override
    public void run(ApplicationArguments args) throws JsonProcessingException {
        log.info("load task start ... ");
        List<TaskDomain> tasks = taskRepository.findAll();
        log.info("find tasks {}", objectMapper.writeValueAsString(tasks));
        tasks.forEach(task -> {
            File file = new File(task.getPath());
            if (file.exists()) {
                try {
                    URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{file.toURI().toURL()});
                    Class c = urlClassLoader.loadClass(task.getMainClass());
                    SlaveRun o = (SlaveRun) c.newInstance();
                    SlaveTask slaveTask = new SlaveTask();
                    slaveTask.setName(task.getName());
                    slaveTask.setCreateTime(new Date().getTime());
                    slaveTask.setStatus(SlaveTask.Status.SLEEP);
                    slaveTask.setPath(file.getAbsolutePath());
                    slaveTask.setRunObject(o);
                    TaskManager.getTasks().put(task.getName(), slaveTask);
                } catch (Exception e) {
                    log.error(ExceptionUtils.getMessage(e));
                }
            }
        });
    }
}
