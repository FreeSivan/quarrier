package sivan.yue.quarrier.search;

import sivan.yue.quarrier.load.segmentLoad.SegmentLoad;
import sivan.yue.quarrier.search.segmentSearch.SegmentSearch;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by xiwen.yxw on 2017/2/27.
 */
public class SearchTest {
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

        System.out.println("load success!");
        try {
            RandomAccessFile realFile = new RandomAccessFile("D:\\work\\data\\afp\\afp\\3425242.afp", "r");
            for (int i = 0; i < realFile.length() - 768; i += 768) {
                byte[] content = new byte[768];
                realFile.seek(i);
                realFile.read(content, 0, 768);
                int ret = segmentSearch.multiSearch(content);
                System.out.println("ret = " + ret);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
