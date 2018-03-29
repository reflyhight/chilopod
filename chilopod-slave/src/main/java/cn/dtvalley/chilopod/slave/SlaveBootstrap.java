package cn.dtvalley.chilopod.slave;

import cn.dtvalley.chilopod.slave.register.EnableRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableRegister
public class SlaveBootstrap {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(SlaveBootstrap.class, args);
        initChilopodContext(applicationContext);
    }

    private static void initChilopodContext(ApplicationContext applicationContext) {
        SlaveChilopodContext chilopodContext = applicationContext.getBean(SlaveChilopodContext.class);
        chilopodContext.init();
    }
}
