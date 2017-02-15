package sivan.yue.quarrier.search.load;

import sivan.yue.quarrier.search.service.IService;

/**
 * Created by xiwen.yxw on 2017/2/15.
 */
public interface ILoad {

    /**
     *
     * @param path
     */
    public void singleLoad(String path);

    /**
     *
     * @param path
     */
    public void multiLoad(String path);
}
