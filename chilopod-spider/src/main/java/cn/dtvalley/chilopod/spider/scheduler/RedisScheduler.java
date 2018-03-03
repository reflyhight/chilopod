package cn.dtvalley.chilopod.spider.scheduler;

import java.io.Closeable;
import java.io.IOException;
import org.apache.commons.codec.digest.DigestUtils;
import com.alibaba.fastjson.JSON;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.DuplicateRemovedScheduler;
import us.codecraft.webmagic.scheduler.MonitorableScheduler;


public class RedisScheduler extends DuplicateRemovedScheduler implements MonitorableScheduler, Closeable {

	private JedisPool pool;
	private Task task;

	public RedisScheduler(JedisPool pool, Task task) {
		this.pool = pool;
		this.task = task;
		// 设置去重器
		setDuplicateRemover(new RedisDuplicateRemover(pool, task));
	}

	@Override // 放入队列的过程
	protected void pushWhenNoDuplicate(Request request, Task task) {
		Jedis jedis = pool.getResource();
		try {
			jedis.rpush(RedisKeys.getQueueKey(this.task), request.getUrl());

			if (request.getExtras() != null) {
				String field = DigestUtils.shaHex(request.getUrl());
				String value = JSON.toJSONString(request);
				jedis.hset((RedisKeys.getItemKey(task)), field, value);
			}
		} finally {
			jedis.close();
		}
	}

	@Override // 消费的过程
	public synchronized Request poll(Task task) {
		Jedis jedis = pool.getResource();
		try {
			String url = jedis.lpop(RedisKeys.getQueueKey(this.task));
			if (url == null) {
				return null;
			}
			String key = RedisKeys.getItemKey(this.task);
			String field = DigestUtils.shaHex(url);

			byte[] bytes = jedis.hget(key.getBytes(), field.getBytes());
			if (bytes != null) {
				Request o = JSON.parseObject(new String(bytes), Request.class);
				return o;
			}
			Request request = new Request(url);
			return request;
		} finally {
			jedis.close();
		}
	}

	@Override // 获取目前还剩的采集请求（目前QUEUE中剩下的requests）
	public int getLeftRequestsCount(Task task) {
		Jedis jedis = pool.getResource();
		try {
			Long size = jedis.llen(RedisKeys.getQueueKey(this.task));
			return size.intValue();
		} finally {
			jedis.close();
		}
	}

	@Override // 获取总的目标采集请求数
	public int getTotalRequestsCount(Task task) {
		Jedis jedis = pool.getResource();
		try {
			Long size = jedis.scard(RedisKeys.getSetKey(this.task));
			return size.intValue();
		} finally {
			jedis.close();
		}
	}

	@Override // 如果采集能正常停止，关闭
	public void close() throws IOException {
		// TODO Auto-generated method stub
		clean();
	}

	public void clean() {
		Jedis jedis = pool.getResource();
		try {

			jedis.del(RedisKeys.getItemKey(this.task));
			jedis.del(RedisKeys.getQueueKey(this.task));
			jedis.del(RedisKeys.getSetKey(this.task));
		} catch (Exception e) {
			// TODO: handle exception
			jedis.close();
		}
	}
}
