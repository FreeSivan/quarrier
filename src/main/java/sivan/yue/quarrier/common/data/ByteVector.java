package sivan.yue.quarrier.common.data;

import sivan.yue.quarrier.common.exception.AccessOutBoundException;

import java.util.Vector;

/**
 *
 * Created by xiwen.yxw on 2017/2/17.
 */
public class ByteVector {
    /**
     * 默认每个byte[]的大小为16M
     */
    private static final int ARRAY_LENGTH = 0x1000000;

    private Vector<byte[]> data = new Vector<>();

    private int currentLen;

    public ByteVector() {
        currentLen = 0;
        data.clear();
    }

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

    public byte getByte(int index) {
        if (index >= currentLen) {
            throw new AccessOutBoundException("out of array size");
        }
        int bunchIndex = index / ARRAY_LENGTH;
        int bunchOffset = index % ARRAY_LENGTH;
        return data.get(bunchIndex)[bunchOffset];
    }

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

    public byte[] getBytes(int offset, int length) {
        if (offset + length > currentLen) {
            return null;
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

    public int getCurrentLen() {
        return this.currentLen;
    }
}
