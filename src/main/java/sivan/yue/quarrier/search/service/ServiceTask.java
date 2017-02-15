package sivan.yue.quarrier.search.service;

import sivan.yue.quarrier.ITask;

/**
 * Created by xiwen.yxw on 2017/2/15.
 */
public abstract class ServiceTask implements ITask {
    public volatile boolean overFlag;
}
