package sivan.yue.quarrier.search.segmentSearch;

import sivan.yue.quarrier.common.data.Segment;
import sivan.yue.quarrier.search.SearchTask;
import java.util.concurrent.BlockingQueue;

/**
 * Created by xiwen.yxw on 2017/2/15.
 */
public class SegmentSearchTask extends SearchTask {

    private Segment segment;

    public SegmentSearchTask(BlockingQueue<Integer> bq, byte[] rawData, Segment segment) {
        super(bq, rawData);
        this.segment = segment;
    }

    @Override
    public void run() {
        int ret = SegmentSearchTool.searchSegment(segment, rawData);
        bq.add(ret);
    }

}
