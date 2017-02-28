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
            RandomAccessFile realFile = new RandomAccessFile("D:\\work\\data\\afp\\2000000001.afp", "r");
            for (int i = 0; i < realFile.length() - 768; i += 768) {
                byte[] content = new byte[768];
                realFile.seek(i);
                realFile.read(content, 0, 768);
                int ret = segmentSearch.multiSearch(content);
                System.out.println("ret = " + ret);
            }
            realFile.close();
            System.out.println("===========================================");
            RandomAccessFile realFile1 = new RandomAccessFile("D:\\work\\data\\afp\\1027712.afp", "r");
            for (int i = 0; i < realFile1.length() - 768; i += 768) {
                byte[] content = new byte[768];
                realFile1.seek(i);
                realFile1.read(content, 0, 768);
                int ret = segmentSearch.multiSearch(content);
                System.out.println("ret = " + ret);
            }
            realFile1.close();
            System.out.println("===========================================");
            RandomAccessFile realFile2 = new RandomAccessFile("D:\\work\\data\\afp\\3598439.afp", "r");
            for (int i = 0; i < realFile2.length() - 768; i += 768) {
                byte[] content = new byte[768];
                realFile2.seek(i);
                realFile2.read(content, 0, 768);
                int ret = segmentSearch.multiSearch(content);
                System.out.println("ret = " + ret);
            }
            realFile2.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
