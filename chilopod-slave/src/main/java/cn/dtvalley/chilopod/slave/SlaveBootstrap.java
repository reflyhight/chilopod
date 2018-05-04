package cn.dtvalley.chilopod.slave;

import cn.dtvalley.chilopod.core.SlaveRun;
import cn.dtvalley.chilopod.core.instance.SlaveTask;
import cn.dtvalley.chilopod.slave.register.RegisterConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SpringBootApplication
public class SlaveBootstrap {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(SlaveBootstrap.class, args);
        initChilopodContext(applicationContext);
    }

    private static void initChilopodContext(ApplicationContext applicationContext) {
        SlaveChilopodContext chilopodContext = applicationContext.getBean(SlaveChilopodContext.class);
        chilopodContext.init();

        String path = applicationContext.getBean(RegisterConfiguration.class).getTask().getPath();
        String taskPath = StringUtils.isBlank(path) ? ((AnnotationConfigServletWebServerApplicationContext) applicationContext).getServletContext().getRealPath("/") : path;

        File file = new File(taskPath + "/jar");
        if (!file.isDirectory()) throw new RuntimeException("task ptah 配置错误");
        File[] files = file.listFiles();
        if (files == null) return;
        URL[] list = Arrays.stream(files).map(it -> {
            try {
                return it.toURI().toURL();
            } catch (MalformedURLException e) {
                return null;
            }
        }).filter(it -> !Objects.isNull(it)).toArray(URL[]::new);
        URLClassLoader urlClassLoader = new URLClassLoader(list);


        Arrays.stream(files).forEach(it -> {
            Class c;
            try {
                c = urlClassLoader.loadClass("com.stmt.test.Test");// fixme
                SlaveRun o;
                try {
                    o = (SlaveRun) c.newInstance();
                    SlaveTask slaveTask = new SlaveTask();
                    slaveTask.setName("test1");
                    slaveTask.setCreateTime(new Date().getTime());
                    slaveTask.setStatus(SlaveTask.Status.SLEEP);
                    slaveTask.setPath(file.getAbsolutePath());
                    slaveTask.setRunObject(o);
                    TaskManager.getTasks().put("test1", slaveTask);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }
}
