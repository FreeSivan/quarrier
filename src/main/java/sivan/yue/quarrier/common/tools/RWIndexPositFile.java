package sivan.yue.quarrier.common.tools;

import sivan.yue.quarrier.common.data.Segment;
import sivan.yue.quarrier.common.exception.FileAccessErrorException;
import sivan.yue.quarrier.common.exception.FileFormatErrorException;
import sivan.yue.quarrier.common.exception.FileNotFindException;

import java.io.*;
import java.util.Map;
import java.util.Vector;

/**
 * description : segment 内存结构和磁盘存储互相转化的工具类
 * 包括：
 * 1. 写倒排索引，加载倒排索引
 * 2. 写倒排文件，加载倒排文件
 * 3. 写正排索引，加载正排索引
 * 4. 写正排文件，加载正排文件
 *
 * Created by xiwen.yxw on 2017/2/16.
 */
public class RWIndexPositFile {

    /**
     * description : 写倒排索引函数
     *
     * 将segment对象中的index对象写入indexName指定的文件中
     * 写入顺序：key（4字节）length（4字节）offset（4字节）
     * 每个key的倒排索引项占用12个字节的大小
     *
     * @param indexName 倒排索引文件的文件名
     * @param segment 存放倒排索引文件的段结构
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
            throw new FileNotFindException("index file not find!");
        } catch (IOException e) {
            throw new FileAccessErrorException("index file write error!");
        }
    }

    /**
     * description : 加载倒排索引函数
     *
     * 将磁盘中存储的倒排索引文件加载到内存中segment对象的indexRun结构中
     * 读文件顺序：key（4字节）length（4字节）offset（4字节）
     * 每个key的倒排索引项占用12个字节的大小，该文件的大小必须是12整数倍
     *
     * @param indexName 倒排索引文件的文件名
     * @param segment 存放倒排索引文件的段结构
     */
    public static void loadIndexFile(String indexName, Segment segment) {
        Map<Integer, Segment.Index> index = segment.indexRun;
        try {
            RandomAccessFile realFile = new RandomAccessFile(indexName, "r");
            long length = realFile.length();
            if ((length % 12) != 0) {
                throw new FileFormatErrorException("Index File Format Error!");
            }
            long count = 0;
            while (count < length) {
                Segment.Index indexMeta = new Segment.Index();
                int key = realFile.readInt();
                indexMeta.length = realFile.readInt();
                indexMeta.offset = realFile.readInt();
                count += 12;
                index.put(key, indexMeta);
            }
            realFile.close();
        } catch (FileNotFoundException e) {
            throw new FileNotFindException("index file not find!");
        } catch (IOException e) {
            throw new FileAccessErrorException("index file load error!");
        }
    }

    /**
     * description : 写倒排文件到磁盘中
     *
     * 将segment对象中的indexData对象写入iValueName指定的文件中
     * 写入顺序：docId（4字节）offset（4字节） 每个倒排项占用8个字节的大小
     *
     * @param iValueName 倒排文件的文件名
     * @param segment 存放倒排项的segment结构
     */
    public static void writeIValueFile(String iValueName, Segment segment) {
        Vector<Segment.IndexMeta> indexData = segment.indexData;
        try {
            DataOutputStream out=new DataOutputStream(new FileOutputStream(iValueName));
            for (Segment.IndexMeta indexMeta : indexData) {
                out.writeInt(indexMeta.docId);
                out.writeInt(indexMeta.offset);
            }
            out.close();
        } catch (FileNotFoundException e) {
            throw new FileNotFindException("index Value file not find!");
        } catch (IOException e) {
            throw new FileAccessErrorException("index value file write error!");
        }
    }

