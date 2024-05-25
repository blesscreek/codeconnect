package com.co.judger.utils;

import java.util.concurrent.*;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-23 14:46
 */

public class ThreadPoolUtils {
    private static ExecutorService executorService;
    private static final int cpuNum = Runtime.getRuntime().availableProcessors();
    private ThreadPoolUtils() {
        //手动创建线程池
        executorService = new ThreadPoolExecutor(
                cpuNum,//核心线程数
                cpuNum,//最大线程数。最多几个线程并发
                3,//当非核心线程无任务时，几秒后结束该线程
                TimeUnit.SECONDS,//结束线程时间单位
                new LinkedBlockingDeque<>(200 * cpuNum),//阻塞队列，当线程数达到最大时，新任务会进入队列等待执行
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy());////队列满了，尝试去和最早的竞争，也不会抛出异常！
    }
    private static class PluginConfigHolder{
        private final static ThreadPoolUtils INSTANCE = new ThreadPoolUtils();
    }
    public static ThreadPoolUtils getInstance() {
        return PluginConfigHolder.INSTANCE;
    }
    public ExecutorService getThreadPool() {
        return executorService;
    }
}
