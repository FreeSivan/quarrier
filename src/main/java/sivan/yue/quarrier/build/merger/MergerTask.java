package sivan.yue.quarrier.build.merger;

import sivan.yue.quarrier.data.Segment;
import sivan.yue.quarrier.build.IMemoryTask;

import java.util.*;

/**
 * description:合并segment任务
 * Created by xiwen.yxw on 2017/2/10.
 */
public class MergerTask implements IMemoryTask {

    private List<Segment> segments = new ArrayList<>();

    @Override
    public void run() {
        Segment segment = new Segment();
        for (Segment seg : segments) {
            buildPosit(segment, seg);
        }
        buildIndex(segment);
    }

    /**
     * description: 合并倒排索引函数
     * 将segments中的所有段的倒排乡音，合并到segment对象中
     * @param segment 保存合并后倒排索引的段
     */
    private void buildIndex(Segment segment) {
        // 创建优先权队列，用于多路归并时存放从每个segment中取出的数据
        Queue<MergeMeta> priorityQueue = new PriorityQueue<>(segments.size(), MergeMeta.orderMeta);
        for (Segment seg : segments) {
            // 从segment的倒排列表中取出最小的key值的倒排索引
            Iterator<Map.Entry<Integer, Segment.Index>> iterator = seg.index.entrySet().iterator();
            // 初始时用第一个key值的倒排索引创建MergeMeta对象，放入优先权队列
            if (iterator.hasNext()) {
                MergeMeta mergeMeta = new MergeMeta();
                Map.Entry<Integer, Segment.Index> entry = iterator.next();
                mergeMeta.key = entry.getKey();
                mergeMeta.offset = entry.getValue().offset;
                mergeMeta.length = entry.getValue().length;
                mergeMeta.iterator = iterator;
                mergeMeta.indexData = seg.indexData;
                priorityQueue.add(mergeMeta);
            }
        }
        while (!priorityQueue.isEmpty()) {
            // 从队列头部取出MergeMeta对象
            MergeMeta mergeMeta = priorityQueue.poll();
            int key = mergeMeta.key;
            // 判断key值是否已经存在于倒排表中
            // 如果存在只需要更新倒排项中的length
            // 如果不存在则创建新的倒排项
            if (segment.index.containsKey(key)) {
                Segment.Index index = segment.index.get(key);
                index.length += mergeMeta.length;
            }
            else {
                Segment.Index index = new Segment.Index();
                index.offset = segment.indexData.size();
                index.length = mergeMeta.length;
                segment.index.put(key, index);
            }
            // 将该MergeMeta对象对象维护的原始文件写入segment的原始文件vector
            for (int i = mergeMeta.offset; i < mergeMeta.offset+mergeMeta.length; ++i) {
                segment.indexData.add(mergeMeta.indexData.get(i));
            }
            // 如果当前处理key值之前所在的segment还有数据，则取下一个，创建对象写入优先权队列
            if (mergeMeta.iterator.hasNext()) {
                Map.Entry<Integer, Segment.Index> entry = mergeMeta.iterator.next();
                MergeMeta newMerge = new MergeMeta();
                newMerge.key = entry.getKey();
                newMerge.offset = entry.getValue().offset;
                newMerge.length = entry.getValue().length;
                newMerge.iterator = mergeMeta.iterator;
                newMerge.indexData = mergeMeta.indexData;
                priorityQueue.add(newMerge);
            }
        }
    }

    /**
     * description: 合并倒排索引函数
     * 将seg段中的正排数据写入segment段中
     * @param segment 待写入正排数据的段
     * @param seg segments中的段，被合并到segment段中
     */
    private void buildPosit(Segment segment, Segment seg) {
        for (Integer key : seg.posit.keySet()) {
            // 获取每个key值的正排索引项
            Segment.Posit posit = seg.posit.get(key);
            // 创建一个新的正排索引项
            Segment.Posit newPosit = new Segment.Posit();
            // 设置当前文件在原始文件列表中的偏移量
            newPosit.offset = segment.positData.size();
            // 设置当前文件的长度
            newPosit.length = posit.length;
            // 将正排放入正排索引表中
            segment.posit.put(key, newPosit);
            // 将seg持有的原始文件数据写入segment段中
            for (int i = posit.offset; i < posit.length; ++i) {
                segment.positData.add(seg.positData.get(i));
            }
        }
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    /**
     * 合并倒排索引时保存中间数据的结构，从segments列表中的段中提取并保存相关数据
     */
    private final static class MergeMeta {
        /**
         * 倒排索引的key值
         */
        public int key;
        /**
         * 倒排项在indexData中的偏移量
         */
        public int offset;
        /**
         * 倒排项的个数
         */
        public int length;
        /**
         * seg段的倒排索引表的迭代器，用于不断从seg中取出倒排索引信息
         */
        public Iterator<Map.Entry<Integer, Segment.Index>> iterator;
        /**
         * seg段中保存倒排项的vector
         */
        public Vector<Segment.IndexMeta> indexData;
        /**
         * 为了放入优先权队列，而实现的Comparator类
         */
        static Comparator<MergeMeta> orderMeta = new Comparator<MergeMeta>() {
            @Override
            public int compare(MergeMeta m1, MergeMeta m2) {
                if (m1.key < m2.key) {
                    return -1;
                }
                if (m1.key > m2.key) {
                    return 1;
                }
                return 0;
            }
        };
    }
}
