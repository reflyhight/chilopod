package cn.dtvalley.chilopod.core.common.utils;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.Properties;

public class RedisPoolUtil {

    private static JedisPool jedisPool;

    static {
        Properties properties = new Properties();

        try {
            properties.load(RedisPoolUtil.class.getClassLoader().getResourceAsStream("Jedis.properties"));
            String hosts = properties.getProperty("jedis.hosts");
            String auth = properties.getProperty("jedis.auth");
            Integer port = Integer.parseInt(properties.getProperty("jedis.port"));
            Integer timeout = Integer.parseInt(properties.getProperty("jedis.timeout"));

            GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
            // 设置最大连接数为默认值的 5 倍
            poolConfig.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL * 50);
            // 设置最大空闲连接数为默认值的 3 倍
            poolConfig.setMaxIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 30);
            // 设置最小空闲连接数为默认值的 2 倍
            poolConfig.setMinIdle(GenericObjectPoolConfig.DEFAULT_MIN_IDLE * 20);
            // 设置开启 jmx 功能
            poolConfig.setJmxEnabled(true);
            // 设置连接池没有连接后客户端的最大等待时间 ( 单位为毫秒 )
            poolConfig.setMaxWaitMillis(60000);
            jedisPool = new JedisPool(poolConfig, hosts, port, timeout, auth);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static JedisPool getJedisPool() {
        return jedisPool;
    }

}
