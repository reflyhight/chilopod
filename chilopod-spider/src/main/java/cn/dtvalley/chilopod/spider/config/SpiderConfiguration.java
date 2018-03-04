package cn.dtvalley.chilopod.spider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * spider配置类
 */
@ConfigurationProperties(prefix = "chilopod.spider")
@Configuration
@Data
public class SpiderConfiguration {
    /**
     * 是否是分布式，默认为单机模式
     */
    private Boolean distributed = false;
}
