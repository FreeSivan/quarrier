package sivan.yue.quarrier.search.segmentSearch;

import sivan.yue.quarrier.common.data.Segment;
import sivan.yue.quarrier.common.exception.FileFormatErrorException;
import sivan.yue.quarrier.search.SearchTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

/**
 * Created by xiwen.yxw on 2017/2/15.
 */
public class SegmentSearchTask extends SearchTask {

    private Segment segment;

    public SegmentSearchTask(BlockingQueue<Integer> bq, byte[] rawData, Segment segment) {
        super(bq, rawData);
        this.segment = segment;
    }

    @Override
    public void run() {
        int ret = searchSegment(segment, rawData);
        bq.add(ret);
    }

    public static byte[] table = new byte[65536];

    static {
        for (int i = 0; i < table.length; ++i) {
            table[i] = countBitOne(i);
        }
    }

    private static byte countBitOne(int i) {
        byte n = 0;
        for (n = 0; i > 0; n++) {
            i &=(i - 1);
        }
        return n;
    }

    /**
     * description : 从一个segment中检索rawData的操作
     *
     * @param segment 待检索的segment
     * @param rawData 待检索的rawData
     * @return 查询成功返回文档原始id，否则返回-1。
     */
    public int searchSegment(Segment segment, byte[] rawData) {
        if ((rawData.length % 4) != 0) {
            throw new FileFormatErrorException("rawData format error!");
        }
        Vector<KeyMeta> keyList = new Vector<>();
        for (int i = 0; i < rawData.length; i+=4) {
            // 获取key值，4个byte对应一个key
            int key = getKeyFromRawData(rawData, i);
            KeyMeta keyMeta = new KeyMeta(key, i);
            keyList.add(keyMeta);
            int ret = searchSingleKey(segment, key, rawData, i);
            if (ret != -1) {
                return ret;
            }
        }
        Vector<Integer> pow = new Vector<>();
        for (int i = 0; i < 31; ++i) {
            pow.add(1 << i);
        }
        Vector<KeyMeta> keyList1 = getFaultTolerant1(keyList, pow);
        if (keyList1 == null) {
            return -1;
        }
        for (KeyMeta keyMeta : keyList1) {
            int ret = searchSingleKey(segment, keyMeta.key, rawData, keyMeta.offset);
            if (ret != -1) {
                return ret;
            }
        }
        keyList1 = getFaultTolerant2(keyList, pow);
        if (keyList1 == null) {
            return -1;
        }
        for (KeyMeta keyMeta : keyList1) {
            int ret = searchSingleKey(segment, keyMeta.key, rawData, keyMeta.offset);
            if (ret != -1) {
                return ret;
            }
        }
        keyList1 = getFaultTolerant3(keyList, pow);
        if (keyList1 == null) {
            return -1;
        }
        for (KeyMeta keyMeta : keyList1) {
            int ret = searchSingleKey(segment, keyMeta.key, rawData, keyMeta.offset);
            if (ret != -1) {
                return ret;
            }
        }
        return -1;
    }

    private Vector<KeyMeta> getFaultTolerant1(Vector<KeyMeta> keyList, Vector<Integer> pow) {
        Vector<KeyMeta> metaLst = new Vector<>();
        for (KeyMeta keyMeta : keyList) {
            if (overFlag) {
                return null;
            }
            for (int i = pow.size()-1; i >= 0; --i) {
                int key = keyMeta.key ^ pow.get(i);
                KeyMeta keyMeta1 = new KeyMeta(key, keyMeta.offset);
                metaLst.add(keyMeta1);
            }
        }
        return metaLst;
    }

    private Vector<KeyMeta> getFaultTolerant2(Vector<KeyMeta> keyList, Vector<Integer> pow) {
        Vector<KeyMeta> metaLst = new Vector<>();
        for (KeyMeta keyMeta : keyList) {
            if (overFlag) {
                return null;
            }
            for (int i = pow.size()-1; i >= 0; --i) {
                for (int j = i - 1; j >= 0; --j) {
                    int key = keyMeta.key ^ pow.get(i) ^ pow.get(j);
                    KeyMeta keyMeta1 = new KeyMeta(key, keyMeta.offset);
                    metaLst.add(keyMeta1);
                }
            }
        }
        return metaLst;
    }

