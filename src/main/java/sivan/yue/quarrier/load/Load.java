package sivan.yue.quarrier.load;

import sivan.yue.quarrier.build.writer.WriterTask;
import sivan.yue.quarrier.common.tools.ProcessMutexFile;
import sivan.yue.quarrier.common.tools.ThreadMutexFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * description: load数据基类
 *
 * 同一时刻要保证只能有一个load类实例运行
 * 某个实例尝试运行时，如果发现有load正在运行
 * 则直接结束，不用等到其运行完毕再执行
 *
 * Created by xiwen.yxw on 2017/2/15.
 */
public abstract class Load implements ILoad {

    @Override
    public abstract void singleLoad(String path);

    @Override
    public void multiLoad(String path) {
        String fName = WriterTask.indexDir + "segment";
        try {
            ThreadMutexFile file = new ThreadMutexFile(fName, "r");
            List<Integer> indexLst  = file.readIntList();
            file.close();
            for (Integer index : indexLst) {
                createTask(index);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void createTask(int index);
}
