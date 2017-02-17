package sivan.yue.quarrier.common.data;

/**
 * Created by xiwen.yxw on 2017/2/17.
 */
public class ByteVectorTest {

    public static void main(String[] args) {
        ByteVector byteVector = new ByteVector();
        for (byte i = 0; i < 100; ++i) {
            byteVector.addByte(i);
        }

        byte[] bytes  = byteVector.getBytes(9, 35);
        for (byte b : bytes) {
            System.out.println("val = " + b);
        }

        byteVector.addBytes(bytes);

        for (int i = 0; i < byteVector.getCurrentLen(); ++i) {
            System.out.println(byteVector.getByte(i));
        }
    }
}
