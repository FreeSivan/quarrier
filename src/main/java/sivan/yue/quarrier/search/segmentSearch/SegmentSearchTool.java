package sivan.yue.quarrier.search.segmentSearch;

import sivan.yue.quarrier.common.data.Segment;
import sivan.yue.quarrier.common.exception.FileFormatErrorException;

import java.util.List;

/**
 * Created by xiwen.yxw on 2017/2/17.
 */
public class SegmentSearchTool {

    public static byte[] table = new byte[65536];

    static {
        for (int i = 0; i < table.length; ++i) {
            table[i] = countBitOne(i);
        }
    }

    private static byte countBitOne(int i) {
        // TODO
        return 0;
    }

    /**
     * description: 从一个segment中检索rawData的操作
     *
     * @param segment 待检索的segment
     * @param rawData 待检索的rawData
     * @return 查询成功返回文档原始id，否则返回-1。
     */
    public static int searchSegment(Segment segment, byte[] rawData) {
        if ((rawData.length % 4) != 0) {
            throw new FileFormatErrorException("rawData format error!");
        }
        for (int i = 0; i < rawData.length; i+=4) {
            int key = getKeyFromRawData(rawData, i);
            Segment.Index index = segment.indexRun.get(key);
            for (int j = index.offset; j < index.offset+index.length; ++j) {
                Segment.IndexMeta indexMeta = segment.indexData.get(j);
                int fileOffset = indexMeta.offset;
                int docId = indexMeta.docId;
                Segment.Posit posit = segment.posit.get(docId);
                int orgId = posit.orgId;
                int rawOffset = posit.offset;
                int rawLength = posit.length;
                int begin = rawOffset + fileOffset - i;
                int end = rawOffset + fileOffset - i + rawData.length;
                if (end > rawOffset + rawLength) {
                    continue;
                }
                List<Byte> content = segment.positData.subList(begin, end);
                if (contentMatch(rawData, content)) {
                    return orgId;
                }
            }
            return -1;
        }
        return -1;
    }

    /**
     * description：判断元是二进制和指纹库中检索出来的指纹段是否相似
     *
     * @param rawData 原始二进制串
     * @param content 指纹库中检索出来的数据段
     * @return true ： 相似； false ：不相似
     */
    private static boolean contentMatch(byte[] rawData, List<Byte> content) {
        if (rawData.length != content.size()) {
            return false;
        }
        Byte[] cont = content.toArray(new Byte[content.size()]);
        int count = 0;
        for (int i = 0; i < rawData.length; i += 2) {
            int key1 = getMetaKeyFromBuffer(rawData, i);
            int key2 = getMetaKeyFromBuffer(cont, i);
            count += table[key1 ^ key2];
        }
        if (count < rawData.length * 8 * 0.37) {
            return true;
        }
        return false;
    }

    private static int getKeyFromRawData(byte[] rawData, int offset) {
        int value = (int) ((rawData[offset] & 0xFF) |
                ((rawData[offset + 1] & 0xFF) << 8) |
                ((rawData[offset + 2] & 0xFF) << 16)|
                ((rawData[offset + 3] & 0xFF) << 24));
        if ((value & 1) == 0) {
            return 0;
        }
        return (value >> 1) & 0x7FFFFFFF;
    }

    private static int getMetaKeyFromBuffer(byte[] buffer, int offset) {
        int value = (buffer[offset] & 0xFF)|((buffer[offset+1] & 0xFF) << 8);
        return value;
    }

    private static int getMetaKeyFromBuffer(Byte[] buffer, int offset) {
        int value = (buffer[offset] & 0xFF)|((buffer[offset+1] & 0xFF) << 8);
        return value;
    }
}
