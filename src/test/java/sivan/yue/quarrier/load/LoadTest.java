package sivan.yue.quarrier.load;

import sivan.yue.quarrier.common.data.Segment;
import sivan.yue.quarrier.load.segmentLoad.SegmentLoad;
import sivan.yue.quarrier.search.segmentSearch.SegmentSearch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Created by xiwen.yxw on 2017/2/23.
 */
public class LoadTest {
    public static void main(String[] args) {
        SegmentSearch segmentSearch = new SegmentSearch();
        SegmentLoad segmentLoad = new SegmentLoad(segmentSearch);
        segmentLoad.multiLoad("D:\\work\\data\\afp\\index\\");

        while (segmentSearch.getSegList().size() != 3) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int i = 0;
        for (Segment segment :segmentSearch.getSegList()) {
            File file = new File("D:\\work\\data\\afp\\searchBuild"+i+".txt");
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                for (Map.Entry<Integer, Segment.Index> entry : segment.indexRun.entrySet()) {
                    writer.write("key = " + entry.getKey() + " count = " + entry.getValue().length + " offset = " + entry.getValue().offset);
                    writer.newLine();
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            i++;
            System.out.println("segment.index.size" + segment.indexRun.size());
        }

        i = 0;
        for (Segment segment :segmentSearch.getSegList()) {
            File file1 = new File("D:\\work\\data\\afp\\SearchPosit" + i + ".txt");
            i++;
            System.out.println("fileName = " + file1.getName());
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file1));
                for (Map.Entry<Integer, Segment.Posit> entry : segment.posit.entrySet()) {
                    writer.write("docId = " + entry.getKey() + " orgId = " + entry.getValue().orgId + " offset = " + entry.getValue().offset + " length = " + entry.getValue().length);
                    writer.newLine();
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("segment.posit.size " + segment.posit.size());
        }
    }
}
