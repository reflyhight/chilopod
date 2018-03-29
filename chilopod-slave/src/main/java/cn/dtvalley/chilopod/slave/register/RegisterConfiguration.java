package cn.dtvalley.chilopod.slave.register;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties("register.server")
@Data
@Configuration
public class RegisterConfiguration {
    private List<String> ip = new ArrayList<String>() {{
        add("localhost:8002");
    }};

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }
}
