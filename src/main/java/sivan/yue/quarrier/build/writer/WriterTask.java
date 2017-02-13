package sivan.yue.quarrier.build.writer;

import sivan.yue.quarrier.data.Segment;
import sivan.yue.quarrier.build.IMemoryTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiwen.yxw on 2017/2/10.
 */
public class WriterTask implements IMemoryTask {

    private List<Segment> segments = new ArrayList<>();

    @Override
    public void run() {
        // TODO 将segment逐个写入磁盘的逻辑
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }
}
