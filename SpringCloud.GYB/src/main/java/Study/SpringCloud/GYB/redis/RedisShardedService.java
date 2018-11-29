package Study.SpringCloud.GYB.redis;

import Study.SpringCloud.GYB.util.CommonUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.HashSet;
import java.util.Set;

@Service
public class RedisShardedService {

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    /**
     * 获取当个对象
     * */
    public <T> T get(KeyPrefix prefix, String key,  Class<T> clazz) {

        ShardedJedis jedis = null;
        try {
            jedis =  shardedJedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            String  str = jedis.get(realKey);
            T t =  CommonUtil.stringToBean(str, clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 设置对象
     * */
    public <T> boolean set(KeyPrefix prefix, String key,  T value) {
        ShardedJedis jedis = null;

        try {
            jedis =  shardedJedisPool.getResource();
            String str = CommonUtil.beanToString(value);
            if(str == null || str.length() <= 0) {
                return false;
            }
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            int seconds =  prefix.expireSeconds();
            if(seconds <= 0) {
                jedis.setnx(realKey, str);
            }else {
                jedis.setex(realKey, seconds, str);
            }
            return true;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 判断key是否存在
     * */
    public <T> boolean exists(KeyPrefix prefix, String key) {
        ShardedJedis jedis = null;
        try {
            jedis =  shardedJedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.exists(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 增加值
     * */
    public <T> Long incr(KeyPrefix prefix, String key) {
        ShardedJedis jedis = null;
        try {
            jedis =  shardedJedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.incr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 减少值
     * */
    public <T> Long decr(KeyPrefix prefix, String key) {
        ShardedJedis jedis = null;
        try {
            jedis =  shardedJedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.decr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }



    private void returnToPool(ShardedJedis jedis) {
        if(jedis != null) {
            jedis.close();
        }
    }



}
