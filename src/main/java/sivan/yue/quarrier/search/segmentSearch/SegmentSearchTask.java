package sivan.yue.quarrier.search.segmentSearch;

import sivan.yue.quarrier.common.data.Segment;
import sivan.yue.quarrier.search.SearchTask;
import java.util.concurrent.BlockingQueue;

/**
 * Created by xiwen.yxw on 2017/2/15.
 */
public class SegmentSearchTask extends SearchTask {

    private BlockingQueue<Integer> bq;

    private Segment segment;

    @Override
    public void run() {

    }

    public BlockingQueue<Integer> getBq() {
        return bq;
    }

    public void setBq(BlockingQueue<Integer> bq) {
        this.bq = bq;
    }

    public Segment getSegment() {
        return segment;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }
}
