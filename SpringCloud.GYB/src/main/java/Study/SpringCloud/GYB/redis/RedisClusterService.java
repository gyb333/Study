package Study.SpringCloud.GYB.redis;

import Study.SpringCloud.GYB.util.CommonUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

@Service
public class RedisClusterService {
    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 获取当个对象
     */
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {


        //生成真正的key
        String realKey = prefix.getPrefix() + key;
        String str = jedisCluster.get(realKey);
        T t = CommonUtil.stringToBean(str, clazz);
        return t;

    }

    /**
     * 设置对象
     */
    public <T> boolean set(KeyPrefix prefix, String key, T value) {

            String str = CommonUtil.beanToString(value);
            if (str == null || str.length() <= 0) {
                return false;
            }
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            int seconds = prefix.expireSeconds();
            if (seconds <= 0) {
                jedisCluster.set(realKey, str);
            } else {
                jedisCluster.setex(realKey, seconds, str);
            }
            return true;

    }

    /**
     * 判断key是否存在
     */
    public <T> boolean exists(KeyPrefix prefix, String key) {

            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedisCluster.exists(realKey);

    }

    /**
     * 增加值
     */
    public <T> Long incr(KeyPrefix prefix, String key) {

            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedisCluster.incr(realKey);

    }

    /**
     * 减少值
     */
    public <T> Long decr(KeyPrefix prefix, String key) {

            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedisCluster.decr(realKey);

    }




}
