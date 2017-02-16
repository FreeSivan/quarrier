package sivan.yue.quarrier.build.writer;

import sivan.yue.quarrier.build.BuildTask;
import sivan.yue.quarrier.common.data.Segment;
import sivan.yue.quarrier.common.tools.ProcessMutexFile;
import sivan.yue.quarrier.common.tools.RWIndexPositFile;
import sivan.yue.quarrier.common.tools.ThreadMutexFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xiwen.yxw on 2017/2/10.
 */
public class WriterTask extends BuildTask {

    private List<Segment> segments = new ArrayList<>();

    private static AtomicInteger count = new AtomicInteger(0);

    public static String indexDir = "";

    @Override
    public void run() {
        for (Segment seg : segments) {
            writeSegToFile(seg);
        }
    }

    private void writeSegToFile(Segment seg) {
        // 四个文件 .in .iv .po .pv
        int index = getCount();
        String indexName = indexDir + index + ".in";
        RWIndexPositFile.writeIndexFile(indexName, seg);
        String iValueName = indexDir + index + ".iv";
        RWIndexPositFile.writeIValueFile(iValueName, seg);
        String positName = indexDir + index + ".po";
        RWIndexPositFile.writePositFile(positName, seg);
        String pValueName = indexDir + index + ".pv";
        RWIndexPositFile.writePValueName(pValueName, seg);
        String segmentName = indexDir + "segment";
        try {
            ThreadMutexFile file = new ThreadMutexFile(segmentName, "rw");
            file.writeInt(index);
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized static int getCount() {
        return count.getAndIncrement();
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

}
