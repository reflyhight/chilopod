package cn.dtvalley.chilopod.master.config;

import cn.dtvalley.chilopod.master.MasterBootstrap;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = MasterBootstrap.class)
public class FeignConfig {
}
