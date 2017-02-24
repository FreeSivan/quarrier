package sivan.yue.quarrier.search;

import sivan.yue.quarrier.search.segmentSearch.SegmentSearchTool;

/**
 * Created by xiwen.yxw on 2017/2/24.
 */
public class BitTableTest {

    public static void main(String[] args) {
        for (int i = 0; i < SegmentSearchTool.table.length; ++i) {
            System.out.println("" + i + " " + SegmentSearchTool.table[i]);
        }
    }
}
