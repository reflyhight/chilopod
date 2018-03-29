package cn.dtvalley.chilopod.slave;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SlaveController {

    @PostMapping
    public ResponseEntity taskRun(String taskname){
        return null;
    }
}
