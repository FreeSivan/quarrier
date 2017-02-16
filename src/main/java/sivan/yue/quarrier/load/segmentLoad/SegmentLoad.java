package sivan.yue.quarrier.load.segmentLoad;

import sivan.yue.quarrier.common.data.Segment;
import sivan.yue.quarrier.load.Load;
import sivan.yue.quarrier.search.segmentSearch.SegmentSearch;

/**
 * Created by xiwen.yxw on 2017/2/15.
 */
public class SegmentLoad extends Load {

    private SegmentSearch service;

    public SegmentLoad(SegmentSearch service) {
        this.service = service;
    }


    @Override
    public void singleLoad(String path) {
        // TODO 迭代每一个
        Segment segment = new Segment();
        // TODO 创建segment的工作
        service.addSubIndex(segment);
    }

    @Override
    protected void createTask(int index) {

    }
}
