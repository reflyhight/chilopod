package cn.dtvalley.chilopod.spider.scheduler;

import us.codecraft.webmagic.Task;


public class RedisKeys {
	

	//URL队列
	private static final String QUEUE_PREFIX = "queue_";

	//去重队列
	private static final String SET_PREFIX = "set_";

	//request对象序列化队列
	private static final String ITEM_PREFIX = "item_";
	
	//工厂队列
	private static final String FACTORY_PREFIX = "factory_";
	

	public static String getSetKey(Task task) {
		return SET_PREFIX + task.getUUID();
	}

	public static String getQueueKey(Task task) {
		return QUEUE_PREFIX + task.getUUID();
	}

	public static String getItemKey(Task task) {
		return ITEM_PREFIX + task.getUUID();
	}
	
	public static String getFactoryKey(Task task) {
		return FACTORY_PREFIX + task.getUUID();
	}
	
	
}
