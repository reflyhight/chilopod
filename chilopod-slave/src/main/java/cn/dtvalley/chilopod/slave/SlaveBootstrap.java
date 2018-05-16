package cn.dtvalley.chilopod.slave;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class SlaveBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(SlaveBootstrap.class, args);
    }
}
