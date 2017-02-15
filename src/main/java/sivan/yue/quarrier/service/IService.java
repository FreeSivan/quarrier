package sivan.yue.quarrier.service;

/**
 * Created by xiwen.yxw on 2017/2/15.
 */
public interface IService<T> {
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
     * @param segment
     */
    public void addIndexSegment(T segment);
}
