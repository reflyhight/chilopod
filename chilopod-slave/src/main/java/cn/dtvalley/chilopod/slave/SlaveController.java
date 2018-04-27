package cn.dtvalley.chilopod.slave;

import cn.dtvalley.chilopod.core.SlaveRun;
import cn.dtvalley.chilopod.core.instance.SlaveTask;
import org.apache.commons.io.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@RestController
public class SlaveController {

    @PostMapping
    public ResponseEntity taskRun(String taskname) {
        return null;
    }

    @PostMapping("slave/jar")
    public ResponseEntity jar() throws IOException, ServletException {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Part part = request.getPart("file");
        String name = request.getParameter("taskName");
        String taskStartClass = request.getParameter("taskStartClass");

        String path = request.getServletContext().getRealPath("/");
        File file = new File(path + "/jar/" + name);
        System.out.println(file.getAbsoluteFile());
        try {
            FileUtils.copyToFile(part.getInputStream(), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        JarFile jarFile = new JarFile(file.getAbsolutePath());
//        Enumeration<JarEntry> jarEntryEnumeration =  jarFile.entries();
//        while (jarEntryEnumeration.hasMoreElements()) {
//            JarEntry jarEntry = jarEntryEnumeration.nextElement();
//            String fileName = jarEntry.getName();
//            if (name != null && name.endsWith(".class")) {//只解析了.class文件，没有解析里面的jar包
//            }
//
//        }

        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{file.getAbsoluteFile().toURI().toURL()});

        try {
            Class c = urlClassLoader.loadClass(taskStartClass);
            SlaveRun o = (SlaveRun) c.newInstance();
            SlaveTask slaveTask = new SlaveTask();
            slaveTask.setName(name);
            slaveTask.setCreateTime(new Date().getTime());
            slaveTask.setStatus(SlaveTask.Status.SLEEP);
            slaveTask.setPath(file.getAbsolutePath());
            slaveTask.setRunObject(o);
            TaskManager.getTasks().put(name , slaveTask);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
