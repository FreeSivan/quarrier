package sivan.yue.quarrier.load.segment;

import sivan.yue.quarrier.ITask;
import sivan.yue.quarrier.data.Segment;
import sivan.yue.quarrier.search.segment.SegmentSearch;

/**
 * Created by xiwen.yxw on 2017/2/15.
 */
public class SegmentLoadTask implements ITask{

    private SegmentSearch service;

    public SegmentLoadTask(SegmentSearch service) {
        this.service = service;
    }

    @Override
    public void run() {
        Segment segment = new Segment();
        // TODO
        service.addIndexSegment(segment);
    }
}
