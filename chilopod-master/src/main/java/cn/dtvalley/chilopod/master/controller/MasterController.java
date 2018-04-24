package cn.dtvalley.chilopod.master.controller;

import cn.dtvalley.chilopod.core.instance.ClientRequest;
import cn.dtvalley.chilopod.master.service.MasterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import java.io.IOException;

@RestController
public class MasterController {

    @Resource
    private MasterService masterService;


    /**
     * 服务注册接口
     */
    @PostMapping("register")
    public ResponseEntity register(@RequestBody ClientRequest instanceInfo) {
        masterService.register(instanceInfo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("register")
    public ResponseEntity list() {
        return ResponseEntity.ok().body(masterService.list());
    }

    /**
     * 服务预约接口
     */
    @PostMapping("renew")
    public ResponseEntity renew(ClientRequest clientRequest) {
        masterService.register(clientRequest);
        return ResponseEntity.ok().build();
    }

    /**
     * 服务删除接口
     */
    @DeleteMapping("cancel")
    public ResponseEntity cancel(ClientRequest instanceInfo) {
        masterService.register(instanceInfo);
        return ResponseEntity.ok().build();
    }


    @PostMapping("dispense")
    public ResponseEntity dispense() throws IOException, ServletException {
        masterService.dispense();
        return ResponseEntity.ok().build();
    }
}
