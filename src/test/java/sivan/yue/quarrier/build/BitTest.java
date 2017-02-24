package sivan.yue.quarrier.build;

import sivan.yue.quarrier.common.exception.FileAccessErrorException;
import sivan.yue.quarrier.common.exception.FileNotFindException;

import java.io.*;

/**
 * Created by xiwen.yxw on 2017/2/24.
 */
public class BitTest {

    public static int getKey(byte[] data, int offset) {
        int value = (int) ((data[offset] & 0xFF) |
                ((data[offset + 1] & 0xFF) << 8) |
                ((data[offset + 2] & 0xFF) << 16)|
                ((data[offset + 3] & 0xFF) << 24));
        if ((value & 1) == 0) {
            return -1;
        }
        return (value >> 1) & 0x7FFFFFFF;
    }

    public static int getValue(byte[] data, int offset) {
        int value = (int) ((data[offset] & 0xFF) |
                ((data[offset + 1] & 0xFF) << 8) |
                ((data[offset + 2] & 0xFF) << 16)|
                ((data[offset + 3] & 0xFF) << 24));
        return value;
    }

    /*public static int getKey2(byte[] data, int offset1, int offset2) {

        int key = (int)((data[offset1] & 0xFF) |
                ((data[offset1 + 1] & 0xFF) << 8) |
                ((data[offset1 + 2] & 0xFF) << 16)|
                ((data[offset1 + 3] & 0xFF) << 24));
        int value = (int)((data[offset2] & 0xFF) |
                ((data[offset2 + 1] & 0xFF) << 8) |
                ((data[offset2 + 2] & 0xFF) << 16)|
                ((data[offset2 + 3] & 0xFF) << 24));
        return  ((value>>1) & 0x7FFF8000) | ((key>>1) & 0x7FFF);
    }*/

    public static int getKey2(byte[] data, int key, int offset) {
        int value = (int)((data[offset] & 0xFF) |
                ((data[offset + 1] & 0xFF) << 8) |
                ((data[offset + 2] & 0xFF) << 16)|
                ((data[offset + 3] & 0xFF) << 24));
        return  ((value >> 1) & 0x7FFF8000) | ((key) & 0x7FFF);
    }

    public static void main(String[] args) {
        File file1 = new File("D:\\work\\data\\afp\\3598439.afp");
        File file2 = new File("D:\\work\\data\\afp\\3598439.txt");
        try {
            RandomAccessFile realFile = new RandomAccessFile(file1, "r");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file2));
            long length = realFile.length();

            //int i = 0;
            /*
            while(i < length) {
                System.out.println(realFile.readInt());
                i += 4;
            }
            */
            byte[] bytes = new byte[(int)length];
            realFile.read(bytes);
            for (int i = 0; i < bytes.length; i += 4) {
                int value = getKey(bytes, i);
                if (value == -1) {
                    continue;
                }
                writer.write(""+value+"\n");
                if (i + 32 * 4 < bytes.length) {
                    writer.write(""+getValue(bytes, i) + ","+getValue(bytes, i+32*4)+"\n");
                    writer.write(""+((getValue(bytes, i+32*4)>>1)&0x7FFF8000) + "," + ((getValue(bytes, i)>>1)&0x7FFF) + "\n");
                    int value2 = getKey2(bytes, value, i + 32 * 4);
                    writer.write(""+value2+"\n");
                }
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFindException("posit Value file not find!");
        } catch (IOException e) {
            throw new FileAccessErrorException("posit value file load error!");
        }
    }
}