    /**
     * description : 读倒排文件到内存中
     *
     * 将磁盘中存储的倒排文件加载到内存中segment对象的indexData结构中
     * 读文件顺序：docId（4字节）offset（4字节），该文件的大小必须是8整数倍
     *
     * @param iValueName 倒排文件的文件名
     * @param segment 存放倒排项的segment结构
     */
    public static void loadIValueFile(String iValueName, Segment segment) {
        Vector<Segment.IndexMeta> indexData = segment.indexData;
        try {
            RandomAccessFile realFile = new RandomAccessFile(iValueName, "r");
            long length = realFile.length();
            if ((length % 8) != 0) {
                throw new FileFormatErrorException("IValue File Format Error!");
            }
            long count = 0;
            while (count < length) {
                Segment.IndexMeta indexMeta = new Segment.IndexMeta();
                indexMeta.docId = realFile.readInt();
                indexMeta.offset = realFile.readInt();
                count += 8;
                indexData.add(indexMeta);
            }
            realFile.close();
        } catch (FileNotFoundException e) {
            throw new FileNotFindException("index Value file not find!");
        } catch (IOException e) {
            throw new FileAccessErrorException("index value file load error!");
        }
    }

    /**
     * description : 写正排索引到磁盘文件中
     *
     * 将segment对象中的posit对象写入positName指定的文件中
     * 写入顺序：key（四字节）docId（4字节）offset（4字节）orgId（4字节）
     * 每个正排索引占用12个字节的大小
     *
     * @param positName 磁盘中正排索引文件的文件名
     * @param segment 内存中存放正排索引结构的segment
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
            throw new FileNotFindException("posit file not find!");
        } catch (IOException e) {
            throw new FileAccessErrorException("posit file write error!");
        }
    }

    /**
     * description : 读正排索引到内存中
     *
     * 将磁盘中的正排索引文件都到内存segment对象的posit结构中
     * 读文件顺序 ：key（4字节）docId（4字节）offset（4字节）orgId（4字节）
     * 每个正派索引占用16个字节的大小
     *
     * @param positName 正排索引文件的文件名
     * @param segment 保存正排索引文件内容的段结构
     */
    public static void loadPositFile(String positName, Segment segment) {
        Map<Integer, Segment.Posit> posit = segment.posit;
        try {
            RandomAccessFile realFile = new RandomAccessFile(positName, "r");
            long length = realFile.length();
            if ((length % 16) != 0) {
                throw new FileFormatErrorException("posit File Format Error!");
            }
            long count = 0;
            while (count < length) {
                Segment.Posit positValue = new Segment.Posit();
                int key = realFile.readInt();
                positValue.length = realFile.readInt();
                positValue.offset = realFile.readInt();
                positValue.orgId = realFile.readInt();
                count += 16;
                posit.put(key, positValue);
            }
            realFile.close();
        } catch (FileNotFoundException e) {
            throw new FileNotFindException("posit file not find!");
        } catch (IOException e) {
            throw new FileAccessErrorException("posit file load error!");
        }
    }

    /**
     * description : 写正排文件到磁盘中
     *
     * 将segment对象中的positData对象写入pValueName指定的文件中
     * positData以byte为单位，每个doc的原始内容依次写入正排文件中
     *
     * @param pValueName 正排文件名
     * @param segment 保存原始文件数据的segment
     */
    public static void writePValueName(String pValueName, Segment segment) {
        Vector<Byte> positData = segment.positData;
        try {
            DataOutputStream out=new DataOutputStream(new FileOutputStream(pValueName));
            byte[] bytes = new byte[positData.size()];
            for (int i = 0; i < positData.size(); ++i) {
                bytes[i] = positData.get(i);
            }
            out.write(bytes, 0, bytes.length);
            out.close();
        } catch (FileNotFoundException e) {
            throw new FileNotFindException("posit Value file not find!");
        } catch (IOException e) {
            throw new FileAccessErrorException("posit value file write error!");
        }
    }

    /**
     * description : 读正排文件到内存中
     *
     * 将pValueName指定的正排文件中的内容读到segment对象的positData中
     *
     * @param pValueName 正排文件的文件名
     * @param segment 保存正排文件原始数据的segment结构
     */
    public static void loadPValueName(String pValueName, Segment segment) {
        Vector<Byte> positData = segment.positData;
        try {
            RandomAccessFile realFile = new RandomAccessFile(pValueName, "r");
            long length = realFile.length();
            byte[] bytes = new byte[(int) length];
            realFile.read(bytes);
            for (byte val : bytes) {
                positData.add(val);
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFindException("posit Value file not find!");
        } catch (IOException e) {
            throw new FileAccessErrorException("posit value file load error!");
        }
    }
}
