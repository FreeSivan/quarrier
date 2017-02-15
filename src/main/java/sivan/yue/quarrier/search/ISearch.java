package sivan.yue.quarrier.search;

/**
 * Created by xiwen.yxw on 2017/2/15.
 */
public interface ISearch<T> {
    /**
     *
     * @param rawData
     * @return
     */
    public int singleSearch(byte[] rawData);

    /**
     *
     * @param rawData
     * @return
     */
    public int multiSearch(byte[] rawData);

    /**
     *
     * @param subIndex
     */
    public void addSubIndex(T subIndex);
}
