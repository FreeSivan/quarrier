package sivan.yue.quarrier.common.tools;

import sivan.yue.quarrier.common.exception.FileFormatErrorException;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * description : 对类加锁的多线程互斥访问文件类
 *
 * 由于是对类加锁，所以即使是打开的不同文件，
 * 对不同文件的读写也会互相阻塞工作线程
 * 这个类只用于打开segment文件，不打开其他文件
 * Created by xiwen.yxw on 2017/2/16.
 */
public class ThreadMutexFile implements Closeable {

    private RandomAccessFile realFile = null;

    public ThreadMutexFile(String name, String mode) throws FileNotFoundException{
        realFile = new RandomAccessFile(name, mode);
    }

    public Integer readInt() {
        Integer value = null;
        synchronized (ThreadMutexFile.class) {
            try {
                if (realFile.length() < 4) {
                    return null;
                }
                value = realFile.readInt();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public List<Integer> readIntList() {
        List<Integer> lst= new ArrayList<Integer>();
        synchronized (ThreadMutexFile.class) {
            try {
                long length = realFile.length();
                if (length % 4 != 0) {
                    throw new FileFormatErrorException("segment format error!");
                }
                long count = 0;
                while (count + 4 <= length) {
                    int value = realFile.readInt();
                    lst.add(value);
                    count += 4;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lst;
    }

    public void writeInt(int value) {
        synchronized (ThreadMutexFile.class) {
            try {
                realFile.seek(realFile.length());
                realFile.writeInt(value);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeIntList(List<Integer> lst) {
        synchronized (ThreadMutexFile.class) {
            try {
                realFile.seek(realFile.length());
                for (Integer value : lst) {
                    realFile.writeInt(value);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void close() throws IOException {
        realFile.close();
    }
}
