package sivan.yue.quarrier.build.creator;

import sivan.yue.quarrier.build.Bunch;
import sivan.yue.quarrier.data.Document;
import sivan.yue.quarrier.schedule.ScheduleCenter;

/**
 * description: creator bunch
 *
 * 维护一个文档的列表，提供添加文档的接口从外部接受文档，
 * 当文档数达到一定数目时（默认是100），创建一个create任务
 *
 * Created by xiwen.yxw on 2017/2/10.
 */
public class CreatorBunch extends Bunch<Document>{

    public static final CreatorBunch INSTANCE = new CreatorBunch();

    private CreatorBunch() {
    }

    @Override
    public synchronized  void addItem(Document value) {
        if (value.content.length % 4 != 0) {
            return;
        }
        super.addItem(value);
    }

    @Override
    protected void createTask() {
        CreatorTask createTask = new CreatorTask();
        createTask.setDocList(itemList);
        ScheduleCenter.INSTANCE.addTask(createTask);
    }

}