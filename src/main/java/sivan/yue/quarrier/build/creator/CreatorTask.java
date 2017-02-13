package sivan.yue.quarrier.build.creator;

import sivan.yue.quarrier.build.merger.MergerBunch;
import sivan.yue.quarrier.data.Document;
import sivan.yue.quarrier.build.IMemoryTask;
import sivan.yue.quarrier.data.Segment;

import java.util.*;

/**
 * description:创建segment任务
 *
 * 该任务维护一个文档列表，并为列表中的文档创建倒排和正排
 * 该任务最终会创建一个segment文件，加入到Merger类对象中
 *
 * Created by xiwen.yxw on 2017/2/10.
 */
public class CreatorTask implements IMemoryTask {

    private List<Document> docList = new ArrayList<>();

    @Override
    public void run() {
        Segment segment = new Segment();
        for (Document doc : docList) {
            // 创建倒排索引
            buildIndex(segment, doc);
            // 创建正排索引
            buildPosit(segment, doc);
        }
        // 将任务添加到调度中心
        MergerBunch.INSTANCE.addItem(segment);
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
        return;
    }

    /**
     *
     * @param segment segment对象，存储倒排索引
     * @param doc 持有原始文件数据及数据信息的文档对象
     */
    private void buildIndex(Segment segment, Document doc) {
        byte[] data = doc.content;
        int count = 0;
        Map<Integer, List<Segment.IndexMeta>> tmpMap = new TreeMap<>();
        for (int i = 0; i < data.length; i+=4) {
            int value = getKey(data, i, i + 3);
            Segment.IndexMeta indexMeta = new Segment.IndexMeta();
            indexMeta.offset = count++;
            indexMeta.docId = doc.docId;
            if (tmpMap.containsKey(value)) {
                tmpMap.get(value).add(indexMeta);
            }
            else {
                List<Segment.IndexMeta> lst = new ArrayList<>();
                lst.add(indexMeta);
                tmpMap.put(value, lst);
            }
        }
        for (Map.Entry<Integer, List<Segment.IndexMeta>> entry: tmpMap.entrySet()) {
            Integer key = entry.getKey();
            List<Segment.IndexMeta> docIdList = entry.getValue();
            Segment.Index index = new Segment.Index();
            index.offset = segment.indexData.size();
            index.length = tmpMap.size();
            segment.index.put(key, index);
            for (Segment.IndexMeta indexMeta : docIdList) {
                segment.indexData.add(indexMeta);
            }
        }
    }

    private int getKey(byte[] data, int left, int right) {
        return 0;
    }

    public List<Document> getDocList() {
        return docList;
    }

    public void setDocList(List<Document> docList) {
        this.docList = docList;
    }
}
