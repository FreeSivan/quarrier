package sivan.yue.quarrier.common.tools;

import sivan.yue.quarrier.common.exception.FileFormatErrorException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * description : 对类加锁的多线程互斥访问文件类
 *
 * 根据文件名获取全局锁，该锁在多线程中可见。
 * 锁必须再FileLockTool中配置。
 *
 * Created by xiwen.yxw on 2017/2/16.
 */
public class ThreadMutexFile implements Closeable {

    private RandomAccessFile realFile = null;

    private String fileName;

    public ThreadMutexFile(String name, String mode) throws FileNotFoundException{
        File file = new File(name);
        realFile = new RandomAccessFile(file, mode);
        fileName = file.getName();
        System.out.println("fileName = " + fileName);
    }

    public Integer readInt() {
        Integer value = null;
        synchronized (FileLockContainer.lockMap.get(fileName)) {
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
        synchronized (FileLockContainer.lockMap.get(fileName)) {
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
        synchronized (FileLockContainer.lockMap.get(fileName)) {
            try {
                realFile.seek(realFile.length());
                realFile.writeInt(value);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeIntList(List<Integer> lst) {
        synchronized (FileLockContainer.lockMap.get(fileName)) {
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
