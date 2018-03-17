package cn.dtvalley.chilopod.slave;

import cn.dtvalley.chilopod.slave.register.EnableRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableRegister
@EnableFeignClients(basePackageClasses = SlaveBootstrap.class)
public class SlaveBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(SlaveBootstrap.class, args);
    }
}
