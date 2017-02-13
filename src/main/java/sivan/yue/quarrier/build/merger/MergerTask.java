package sivan.yue.quarrier.build.merger;

import sivan.yue.quarrier.data.Segment;
import sivan.yue.quarrier.build.IMemoryTask;

import java.util.*;

/**
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

    private void buildIndex(Segment segment) {
        Queue<MergeMeta> priorityQueue = new PriorityQueue<MergeMeta>(20, MergeMeta.orderMeta);
        for (Segment seg : segments) {
            Iterator<Map.Entry<Integer, Segment.Index>> iterator = seg.index.entrySet().iterator();
            // 将所有seg的倒排索引表的第一个节点，创建对象，放入优先权队列
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
            MergeMeta mergeMeta = priorityQueue.poll();
            int key = mergeMeta.key;
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
            for (int i = mergeMeta.offset; i < mergeMeta.offset+mergeMeta.length; ++i) {
                segment.indexData.add(mergeMeta.indexData.get(i));
            }
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

    private void buildPosit(Segment segment, Segment seg) {
        for (Integer key : seg.posit.keySet()) {
            Segment.Posit posit = seg.posit.get(key);
            Segment.Posit newPosit = new Segment.Posit();
            newPosit.offset = segment.positData.size();
            newPosit.length = posit.offset;
            segment.posit.put(key, newPosit);
            for (int i = posit.offset; i < posit.length; ++i) {
                segment.positData.add(seg.positData.get(i));
            }
        }
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    private final static class MergeMeta {
        public int key;
        public int offset;
        public int length;
        public Iterator<Map.Entry<Integer, Segment.Index>> iterator;
        public Vector<Segment.IndexMeta> indexData;

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
