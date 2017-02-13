package sivan.yue.quarrier.build.merger;

import sivan.yue.quarrier.build.Bunch;
import sivan.yue.quarrier.data.Segment;
import sivan.yue.quarrier.schedule.ScheduleCenter;

/**
 * description: segment bunch
 *
 * 该类维护一个segment链表，提供添加segment的接口给外部
 * 当segment到达一定数目时，创建一个merger任务
 *
 * Created by xiwen.yxw on 2017/2/10.
 */
public class MergerBunch extends Bunch<Segment>{

    public static final MergerBunch INSTANCE = new MergerBunch();

    private MergerBunch() {
    }

    @Override
    protected void createTask() {
        MergerTask mergerTask = new MergerTask();
        mergerTask.setSegments(itemList);
        ScheduleCenter.INSTANCE.addTask(mergerTask);
    }

}
