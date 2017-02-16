package sivan.yue.quarrier.common.tools;

import sivan.yue.quarrier.common.data.Segment;

import java.io.*;
import java.util.Map;
import java.util.Vector;

/**
 * Created by xiwen.yxw on 2017/2/16.
 */
public class RWIndexPositFile {

    /**
     *
     * @param indexName
     * @param segment
     */
    public static void writeIndexFile(String indexName, Segment segment) {
        Map<Integer, Segment.Index> index = segment.index;
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

    /**
     *
     * @param indexFileName
     * @param segment
     */
    public static void loadIndexFile(String indexFileName, Segment segment) {
        Map<Integer, Segment.Index> index = segment.indexRun;
        try {
            RandomAccessFile realFile = new RandomAccessFile(indexFileName, "r");
            long length = realFile.length();
            long count = 0;
            while (count < length) {
                // 读键值
                Segment.Index indexMeta = new Segment.Index();
                int key = realFile.readInt();
                count += 4;
                indexMeta.length = realFile.readInt();
                count += 4;
                indexMeta.offset = realFile.readInt();
                count += 4;
                index.put(key, indexMeta);
            }
            realFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param iValueName
     * @param segment
     */
    public static void writeIValueFile(String iValueName, Segment segment) {
        Vector<Segment.IndexMeta> indexData = segment.indexData;
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
     * @param indexVFileName
     * @param segment
     */
    public static void loadIValueFile(String indexVFileName, Segment segment) {
        Vector<Segment.IndexMeta> indexData = segment.indexData;
        try {
            RandomAccessFile realFile = new RandomAccessFile(indexVFileName, "r");
            long length = realFile.length();
            long count = 0;
            while (count < length) {
                Segment.IndexMeta indexMeta = new Segment.IndexMeta();
                indexMeta.docId = realFile.readInt();
                count += 4;
                indexMeta.offset = realFile.readInt();
                count += 4;
                indexData.add(indexMeta);
            }
            realFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param positName
     * @param segment
     */
    public static void writePositFile(String positName, Segment segment) {
        Map<Integer, Segment.Posit> posit = segment.posit;
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
     * @param positFileName
     * @param segment
     */
    public static void loadPositFile(String positFileName, Segment segment) {
        Map<Integer, Segment.Posit> posit = segment.posit;
        try {
            RandomAccessFile realFile = new RandomAccessFile(positFileName, "r");
            long length = realFile.length();
            long count = 0;
            while (count < length) {
                Segment.Posit positValue = new Segment.Posit();
                int key = realFile.readInt();
                count += 4;
                positValue.length = realFile.readInt();
                count += 4;
                positValue.offset = realFile.readInt();
                count += 4;
                positValue.orgId = realFile.readInt();
                count += 4;
                posit.put(key, positValue);
            }
            realFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param pValueName
     * @param segment
     */
    public static void writePValueName(String pValueName, Segment segment) {
        Vector<Byte> positData = segment.positData;
        try {
            DataOutputStream out=new DataOutputStream(new FileOutputStream(pValueName));
            byte[] bytes = new byte[positData.size()];
            // TODO 这地方太low了
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
     * @param pValueName
     * @param segment
     */
    public static void loadPValueName(String pValueName, Segment segment) {
        Vector<Byte> positData = segment.positData;
    }
}
