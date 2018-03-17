package cn.dtvalley.chilopod.master.controller;

import cn.dtvalley.chilopod.core.instance.ClientRequest;
import cn.dtvalley.chilopod.core.instance.InstanceInfo;
import cn.dtvalley.chilopod.master.service.MasterService;
import cn.dtvalley.chilopod.master.service.URLService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class MasterController {
    @Resource
    private URLService urlService;
    @Resource
    private MasterService masterService;

    /**
     * slave执行任务完成后上报接口
     * master负责将以完成任务标识完成
     * master制订已完成部分数据迁移工作[异步](是否需要)
     * 此接口支持幂等操作
     */
    @GetMapping("")
    public String jobFinish(){
        return null;
    }

    /**
     * slave 通过此接口获取url进行爬虫
     */
    @GetMapping("/pop")
    public String getURL(String key){
        return urlService.pop(key);
    }


    /**
     * slave节点 通过此接口将解析后的接口进行上传
     */
    @GetMapping("/push")
    public Long push(String key, String value){
        return urlService.push(key , value);
    }

    /**
     * 服务注册接口
     * todo 目前暂时实现简易版,后期优化,集成zookeeper和eureka
     */
    @PostMapping("register")
    public String register(@RequestBody ClientRequest instanceInfo){
        masterService.register(instanceInfo);
        return null;
    }

    /**
     * 服务预约接口
     */
    @PostMapping("renew")
    public String renew(ClientRequest clientRequest){
        masterService.register(clientRequest);
        return null;
    }
    /**
     * 服务删除接口
     */
    @DeleteMapping("cancel")
    public String cancel(ClientRequest instanceInfo){
        masterService.register(instanceInfo);
        return null;
    }

    /**
     * 服务集群同步接口
     */
    @DeleteMapping("sync")
    public String sync(ClientRequest instanceInfo){
        masterService.register(instanceInfo);
        return null;
    }

}
