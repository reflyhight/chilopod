package cn.dtvalley.chilopod.spider.scheduler;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.component.DuplicateRemover;


public class RedisDuplicateRemover implements DuplicateRemover{
	protected JedisPool pool;
	protected Task task;
	
	public RedisDuplicateRemover(JedisPool pool,Task task){
		this.pool=pool;
		this.task=task;
	}
	

	@Override
	public boolean isDuplicate(Request request, Task task) {
		// TODO Auto-generated method stub
		Jedis jedis = pool.getResource();
		try {
			return jedis.sadd(RedisKeys.getSetKey(this.task), request.getUrl()) == 0;
		} finally {
			jedis.close();
		}
	}

	@Override
	public void resetDuplicateCheck(Task task) {
		// TODO Auto-generated method stub
		Jedis jedis = pool.getResource();
		try {
			jedis.del(RedisKeys.getSetKey(this.task));
		} finally {
			jedis.close();
		}
	}

	@Override
	public int getTotalRequestsCount(Task task) {
		// TODO Auto-generated method stub
		Jedis jedis = pool.getResource();
		try {
			Long size = jedis.scard(RedisKeys.getSetKey(this.task));
			return size.intValue();
		} finally {
			jedis.close();
		}
	}

}
