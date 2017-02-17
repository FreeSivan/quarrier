package sivan.yue.quarrier.search.segmentSearch;

import sivan.yue.quarrier.common.data.Segment;
import sivan.yue.quarrier.common.exception.FileFormatErrorException;
import sivan.yue.quarrier.search.SearchTask;
import sivan.yue.quarrier.search.Search;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Created by xiwen.yxw on 2017/2/15.
 */
public class SegmentSearch extends Search<Segment> {

    @Override
    public int singleSearch(byte[] rawData) {
        int ret = -1;
        for (Segment segment : segList) {
            ret = SegmentSearchTool.searchSegment(segment, rawData);
            if (ret >= 0) {
                break;
            }
        }
        return ret;
    }

    @Override
    protected SearchTask createTask(Segment seg, BlockingQueue<Integer> bq, byte[] rawData) {
        return new SegmentSearchTask(bq, rawData, seg);
    }
}
