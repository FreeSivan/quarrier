package sivan.yue.quarrier.common.data;

import sivan.yue.quarrier.common.exception.AccessOutBoundException;

import java.util.Vector;

/**
 * description : byte数组的容器，用于存放原始数据文件。
 *
 * 每次分配16M的byte数组大小，填充满之后继续申请
 *
 * Created by xiwen.yxw on 2017/2/17.
 */
public class ByteVector {
    /**
     * 默认每个byte[]的大小为16M
     */
    private static final int ARRAY_LENGTH = 0x1000000;

    /**
     * 存放数据的vector，不设置最大长度
     */
    private Vector<byte[]> data = new Vector<>();

    /**
     * 当前数据长度
     */
    private int currentLen;

    public ByteVector() {
        currentLen = 0;
        data.clear();
    }

    /**
     * description ：添加数据函数
     *
     * 向容器中添加一个字节
     *
     * @param val 待添加的数据
     */
    public void addByte(byte val) {
        if (currentLen % ARRAY_LENGTH == 0) {
            byte[] newBunch = new byte[ARRAY_LENGTH];
            data.add(newBunch);
        }
        // 计算位置
        int bunchIndex = currentLen / ARRAY_LENGTH;
        int bunchOffset = currentLen % ARRAY_LENGTH;
        data.get(bunchIndex)[bunchOffset] = val;
        currentLen ++;
    }

    /**
     * description ：获取数据函数
     *
     * 从容器中指定位置获取数据，越界则抛出异常
     *
     * @param index 指定的位置
     * @return 获取的数据
     */
    public byte getByte(int index) {
        if (index >= currentLen) {
            throw new AccessOutBoundException("out of array size");
        }
        int bunchIndex = index / ARRAY_LENGTH;
        int bunchOffset = index % ARRAY_LENGTH;
        return data.get(bunchIndex)[bunchOffset];
    }

    /**
     * description ：添加数据函数
     *
     * 向容器中添加一组数据
     *
     * @param val 添加的字节序列
     */
    public void addBytes(byte[] val) {
        int length = val.length;
        int i = 0;
        while (i < length) {
            if (currentLen % ARRAY_LENGTH == 0) {
                byte[] newBunch = new byte[ARRAY_LENGTH];
                data.add(newBunch);
            }
            int bunchIndex = currentLen / ARRAY_LENGTH;
            int bunchOffset = currentLen % ARRAY_LENGTH;
            while ((i != length) && (bunchOffset < ARRAY_LENGTH)) {
                data.get(bunchIndex)[bunchOffset++] = val[i++];
                currentLen ++;
            }
        }
    }

    /**
     *
     * description ：获取数据函数
     *
     * 从指定位置获取指定长度的数据，越界抛异常
     *
     * @param offset 指定获取数据的位置
     * @param length 指定获取数据的长度
     * @return 获取的数据
     */
    public byte[] getBytes(int offset, int length) {
        if (offset + length > currentLen) {
            throw new AccessOutBoundException("out of array size");
        }
        byte[] result = new byte[length];
        int i = offset;
        while (i < offset + length) {
            int bunchIndex = i / ARRAY_LENGTH;
            int bunchOffset = i % ARRAY_LENGTH;
            while (i < offset + length && bunchOffset < ARRAY_LENGTH) {
                result[i-offset] = data.get(bunchIndex)[bunchOffset ++];
                i++;
            }
        }
        return result;
    }

    /**
     * description ： 获取当前长度
     * @return 容器当前持有的字节数
     */
    public int getCurrentLen() {
        return this.currentLen;
    }
}
