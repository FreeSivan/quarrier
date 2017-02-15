package sivan.yue.quarrier.build.writer;

import sivan.yue.quarrier.build.BuildTask;
import sivan.yue.quarrier.data.Segment;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
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
        writeIndexFile(indexName, seg.index);
        String iValueName = indexDir + index + ".iv";
        writeIValueFile(iValueName, seg.indexData);
        String positName = indexDir + index + ".po";
        writePositFile(positName, seg.posit);
        String pValueName = indexDir + index + ".pv";
        writePValueName(pValueName, seg.positData);
    }

    /**
     *
     * @param pValueName
     * @param positData
     */
    private void writePValueName(String pValueName, Vector<Byte> positData) {
        try {
            DataOutputStream out=new DataOutputStream(new FileOutputStream(pValueName));
            byte[] bytes = new byte[positData.size()];
            // TODO 这地方太low了，后面要实现一个动态的byte数组来取代vector
            for (int i = 0; i < positData.size(); ++i) {
                bytes[i] = positData.get(i);
            }
            out.write(bytes, 0, bytes.length);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param positName
     * @param posit
     */
    private void writePositFile(String positName, Map<Integer, Segment.Posit> posit) {
        try {
            DataOutputStream out=new DataOutputStream(new FileOutputStream(positName));
            for (Map.Entry<Integer, Segment.Posit> entry : posit.entrySet()) {
                Integer key = entry.getKey();
                Segment.Posit positValue = entry.getValue();
                out.writeInt(key);
                out.writeInt(positValue.length);
                out.writeInt(positValue.offset);
                out.writeInt(positValue.orgId);
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param iValueName
     * @param indexData
     */
    private void writeIValueFile(String iValueName, Vector<Segment.IndexMeta> indexData) {
        try {
            DataOutputStream out=new DataOutputStream(new FileOutputStream(iValueName));
            for (Segment.IndexMeta indexMeta : indexData) {
                out.writeInt(indexMeta.docId);
                out.writeInt(indexMeta.offset);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param indexName
     * @param index
     */
    private void writeIndexFile(String indexName, Map<Integer, Segment.Index> index) {
        try {
            DataOutputStream out=new DataOutputStream(new FileOutputStream(indexName));
            for (Map.Entry<Integer, Segment.Index> entry : index.entrySet()) {
                Integer key = entry.getKey();
                Segment.Index inx = entry.getValue();
                out.writeInt(key);
                out.writeInt(inx.length);
                out.writeInt(inx.offset);
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized static int getCount() {
        return count.getAndIncrement();
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

}
