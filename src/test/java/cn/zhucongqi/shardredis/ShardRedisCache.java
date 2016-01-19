/**
 * 
 */
package cn.zhucongqi.shardredis;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.ext2.plugin.redis.ShardCache;
import com.jfinal.ext2.plugin.redis.ShardRedis;
import com.jfinal.ext2.plugin.redis.ShardRedisPlugin;

import redis.clients.jedis.JedisShardInfo;

/**
 * @author BruceZCQ
 *
 */
public class ShardRedisCache {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		JedisShardInfo shard = new JedisShardInfo("192.168.1.250", 6379);
		shard.setPassword("redisadmin");
		shards.add( shard );
		ShardRedisPlugin redis = new ShardRedisPlugin(shards);
		redis.start();
		
		ShardCache cache = ShardRedis.userShard();
		cache.set("zcq", "--BruceZCQ---");
		cache.set("zcq1", "--BruceZCQ---");
		
		System.out.println(cache.get("zcq1"));
	}

}
