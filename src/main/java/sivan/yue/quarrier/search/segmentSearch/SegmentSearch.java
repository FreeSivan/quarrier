package sivan.yue.quarrier.search.segmentSearch;

import sivan.yue.quarrier.common.data.Segment;
import sivan.yue.quarrier.search.SearchTask;
import sivan.yue.quarrier.search.Search;

import java.util.concurrent.BlockingQueue;

/**
 * Created by xiwen.yxw on 2017/2/15.
 */
public class SegmentSearch extends Search<Segment> {

    @Override
    public int singleSearch(byte[] rawData) {
        return 0;
    }

    @Override
    protected SearchTask createTask(Segment seg, BlockingQueue<Integer> bq) {
        SegmentSearchTask serviceTask = new SegmentSearchTask();
        serviceTask.setBq(bq);
        serviceTask.setSegment(seg);
        serviceTask.overFlag = false;
        return serviceTask;
    }
}
