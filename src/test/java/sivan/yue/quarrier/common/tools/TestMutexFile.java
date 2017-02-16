package sivan.yue.quarrier.common.tools;

import sivan.yue.quarrier.ITask;
import sivan.yue.quarrier.common.schedule.ScheduleCenter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by xiwen.yxw on 2017/2/16.
 */
public class TestMutexFile {

    private static final String FILE_NAME = "D:\\work\\data\\text.dat";

    public static void main(String[] args) {
        WriteEvent wEvent1 = new WriteEvent();
        wEvent1.index = 1;
        WriteEvent wEvent2 = new WriteEvent();
        wEvent2.index = 2;
        WriteEvent wEvent3 = new WriteEvent();
        wEvent3.index = 3;
        WriteEvent wEvent4 = new WriteEvent();
        wEvent4.index = 4;
        WriteEvent wEvent5 = new WriteEvent();
        wEvent5.index = 5;

        ReadEvent rEvent = new ReadEvent();
        ScheduleCenter.INSTANCE.addTask(wEvent1);
        ScheduleCenter.INSTANCE.addTask(wEvent2);
        ScheduleCenter.INSTANCE.addTask(wEvent3);
        ScheduleCenter.INSTANCE.addTask(wEvent4);
        ScheduleCenter.INSTANCE.addTask(wEvent5);
        ScheduleCenter.INSTANCE.addTask(rEvent);
    }

    public static class WriteEvent implements ITask {

        public int index = 0;

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    ThreadMutexFile mFile = new ThreadMutexFile(FILE_NAME, "rw");
                    mFile.writeInt(index);
                    mFile.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class ReadEvent implements ITask {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    ThreadMutexFile mFile = new ThreadMutexFile(FILE_NAME, "rw");
                    List<Integer> lst = mFile.readIntList();
                    mFile.close();
                    System.out.println("==============================");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
