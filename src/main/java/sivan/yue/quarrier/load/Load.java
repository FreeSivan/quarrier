package sivan.yue.quarrier.load;

import sivan.yue.quarrier.build.writer.WriterTask;
import sivan.yue.quarrier.load.ILoad;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

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
            RandomAccessFile file = new RandomAccessFile(fName, "r");
            FileChannel channel = file.getChannel();
            FileLock lock = null;
            while(true) {
                try {
                    lock = channel.lock();
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            int count = 0;
            while(count < file.length()) {
                int index = file.readInt();
                count += 4;
                createTask(index);
            }
            lock.release();
            channel.close();
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void createTask(int index);
}
