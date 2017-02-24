package sivan.yue.quarrier.load.segmentLoad;

import sivan.yue.quarrier.common.data.Segment;
import sivan.yue.quarrier.common.tools.RWIndexPositFile;
import sivan.yue.quarrier.load.LoadTask;
import sivan.yue.quarrier.search.segmentSearch.SegmentSearch;

/**
 * Created by xiwen.yxw on 2017/2/15.
 */
public class SegmentLoadTask extends LoadTask {

    private SegmentSearch service;

    private String path;

    private Integer index;

    public SegmentLoadTask(SegmentSearch service, String path, Integer index) {
        this.service = service;
        this.path = path;
        this.index = index;
    }

    @Override
    public void run() {
        Segment segment = new Segment();
        String indexName = path + index + ".in";
        RWIndexPositFile.loadIndexFile(indexName, segment);
        String iValueName = path + index + ".iv";
        RWIndexPositFile.loadIValueFile(iValueName, segment);
        String positName = path + index + ".po";
        RWIndexPositFile.loadPositFile(positName, segment);
        String pValueName = path + index + ".pv";
        RWIndexPositFile.loadPValueName(pValueName, segment);
        service.addSubIndex(segment);

    }

}
