package sivan.yue.quarrier.service.segment;

import sivan.yue.quarrier.data.Segment;
import sivan.yue.quarrier.service.ServiceTask;
import sivan.yue.quarrier.service.Service;

import java.util.concurrent.BlockingQueue;

/**
 * Created by xiwen.yxw on 2017/2/15.
 */
public class SegmentService extends Service<Segment>{
    @Override
    protected int singleSearchSeg(Segment seg, byte[] rawData) {
        return 0;
    }

    @Override
    protected ServiceTask createTask(Segment seg, BlockingQueue<Integer> bq) {
        SegmentServiceTask serviceTask = new SegmentServiceTask();
        serviceTask.setBq(bq);
        serviceTask.setSegment(seg);
        serviceTask.overFlag = false;
        return serviceTask;
    }
}
