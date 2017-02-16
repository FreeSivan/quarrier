package sivan.yue.quarrier.load.segmentLoad;

import sivan.yue.quarrier.common.data.Segment;
import sivan.yue.quarrier.load.LoadTask;
import sivan.yue.quarrier.search.segmentSearch.SegmentSearch;

/**
 * Created by xiwen.yxw on 2017/2/15.
 */
public class SegmentLoadTask extends LoadTask {

    private SegmentSearch service;

    public SegmentLoadTask(SegmentSearch service) {
        this.service = service;
    }

    @Override
    public void run() {
        Segment segment = new Segment();
        // TODO
        service.addSubIndex(segment);
    }
}
