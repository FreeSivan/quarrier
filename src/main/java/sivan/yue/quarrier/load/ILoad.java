package sivan.yue.quarrier.load;

/**
 * description: load数据接口
 *
 * 从磁盘中加载由build模块建好的文件到内存结构中
 * 不同的子类将数据加载到不同的内存结构中
 * 磁盘中每个segment是一个子索引，由一组文件组成，
 * 在内存中一个为一个子索引构造一个对象
 * Created by xiwen.yxw on 2017/2/15.
 */
public interface ILoad {

    /**
     * description : 单线程加载函数
     *
     * 单个线程逐个加载指定路径下的倒排文件到内存中
     * 为每个文件创建一个对象来持有所有的数据
     *
     * @param path 待加载的文件存在的路径
     */
    public void singleLoad(String path);

    /**
     * description : 多线程加载函数
     *
     * 找到指定目录下的所有倒排索引文件，为每个文件
     * 创建一个loadTask，放如调度中心由工作线程执行
     *
     * @param path 待加载的文件存在的路径
     */
    public void multiLoad(String path);
}
