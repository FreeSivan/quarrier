package sivan.yue.quarrier.search;

/**
 * description : 二进制搜索接口
 *
 * Created by xiwen.yxw on 2017/2/15.
 */
public interface ISearch<T> {
    /**
     * description : 单线程模式检索二进制流
     * @param rawData 待检索的二进制流
     * @return 二进制所属的文档原始id
     */
    public int singleSearch(byte[] rawData);

    /**
     * description : 多线程模式检索二进制流
     * @param rawData 待检索的二进制流
     * @return 二进制所属的文档原始id
     */
    public int multiSearch(byte[] rawData);

    /**
     * 向search中添加子索引单元
     * @param subIndex 子索引单元
     */
    public void addSubIndex(T subIndex);
}
