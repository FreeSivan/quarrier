package sivan.yue.quarrier.build.creator;

import sivan.yue.quarrier.build.BuildTask;
import sivan.yue.quarrier.build.merger.MergerBunch;
import sivan.yue.quarrier.common.data.Document;
import sivan.yue.quarrier.common.data.Segment;

import java.util.*;

/**
 * description:创建segment任务
 *
 * 该任务维护一个文档列表，并为列表中的文档创建倒排和正排
 * 该任务最终会创建一个segment文件，加入到Merger类对象中
 *
 * Created by xiwen.yxw on 2017/2/10.
 */
public class CreatorTask extends BuildTask {

    private MergerBunch mergerBunch;

    private List<Document> docList = new ArrayList<>();

    private static boolean isFlush;

    private static int count = 0;

    @Override
    public void run() {
        Segment segment = new Segment();
        // 创建倒排索引
        buildIndex(segment, docList);
        for (Document doc : docList) {
            // 创建正排索引
            buildPosit(segment, doc);
        }
        // 将segment添加到merger中
        mergerBunch.addItem(segment);
        delCount();
        if (isIsFlush() && getCount() == 0) {
            mergerBunch.flush();
            setIsFlush(false);
        }
    }

    /**
     * description: 创建正排索引函数
     * 将doc对象中的content和docid提取出来，按照一定的方式
     * 组织到传入到该函数中的segment对象中
     * @param segment segment对象，存储正排索引
     * @param doc 持有原始文件数据及数据信息的文档对象
     */
    private void buildPosit(Segment segment, Document doc) {
        byte[] data = doc.content;
        int offset = segment.positData.size();
        // 将文件内容写入segment的原始数据容器positData
        for (byte item : data) {
            segment.positData.add(item);
        }
        // 创建正排索引
        Segment.Posit posit = new Segment.Posit();
        // 设置文件数据原始原始数据容器positData的偏移量
        posit.offset = offset;
        // 设置原始文件的主要标识
        posit.orgId = doc.orgId;
        // 设置原始文件的长度
        posit.length = doc.content.length;
        // 将正排结构存入正排索引表
        segment.posit.put(doc.docId, posit);
    }

    /**
     *description: 创建倒排索引函数
     *
     * @param segment segment对象，存储倒排索引
     * @param docList 持有原始文件数据及数据信息的文档对象
     */
    private void buildIndex(Segment segment, List<Document> docList) {
        // 临时的倒排表，treeMap存储key值，方便key值的排序
        Map<Integer, List<Segment.IndexMeta>> tmpMap = new TreeMap<>();
        for (Document doc : docList) {
            byte[] data = doc.content;
            // 以4个byte为一组作为一个key值
            for (int i = 0; i < data.length; i += 4) {
                // 构造key值
                int value = getKey(data, i);
                // 只对奇数帧建库
                if (value == -1) {
                    continue;
                }
                // 创建倒排索引结构
                Segment.IndexMeta indexMeta = new Segment.IndexMeta();
                // 保存key值在文档中的byte偏移量
                indexMeta.offset = i;
                // 保存文档的id
                indexMeta.docId = doc.docId;
                //if (value == 0) {
                //    System.out.println("docid1111 = " + doc.orgId + " offset1111 = " + indexMeta.offset);
                //}
                // 将key值及对应的倒排项加入临时倒排表
                if (tmpMap.containsKey(value)) {
                    tmpMap.get(value).add(indexMeta);
                } else {
                    List<Segment.IndexMeta> lst = new ArrayList<>();
                    lst.add(indexMeta);
                    tmpMap.put(value, lst);
                }
                // 我也不知道下面的代码是要干嘛,指纹提取的同学要求索引这个key
                if (i + 32 * 4 < data.length) {
                    int value2 = getKey2(data, value, i + 32 * 4);
                    //if (value2 == 0) {
                    //    int v1 = value;
                    //    int v2 = getKey(data, i+32*4);
                    //    System.out.println("docid = " + doc.orgId + " offset = " + indexMeta.offset + " v1 = "+v1 + " offset2 = " + (i+32*4) + " v2" + v2);
                    //}
                    if (tmpMap.containsKey(value2)) {
                        tmpMap.get(value2).add(indexMeta);
                    } else {
                        List<Segment.IndexMeta> lst = new ArrayList<>();
                        lst.add(indexMeta);
                        tmpMap.put(value2, lst);
                    }
                }
            }
        }
        // 将临时倒排结构写入到segment结构的倒排表中
        for (Map.Entry<Integer, List<Segment.IndexMeta>> entry: tmpMap.entrySet()) {
            // key值及key值对应的倒排项
            Integer key = entry.getKey();
            List<Segment.IndexMeta> docIdList = entry.getValue();
            // 创建新的倒排索引，持有key值及倒排项的位置信息
            Segment.Index index = new Segment.Index();
            index.offset = segment.indexData.size();
            index.length = docIdList.size();
            segment.index.put(key, index);
            // 倒排项写入segment的倒排项数组中
            for (Segment.IndexMeta indexMeta : docIdList) {
                segment.indexData.add(indexMeta);
            }
        }
    }

    private int getKey(byte[] data, int offset) {
        int value = (int) ((data[offset] & 0xFF) |
                ((data[offset + 1] & 0xFF) << 8) |
                ((data[offset + 2] & 0xFF) << 16)|
                ((data[offset + 3] & 0xFF) << 24));
        if ((value & 1) == 0) {
            return -1;
        }
        return (value >> 1) & 0x7FFFFFFF;
    }

    private int getKey2(byte[] data, int key, int offset) {
        int value = (int)((data[offset] & 0xFF) |
                ((data[offset + 1] & 0xFF) << 8) |
                ((data[offset + 2] & 0xFF) << 16)|
                ((data[offset + 3] & 0xFF) << 24));
        return  ((value >> 1) & 0x7FFF8000) | ((key) & 0x7FFF);
    }

    public List<Document> getDocList() {
        return docList;
    }

    public void setDocList(List<Document> docList) {
        this.docList = docList;
    }

    public void setMergerBunch(MergerBunch mergerBunch) {
        this.mergerBunch = mergerBunch;
    }


    public synchronized static void addCount() {
        count++;
    }

    public synchronized static void delCount() {
        count--;
    }

    public synchronized static int getCount() {
        return count;
    }

    public synchronized static void setCount(int count) {
        CreatorTask.count = count;
    }

    public synchronized static boolean isIsFlush() {
        return isFlush;
    }

    public synchronized static void setIsFlush(boolean isFlush) {
        CreatorTask.isFlush = isFlush;
    }
}
