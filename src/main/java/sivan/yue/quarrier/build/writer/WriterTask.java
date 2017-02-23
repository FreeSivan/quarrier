package sivan.yue.quarrier.build.writer;

import sivan.yue.quarrier.build.BuildTask;
import sivan.yue.quarrier.common.data.Segment;
import sivan.yue.quarrier.common.tools.RWIndexPositFile;
import sivan.yue.quarrier.common.tools.ThreadMutexFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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

        /*
        File file = new File("D:\\work\\data\\afp\\BuildIndex"+index+".txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (Map.Entry<Integer, Segment.Index> entry : seg.index.entrySet()) {
                writer.write("key = " + entry.getKey() + " count = " + entry.getValue().length + " offset = " + entry.getValue().offset);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("segment.index.size " + seg.index.size());

        File file1 = new File("D:\\work\\data\\afp\\BuildPosit"+index+".txt");
        System.out.println("fileName = " + file.getName());
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file1));
            for (Map.Entry<Integer, Segment.Posit> entry : seg.posit.entrySet()) {
                writer.write("docId = " + entry.getKey() + " orgId = " + entry.getValue().orgId + " offset = " + entry.getValue().offset + " length = " + entry.getValue().length);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("segment.posit.size " + seg.posit.size());
        */
    }

    private synchronized static int getCount() {
        return count.getAndIncrement();
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

}
