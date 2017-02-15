package sivan.yue.quarrier.search.service;

import sivan.yue.quarrier.schedule.ScheduleCenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by xiwen.yxw on 2017/2/15.
 */
public abstract class Service<T> implements IService<T>{

    protected List<T> segList = new ArrayList<T>();

    @Override
    public int singleSearch(byte[] rawData) {
        for (T seg : segList) {
            int retId = singleSearchSeg(seg, rawData);
            if (retId > 0) {
                return retId;
            }
        }
        return -1;
    }

    protected abstract int singleSearchSeg(T seg, byte[] rawData);

    @Override
    public int multiSearch(byte[] rawData) {
        BlockingQueue<Integer> bq = new LinkedBlockingQueue<>();
        List<ServiceTask> searchTasks = new ArrayList<>();
        int count = 0;
        for (T seg : segList) {
            ServiceTask task = createTask(seg, bq);
            searchTasks.add(task);
            ScheduleCenter.INSTANCE.addTask(task);
        }
        while(true) {
            if (count >= segList.size()) {
                break;
            }
            try {
                int ret = bq.take();
                if (ret >= 0) {
                    for (ServiceTask t : searchTasks) {
                        t.overFlag = true;
                    }
                    return ret;
                }
                count++;
            } catch (InterruptedException e) {
                count++;
                e.printStackTrace();
            }
        }
        return -1;
    }

    protected abstract ServiceTask createTask(T seg, BlockingQueue<Integer> bq);

    @Override
    public synchronized void addIndexSegment(T segment) {
        segList.add(segment);
    }
}
