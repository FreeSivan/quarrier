package sivan.yue.quarrier.build;

/**
 * Created by xiwen.yxw on 2017/2/22.
 */
public class CountTest {

    public static class BaseTest {
        public synchronized void flush() {
            System.out.println("baseTest");
        }
    }

    public static class Test1 extends BaseTest {
        private static int count = 0;

        public synchronized void flush() {
            super.flush();
            System.out.println("test1");
        }
        public static int getCount() {
            return count;
        }

        public static void setCount(int count) {
            Test1.count = count;
        }
    }

    public static class Test2 extends BaseTest {
        private static int count = 0;


        public static int getCount() {
            return count;
        }

        public static void setCount(int count) {
            Test2.count = count;
        }
    }

    public static void main(String[] args) {
        Test1 test1 = new Test1();
        Test2 test2 = new Test2();
        test1.flush();
        System.out.println(test1.getCount());
        System.out.println(test2.getCount());
        test1.setCount(3);
        System.out.println(test1.getCount());
        System.out.println(test2.getCount());
    }

}
