package sivan.yue.quarrier.data;

/**
 * description: 数据单位类
 *
 * 存储原始数据的基本单位
 * 对于音频指纹一个指纹就是一个文档
 *
 * Created by xiwen.yxw on 2017/2/10.
 */
public class Document {
    /**
     * 二进制文档id
     */
    public int docId;
    /**
     * 原始文件ID
     */
    public int orgId;
    /**
     * 二进制文档内容
     */
    public byte[] content;
}
