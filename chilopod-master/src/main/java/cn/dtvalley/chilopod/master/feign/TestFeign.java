package cn.dtvalley.chilopod.master.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "test" , url = "www.baidu.com")
public interface TestFeign {

    @GetMapping("/")
    String test();
}
