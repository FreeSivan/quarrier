package sivan.yue.quarrier.search;

import sivan.yue.quarrier.ITask;

import java.util.concurrent.BlockingQueue;

/**
 * description: 所有search任务的基类
 *
 * Created by xiwen.yxw on 2017/2/15.
 */
public abstract class SearchTask implements ITask {

    /**
     * 结束标志
     */
    public volatile boolean overFlag;
    /**
     * 结果队列
     */
    protected BlockingQueue<Integer> bq;
    /**
     * 待检索的二进制流
     */
    protected byte[] rawData;

    /**
     * description : 检索一段二进制流，将结果放入到结果队列中
     *
     * @param bq 存放搜索结果的队列，未检索到存入-1
     * @param rawData 待检索的二进制流
     */
    public SearchTask(BlockingQueue<Integer> bq, byte[] rawData) {
        overFlag = false;
        this.bq = bq;
        this.rawData = rawData;
    }
}
