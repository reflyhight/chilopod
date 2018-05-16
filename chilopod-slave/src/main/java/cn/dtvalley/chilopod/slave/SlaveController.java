package cn.dtvalley.chilopod.slave;

import cn.dtvalley.chilopod.core.SlaveRun;
import cn.dtvalley.chilopod.core.instance.SlaveTask;
import cn.dtvalley.chilopod.core.instance.TaskStartParam;
import cn.dtvalley.chilopod.slave.context.SlaveChilopodContext;
import cn.dtvalley.chilopod.slave.dao.TaskDomain;
import cn.dtvalley.chilopod.slave.dao.TaskRepository;
import cn.dtvalley.chilopod.slave.register.RegisterConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.Future;

@RestController
public class SlaveController {

    @Resource
    private RegisterConfiguration registerConfiguration;
    @Resource
    private TaskRepository taskRepository;
    @Resource
    private SlaveChilopodContext slaveChilopodContext;

    @PostMapping("/slave/task")
    public ResponseEntity taskRun(@RequestBody TaskStartParam param) {
        SlaveTask task = TaskManager.getTasks().get(param.getTaskName());
        if (Objects.isNull(task))
            throw new RuntimeException("未找到task");
        switch (param.getType()) {
            case "run":
                if (task.getStatus() == SlaveTask.Status.RUNNING)
                    throw new RuntimeException("task正在运行");
                Future future = TaskManager.threadPoolExecutor.submit(() -> {
                    SlaveRun slaveRun = task.getRunObject();
                    task.setStatus(SlaveTask.Status.RUNNING);
                    try {
                        if (slaveRun.init()) {
                            slaveRun.run();
                            slaveRun.destroy();
                        }
                    } catch (Exception e) {
                        System.out.println(ExceptionUtils.getMessage(e));
                    } finally {
                        task.setStatus(SlaveTask.Status.SLEEP);
                        task.setFuture(null);
                    }
                });
                task.setFuture(future);
                break;
            case "cancel":
                if (task.getFuture() != null) {
                    task.getFuture().cancel(true);
                }
                task.setStatus(SlaveTask.Status.SLEEP);
                task.setFuture(null);
                break;
            default:
                throw new RuntimeException("类型不匹配");
        }


        return ResponseEntity.ok().build();
    }

    @PostMapping("slave/jar")
    public ResponseEntity jar() throws IOException, ServletException {

        TaskDomain taskDomain = new TaskDomain();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Part part = request.getPart("file");
        String name = request.getParameter("taskName");
        String taskStartClass = request.getParameter("taskStartClass");
        taskDomain.setMainClass(taskStartClass);
        String path = StringUtils.isBlank(registerConfiguration.getTask().getPath()) ?
                request.getServletContext().getRealPath("/") :
                registerConfiguration.getTask().getPath();
        File file = new File(path + "/jar/" + name + ".jar");
        taskDomain.setPath(file.getAbsolutePath());
        try {
            FileUtils.copyToFile(part.getInputStream(), file);

            URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{file.getAbsoluteFile().toURI().toURL()});

            Class c = urlClassLoader.loadClass(taskStartClass);
            SlaveRun o = (SlaveRun) c.newInstance();
            SlaveTask slaveTask = new SlaveTask();
            slaveTask.setName(name);
            taskDomain.setName(name);
            taskDomain.setStatus(SlaveTask.Status.SLEEP.name());
            taskDomain.setIp(slaveChilopodContext.getIp());
            taskDomain.setInstanceName(slaveChilopodContext.getName());
            slaveTask.setCreateTime(new Date().getTime());
            slaveTask.setStatus(SlaveTask.Status.SLEEP);
            slaveTask.setPath(file.getAbsolutePath());
            slaveTask.setRunObject(o);
            TaskManager.getTasks().put(name, slaveTask);
            taskRepository.save(taskDomain);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("系统异常");
        }
        return ResponseEntity.ok().build();
    }
}
