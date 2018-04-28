package cn.dtvalley.chilopod.spider;

import cn.dtvalley.chilopod.core.common.utils.RedisPoolUtil;
import cn.dtvalley.chilopod.spider.scheduler.RedisScheduler;
import cn.dtvalley.chilopod.spider.scheduler.UrlFactory;
import redis.clients.jedis.JedisPool;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.thread.CountableThreadPool;
import us.codecraft.webmagic.utils.UrlUtils;
import cn.dtvalley.chilopod.core.common.utils.DateUtil;
import cn.dtvalley.chilopod.core.common.utils.NetUtil;

import java.io.Closeable;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ChilopodSpider extends Spider {
    public ChilopodSpider(PageProcessor pageProcessor) {
        super(pageProcessor);
        JedisPool jedisPool = RedisPoolUtil.getJedisPool();
        this.scheduler = new RedisScheduler(jedisPool, this);
        this.factory = new UrlFactory(jedisPool, this);
        this.startRequests = new ArrayList<Request>();
    }

    // 是否是分布式，默认为单机模式
    protected boolean distributed = false;
    // 执行周期标识
    protected String cronExp;
    // Url工厂类
    protected UrlFactory factory;

    public Spider setDistributed(boolean distributed) {
        this.distributed = distributed;
        return this;
    }

    public Spider setCronExp(String cronExp) {
        this.cronExp = cronExp;
        return this;
    }

    @Override // 添加到种子url
    public Spider addUrl(String... urls) {
        // TODO Auto-generated method stub
        for (String url : urls) {
            this.startRequests.add(new Request(url));
        }
        return this;
    }

    @Override
    public Spider addRequest(Request... requests) {
        // TODO Auto-generated method stub
        for (Request request : requests) {
            this.startRequests.add(request);
        }
        return this;
    }

    @Override // 重写种子请求逻辑，因为addRequest和addUrl可能已经添加了一些Request
    public Spider startRequest(List<Request> startRequests) {
        // TODO Auto-generated method stub
        checkIfRunning();
        if (startRequests.size() == 0)
            this.startRequests = startRequests;
        else {
            this.startRequests.addAll(startRequests);
        }
        return this;
    }

    @Override // 重写设置uuid的方法，进制用户设置，采用自动生成的方式
    public Spider setUUID(String uuid) {
        // TODO Auto-generated method stub
        this.uuid = null;
        return this;
    }

    @Override // 重写初始化
    protected void initComponent() {
        this.initCronExp();
        this.initSiteDomain();
        this.initUuid();
        if (!distributed && scheduler instanceof Closeable) {
            // 单机模式，为了方便测试，每次清空队列（一个耗时很长的采集任务，分布式模式就得手动去清除还未采集完的队列，太过麻烦）
            try {
                ((Closeable) scheduler).close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        this.addFactoryUrls();// 放入外部工厂url
        if (startRequests != null) {
            for (Request request : startRequests) {
                scheduler.push(request, this);// 这个直接加入队列
            }
            startRequests.clear();
        }
        if (downloader == null) {
            this.downloader = new HttpClientDownloader();
        }
        if (pipelines.isEmpty()) {
            pipelines.add(new ConsolePipeline());
        }
        downloader.setThread(threadNum);
        if (threadPool == null || threadPool.isShutdown()) {
            if (executorService != null && !executorService.isShutdown()) {
                threadPool = new CountableThreadPool(threadNum, executorService);
            } else {
                threadPool = new CountableThreadPool(threadNum);
            }
        }

        //startTime = new Date();
    }

    private void initCronExp() {
        if (cronExp == null || cronExp.length() == 0) {
            this.cronExp = DateUtil.format(new Date());
        }
    }

    private void initSiteDomain() {
        for (Request request : startRequests) {
            if (site.getDomain() != null)
                break;
            if (request != null && request.getUrl() != null) {
                site.setDomain(UrlUtils.getDomain(request.getUrl()));
            }
        }
    }

    // uuid初始化。爬虫uuid的确定由：pageProcessor类名 和 cronExp 唯一确定
    private void initUuid() {
        if (uuid == null && !distributed) {
            // 单机模式，uuid加入IP,不影响线上的分布式环境
            try {
                this.uuid = this.pageProcessor.getClass().getSimpleName() +
                        "_" + cronExp + "_" + NetUtil.getIpAddr();
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            this.uuid = this.pageProcessor.getClass().getSimpleName() + "_" + cronExp;
        }
    }
    private void addFactoryUrls() {
        List<String> extUrls = this.factory.getBatchUrls(this);
        for (String url : extUrls) {
            scheduler.push(new Request(url), this);
        }
    }
}
