package sivan.yue.quarrier.common.tools;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
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
                long count = 0;
                while (count + 4 <= length) {
                    int value = realFile.readInt();
                    System.out.println("value = " + value);
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
