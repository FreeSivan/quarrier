package sivan.yue.quarrier.load.segmentLoad;

import sivan.yue.quarrier.common.data.Segment;
import sivan.yue.quarrier.common.schedule.ScheduleCenter;
import sivan.yue.quarrier.common.tools.RWIndexPositFile;
import sivan.yue.quarrier.common.tools.ThreadMutexFile;
import sivan.yue.quarrier.load.Load;
import sivan.yue.quarrier.search.segmentSearch.SegmentSearch;

import java.io.*;
import java.util.List;


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
        String fName = path + "segment";
        try {
            ThreadMutexFile file = new ThreadMutexFile(fName, "r");
            List<Integer> indexLst = file.readIntList();
            file.close();
            for (Integer index : indexLst) {
                loadSegment(path, index);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSegment(String path, Integer index) {
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

    @Override
    protected void createTask(String path, int index) {
        SegmentLoadTask loadTask = new SegmentLoadTask(service, path, index);
        ScheduleCenter.INSTANCE.addTask(loadTask);
    }
}
