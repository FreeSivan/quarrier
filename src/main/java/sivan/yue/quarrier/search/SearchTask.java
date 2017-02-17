package sivan.yue.quarrier.search;

import sivan.yue.quarrier.ITask;

import java.util.concurrent.BlockingQueue;

/**
 * description: 所有search任务的基类
 * Created by xiwen.yxw on 2017/2/15.
 */
public abstract class SearchTask implements ITask {

    public volatile boolean overFlag;

    protected BlockingQueue<Integer> bq;

    protected byte[] rawData;

    public SearchTask(BlockingQueue<Integer> bq, byte[] rawData) {
        overFlag = false;
        this.bq = bq;
        this.rawData = rawData;
    }
}
