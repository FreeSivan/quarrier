package sivan.yue.quarrier.search.service.segment;

import sivan.yue.quarrier.data.Segment;
import sivan.yue.quarrier.search.service.ServiceTask;
import java.util.concurrent.BlockingQueue;

/**
 * Created by xiwen.yxw on 2017/2/15.
 */
public class SegmentServiceTask extends ServiceTask {

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
