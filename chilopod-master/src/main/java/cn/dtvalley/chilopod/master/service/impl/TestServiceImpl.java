package cn.dtvalley.chilopod.master.service.impl;

import cn.dtvalley.chilopod.master.feign.TestFeign;
import cn.dtvalley.chilopod.master.service.TestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TestServiceImpl implements TestService{

    @Resource
    private TestFeign testFeign;


    @Override
    public String test() {
        return testFeign.test();
    }
}
