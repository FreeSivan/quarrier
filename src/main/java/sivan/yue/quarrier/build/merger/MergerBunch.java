package sivan.yue.quarrier.build.merger;

import sivan.yue.quarrier.build.Bunch;
import sivan.yue.quarrier.common.data.Segment;
import sivan.yue.quarrier.common.schedule.ScheduleCenter;

/**
 * description: segment bunch
 *
 * 该类维护一个segment链表，提供添加segment的接口给外部
 * 当segment到达一定数目时，创建一个merger任务
 *
 * Created by xiwen.yxw on 2017/2/10.
 */
public class MergerBunch extends Bunch<Segment>{

    private Bunch<Segment> bunchSegment;

    public MergerBunch() {
        super(10);
    }

    public MergerBunch(int maxCount) {
        super(maxCount);
    }

    @Override
    public synchronized void flush() {
        if (itemList.size() != 0) {
            super.flush();
        }
        MergerTask.setIsFlush(true);
    }

    @Override
    protected void createTask() {
        MergerTask mergerTask = new MergerTask();
        mergerTask.setSegments(itemList);
        mergerTask.setBunchSegment(bunchSegment);
        MergerTask.addCount();
        ScheduleCenter.INSTANCE.addTask(mergerTask);
    }

    public Bunch<Segment> getBunchSegment() {
        return bunchSegment;
    }

    public void setBunchSegment(Bunch<Segment> bunchSegment) {
        this.bunchSegment = bunchSegment;
    }
}
