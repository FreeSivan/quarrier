package sivan.yue.quarrier.build;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * description:
 *
 * 数据开始处理的单位，比如一捆doc开始建立索引
 * 一捆segment进行合并， 一捆segment写磁盘
 *
 * 维护的列表，提供添加文档的接口从外部接受文档，
 *
 * Created by xiwen.yxw on 2017/2/10.
 */
public abstract class Bunch <T> {
    /**
     * 单元列表
     */
    protected List<T> itemList;
    /**
     * 当前单元数量
     */
    protected int itemCount;
    /**
     * 最大单元数量，到达这个数目创建相应任务
     */
    protected int maxItemCount;

    public Bunch(int maxCount) {
        this.init(maxCount);
    }

    private void init(int maxCount) {
        this.itemList = new ArrayList<>();
        this.itemCount = 0;
        this.maxItemCount = maxCount;
    }

    /**
     * 设置触发创建任务的item数量的阈值
     * @param maxCount 阈值
     */
    public synchronized void setMaxCount(int maxCount) {
        this.maxItemCount = maxCount;
    }

    /**
     * 该方法是个模板方法，向bunch中添加一个Item
     * 如果item数目到达maxItemCount：
     *      1. 触发一次创建任务的行为
     *      2. itemCount置为0
     *      3. 创建一个新的队列
     * @param value
     */
    public synchronized  void addItem(T value) {
        this.itemList.add(value);
        itemCount ++;
        if (itemCount >= maxItemCount) {
            createTask();
            itemList = new ArrayList<>();
            itemCount = 0;
        }
    }

    public synchronized void flush() {
        createTask();
        itemList = new ArrayList<>();
        itemCount = 0;
    }

    /**
     * 由子类实现，方便不同的子类创建不同的任务
     */
    protected abstract void createTask();

}