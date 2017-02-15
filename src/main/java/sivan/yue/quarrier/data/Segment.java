package sivan.yue.quarrier.data;

import java.util.*;

/**
 * description
 * :
 * 一个段在内存中由一个倒排索引结构和一个正排索引结构组成
 * 倒排索引结构：
 *      倒排键值标引表由一个treeMap结构来组织，之所以用treeMap
 *      而不用hashMap是为了通过键值进行排序
 *      倒排项用一个IndexMeta的vector结构来存储
 * 正排索引结构：
 *      正排表的键值同样由一个treeMap结构来阻止，同样也是为了排序
 *      所有文档的原始数据存放在一个byte的vector结构中
 *
 * Created by xiwen.yxw on 2017/2/11.
 */
public class Segment {

    /**
     * 建库时的倒排索引表，索引key到倒排项，需要对key值排序
     */
    public Map<Integer, Index> index = new TreeMap<>();

    /**
     * 搜索时的倒排索引表，不需要对key值排序
     */
    public Map<Integer, Index> indexRun = new HashMap<>();

    /**
     * 倒排项数据存放数组
     */
    public Vector<IndexMeta> indexData = new Vector<>();

    /**
     * 正排索引表， 索引docId到doc的原始数据存放位置
     */
    public Map<Integer, Posit> posit = new HashMap<>();

    /**
     * 正排原始数据存放数组
     */
    public Vector<Byte> positData = new Vector<>();

    /**
     * 记录某个key的到排项在倒排表中的位置和长度
     */
    public final static class Index {
        /**
         * 对应IndexMeta在倒排表中的偏移量
         */
        public int offset;
        /**
         * 对应IndexMeta的个数
         */
        public int length;
    }

    /**
     * 倒排项记录的信息
     */
    public final static class IndexMeta {
        /**
         * 该倒排项指定的文档ID
         */
        public int docId;
        /**
         * key值在文档中的偏移量
         */
        public int offset;
    }

    /**
     * 正排项记录的信息
     */
    public final static class Posit {
        /**
         * 原始文档的主要标识
         */
        public int orgId;
        /**
         * 原始文档在正排表中的偏移量
         */
        public int offset;
        /**
         * 原始文件的长度
         */
        public int length;
    }
}
