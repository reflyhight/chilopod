package cn.dtvalley.chilopod.slave;

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
import java.util.Collection;

@RestController
public class SlaveController {

    @PostMapping
    public ResponseEntity taskRun(String taskname) {
        return null;
    }

    @PostMapping("slave/jar")
    public ResponseEntity jar() throws IOException, ServletException {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Collection<Part> parts = request.getParts();
        parts.forEach(it -> {
            String path = request.getServletContext().getRealPath("/");
            File file = new File(path+"/jar/" + it.getName());
            System.out.println(file.getAbsoluteFile());
            try {
                FileUtils.copyToFile(it.getInputStream(), file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(it);
        });
        return null;
    }
}
