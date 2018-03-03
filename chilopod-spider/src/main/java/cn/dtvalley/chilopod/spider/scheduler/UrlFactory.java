package cn.dtvalley.chilopod.spider.scheduler;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import us.codecraft.webmagic.Task;

public class UrlFactory {

	protected JedisPool pool;
	protected Task task;

	public UrlFactory(JedisPool pool, Task task) {
		this.pool = pool;
		this.task = task;
	}


	/**
	 *	拉取种子URL
	 * @param task
	 * @return
	 */
	public List<String> getBatchUrls(Task task) {

		Jedis jedis = pool.getResource();
		List<String> urls = null;
		try {

			urls = jedis.lrange(RedisKeys.getFactoryKey(this.task), 0, -1);
			jedis.ltrim(RedisKeys.getFactoryKey(this.task), 1, 0);
		} catch (Exception e) {
			// TODO: handle exception
			jedis.close();
		}
		return urls;
	}

}
