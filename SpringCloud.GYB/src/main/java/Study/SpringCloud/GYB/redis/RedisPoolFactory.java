package Study.SpringCloud.GYB.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RedisPoolFactory {

    @Autowired
    RedisConfig redisConfig;

    @Bean
    public JedisPool JedisPoolFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
        poolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
        poolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait() * 1000);
        JedisPool jp = new JedisPool(poolConfig, redisConfig.getHost(), redisConfig.getPort(),
                redisConfig.getTimeout() * 1000, redisConfig.getPassword(), 0);
        return jp;
    }

    @Bean
    public ShardedJedisPool ShardedJedisPoolFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
        poolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
        poolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait() * 1000);

        String[] nodes = redisConfig.getNodes().split(",");
        ShardedJedisPool sjp = null;
        if (nodes != null && nodes.length > 0) {
            List<JedisShardInfo> jdsInfoList = new ArrayList<JedisShardInfo>();
            JedisShardInfo info = null;
            String Host=null;
            int Port =0;
            for (String each : nodes) {
                String[] node = each.split(":");
                Host=node[0];
                Port=Integer.parseInt(node[1]);
                info = new JedisShardInfo(Host,Port );
                info.setPassword(redisConfig.getPassword());
                jdsInfoList.add(info);

                HostAndPort hostAndPort = new HostAndPort(Host, Port);
                Set<HostAndPort> hostAndPortSet = new HashSet<HostAndPort>();
                hostAndPortSet.add(hostAndPort);
                JedisCluster jedis = new JedisCluster(hostAndPortSet);
                
            }
            sjp = new ShardedJedisPool(poolConfig, jdsInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
        }

        return sjp;
    }

}
