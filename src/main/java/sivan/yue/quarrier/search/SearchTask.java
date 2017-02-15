package sivan.yue.quarrier.search;

import sivan.yue.quarrier.ITask;

/**
 * description: 所有search任务的基类
 * Created by xiwen.yxw on 2017/2/15.
 */
public abstract class SearchTask implements ITask {
    public volatile boolean overFlag;
}
