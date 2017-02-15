package sivan.yue.quarrier.search.load.segment;

import sivan.yue.quarrier.ITask;
import sivan.yue.quarrier.data.Segment;
import sivan.yue.quarrier.search.SearchTask;
import sivan.yue.quarrier.search.service.segment.SegmentService;

/**
 * Created by xiwen.yxw on 2017/2/15.
 */
public class SegmentLoadTask implements ITask{

    private SegmentService service;

    public SegmentLoadTask(SegmentService service) {
        this.service = service;
    }

    @Override
    public void run() {
        Segment segment = new Segment();
        // TODO
        service.addIndexSegment(segment);
    }
}
