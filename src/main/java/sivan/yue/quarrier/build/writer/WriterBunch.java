package sivan.yue.quarrier.build.writer;

import sivan.yue.quarrier.build.Bunch;
import sivan.yue.quarrier.common.data.Segment;
import sivan.yue.quarrier.common.schedule.ScheduleCenter;

/**
 * description: segment bunch
 *
 * 该类维护一个segment链表，提供添加segment的接口给外部
 * 当segment到达一定数目时，创建一个writer任务
 *
 * Created by xiwen.yxw on 2017/2/11.
 */
public class WriterBunch extends Bunch<Segment>{

    @Override
    protected void createTask() {
        WriterTask writerTask = new WriterTask();
        writerTask.setSegments(itemList);
        ScheduleCenter.INSTANCE.addTask(writerTask);
    }

}
