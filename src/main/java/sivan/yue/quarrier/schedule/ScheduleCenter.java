package sivan.yue.quarrier.schedule;

import sivan.yue.quarrier.ITask;

import java.util.concurrent.*;

/**
 * description：任务调度中心
 *
 * 其本质上就是一个线程池，维护后台线程去阻塞队列中去任务并执行
 * 这个类需要后续扩展，对线程数和阻塞队列的长度做扩展
 *
 * Created by xiwen.yxw on 2017/2/11.
 */
public class ScheduleCenter {

    public static ScheduleCenter INSTANCE = new ScheduleCenter();

    private ThreadPoolExecutor threadPool = null;

    private ScheduleCenter() {
        int minSize = 5;
        int maxSize = 20;
        int keepAliveTime = 60;
        BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>(512);
        threadPool = new ThreadPoolExecutor(minSize, maxSize, keepAliveTime, TimeUnit.SECONDS, taskQueue);
    }

    public boolean addTask(ITask task) {
        if (threadPool == null) {
            return false;
        }
        try {
            threadPool.execute(task);
        } catch (RejectedExecutionException e) {
            return false;
        }
        return true;
    }
}
