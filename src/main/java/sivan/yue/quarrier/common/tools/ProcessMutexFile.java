package sivan.yue.quarrier.common.tools;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiwen.yxw on 2017/2/16.
 */
public class ProcessMutexFile implements Closeable {

    private RandomAccessFile realFile = null;

    private FileChannel channel = null;

    private FileLock lock = null;

    public ProcessMutexFile(String name, String mode) throws FileNotFoundException {
        realFile = new RandomAccessFile(name, mode);
        channel = realFile.getChannel();
        while(true) {
            try {
                lock = channel.lock();
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Integer readInt() {
        Integer value = null;
        try {
            if (realFile.length() < 4) {
                return null;
            }
            value = realFile.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    public List<Integer> readIntList() {
        List<Integer> lst= new ArrayList<Integer>();
        try {
            long length = realFile.length();
            long count = 0;
            while (count + 4 <= length) {
                lst.add(realFile.readInt());
                count += 4;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lst;
    }

    public void writeInt(int value) {
        try {
            realFile.seek(realFile.length());
            realFile.writeInt(value);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeIntList(List<Integer> lst) {
        try {
            realFile.seek(realFile.length());
            for (Integer value : lst) {
                realFile.writeInt(value);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void close() throws IOException {
        lock.release();
        channel.close();
        realFile.close();
    }
}
