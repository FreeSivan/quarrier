package sivan.yue.quarrier.build;

/**
 * description： 任务基类
 *
 * quarrier项目建库的主要工作都是以创建task的模式完成，
 *      1. 达到一定条件式创建一个相应的task
 *      2. 将创建好的task放入ScheduleCenter中
 *      3. 后台的多线程从ScheduleCenter中取任务并执行
 * 条件： 各个任务必须是独立的，即：任务之间没有依赖关系
 *
 * Created by xiwen.yxw on 2017/2/10.
 */
public interface IMemoryTask extends Runnable{
}
