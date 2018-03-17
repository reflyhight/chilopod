package cn.dtvalley.chilopod.master.service.impl;

import cn.dtvalley.chilopod.master.service.URLService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class URLServiceImpl implements URLService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String pop(String key) {
        String value = stringRedisTemplate.opsForList().rightPop(key);
        return value;
    }

    @Override
    public Long push(String key, String value) {
        long res = stringRedisTemplate.opsForList().leftPush(key, value);
        return res;
    }
}