    private Vector<KeyMeta> getFaultTolerant3(Vector<KeyMeta> keyList, Vector<Integer> pow) {
        Vector<KeyMeta> metaLst = new Vector<>();
        for (KeyMeta keyMeta : keyList) {
            if (overFlag) {
                return null;
            }
            for (int i = pow.size()-1; i >= 0; --i) {
                for (int j = i - 1; j >= 0; --j) {
                    for (int k = j - 1; k >= 0; --k) {
                        int key = keyMeta.key ^ pow.get(i) ^ pow.get(j) ^ pow.get(k);
                        KeyMeta keyMeta1 = new KeyMeta(key, keyMeta.offset);
                        metaLst.add(keyMeta1);
                    }
                }
            }
        }
        return metaLst;
    }

    private int searchSingleKey(Segment segment, int key, byte[] rawData, int offset) {
        Segment.Index index = segment.indexRun.get(key);
        if (index == null) {
            return -1;
        }
        // 迭代key值对应的倒排表中的每个倒排项目
        for (int j = index.offset; j < index.offset+index.length; ++j) {
            if (overFlag) {
                return -1;
            }
            // 取出倒排项目
            Segment.IndexMeta indexMeta = segment.indexData.get(j);
            if (indexMeta.offset < offset) {
                continue;
            }
            // 获取倒排项对应的正排索引
            Segment.Posit posit = segment.posit.get(indexMeta.docId);
            // 计算原始二进制流在raw文件中的起始位置
            int begin = posit.offset + indexMeta.offset - offset;
            // 计算原始二进制流在raw文件中的结束位置
            int end = begin + rawData.length;
            // 如果二进制流对应的结束位置大于文件再raw文件中的结束位置
            if (end > posit.offset + posit.length) {
                continue;
            }
            // 如果匹配成功则返回文档原始的id
            if (contentMatch(rawData, segment.positData, begin)) {
                return posit.orgId;
            }
        }
        return -1;
    }


    /**
     * description :判断元是二进制和指纹库中检索出来的指纹段是否相似
     *
     * @param rawData 原始二进制串
     * @param content 指纹库中检索出来的数据段
     * @param begin content中的起始位置
     * @return true ： 相似； false ：不相似
     */
    private boolean contentMatch(byte[] rawData, List<Byte> content, int begin) {
        int count = 0;
        for (int i = 0; i < rawData.length; i += 2) {
            int key1 = getMetaKeyFromBuffer(rawData, i);
            int key2 = getMetaKeyFromBuffer(content, begin + i);
            if (-1 == key2) {
                continue;
            }
            count += table[key1 ^ key2];
        }
        if (count < rawData.length * 8 * 0.37) {
            return true;
        }
        return false;
    }

    /**
     * description : 从rawData的offset位置获取一个索引key值
     *
     * @param rawData 待检索的二进制流
     * @param offset 二进制流的偏移地址
     * @return 待检索的key值
     */
    private int getKeyFromRawData(byte[] rawData, int offset) {
        int value = (int) ((rawData[offset] & 0xFF) |
                ((rawData[offset + 1] & 0xFF) << 8) |
                ((rawData[offset + 2] & 0xFF) << 16)|
                ((rawData[offset + 3] & 0xFF) << 24));
        return (value >> 1) & 0x7FFFFFFF;
    }

    private int getMetaKeyFromBuffer(byte[] buffer, int offset) {
        int value = (buffer[offset] & 0xFF)|((buffer[offset+1] & 0xFF) << 8);
        return value;
    }

    private int getMetaKeyFromBuffer(List<Byte> buffer, int offset) {
        int value = (buffer.get(offset) & 0xFF) | ((buffer.get(offset + 1) & 0xFF) << 8);
        return value;
    }

    private static class KeyMeta {
        public int key;
        public int offset;

        public KeyMeta(int key, int offset) {
            this.key = key;
            this.offset = offset;
        }
    }

}
