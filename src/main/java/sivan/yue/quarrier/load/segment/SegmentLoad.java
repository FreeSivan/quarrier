package sivan.yue.quarrier.load.segment;

import sivan.yue.quarrier.data.Segment;
import sivan.yue.quarrier.schedule.ScheduleCenter;
import sivan.yue.quarrier.load.ILoad;
import sivan.yue.quarrier.service.segment.SegmentService;

/**
 * Created by xiwen.yxw on 2017/2/15.
 */
public class SegmentLoad implements ILoad{

    private SegmentService service;

    public SegmentLoad(SegmentService service) {
        this.service = service;
    }


    @Override
    public void singleLoad(String path) {
        // TODO 迭代每一个
        Segment segment = new Segment();
        // TODO 创建segment的工作
        service.addIndexSegment(segment);
    }

    @Override
    public void multiLoad(String path) {
        // TODO 迭代每一个
        SegmentLoadTask task = new SegmentLoadTask(service);
        // TODO 创建segment的工作
        ScheduleCenter.INSTANCE.addTask(task);
    }
}
