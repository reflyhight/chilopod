package cn.dtvalley.chilopod.slave.register;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties("slave")
@Data
@Configuration
public class RegisterConfiguration {
    @Getter
    private Server server = new Server();
    @Getter
    private Task task = new Task();

    @Data
    public static class Server {
        private List<String> ip = new ArrayList<String>() {{
            add("localhost:8002");
        }};
    }

    @Data
    public static class Task {
        private String path ;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }
}
