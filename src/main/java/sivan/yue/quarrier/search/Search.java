package sivan.yue.quarrier.search;

import sivan.yue.quarrier.common.exception.BlockQueueException;
import sivan.yue.quarrier.common.schedule.ScheduleCenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by xiwen.yxw on 2017/2/15.
 */
public abstract class Search<T> implements ISearch<T> {

    protected List<T> segList = new ArrayList<T>();

    @Override
    public abstract int singleSearch(byte[] rawData);

    @Override
    public int multiSearch(byte[] rawData) {
        // 结果队列，工作线程将执行结果放入这个阻塞队列中
        BlockingQueue<Integer> bq = new LinkedBlockingQueue<>();
        List<SearchTask> searchTasks = new ArrayList<>();
        int count = 0;
        // 为每个子索引创建一个任务，放入任务调度中心
        for (T seg : segList) {
            SearchTask task = createTask(seg, bq, rawData);
            searchTasks.add(task);
            ScheduleCenter.INSTANCE.addTask(task);
        }
        // 从结果队列中取出所有的结果，判断结果并返回
        // 当有一个工作线程返回正确结果后，设置其他线程的停止位
        while(true) {
            if (count >= segList.size()) {
                break;
            }
            try {
                int ret = bq.take();
                if (ret >= 0) {
                    for (SearchTask t : searchTasks) {
                        t.overFlag = true;
                    }
                    return ret;
                }
                count++;
            } catch (InterruptedException e) {
                count++;
                throw new BlockQueueException("multiSearch Exception!");
            }
        }
        return -1;
    }

    protected abstract SearchTask createTask(T seg, BlockingQueue<Integer> bq, byte[] rawData);

    @Override
    public synchronized void addSubIndex(T subIndex) {
        segList.add(subIndex);
    }

    public List<T> getSegList() {
        return this.segList;
    }
}
