package com.wolf.test.redis;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.FileCopyUtils;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * <br/> Created on 2017/5/15 13:12
 *
 * @author 李超
 * @since 1.0.0
 */
public class RateLimit {

    private String script;
    private Jedis jedis;

    public RateLimit(Jedis jedis) {
        this.jedis = jedis;
    }

    // 省略了构造方法
    public void init() throws Exception {
        ClassPathResource resource = new ClassPathResource("ratelimiting.lua");
        script = FileCopyUtils.copyToString(new EncodedResource(resource, "UTF-8").getReader());
    }

    /**
     * 提供限制速率的功能
     *
     * @param key           关键字
     * @param expireSeconds 过期时间,秒
     * @param count         在过期时间内可以访问的次数
     * @return 没有超过指定次数则返回true, 否则返回false
     */
    public boolean isExceedRate(String key, long expireSeconds, int count) {
        List<String> params = new ArrayList<>();
        params.add(Long.toString(expireSeconds));
        params.add(Integer.toString(count));
        List<String> keys = new ArrayList<>(1);
        keys.add(key);
        Long canSend = (Long) jedis.eval(script, keys, params);
        return canSend == 0;
    }
}
