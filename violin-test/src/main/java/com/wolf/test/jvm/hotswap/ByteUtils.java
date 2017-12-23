package com.wolf.test.jvm.hotswap;

/**
 * Description:
 * <br/> Created on 11/5/17 8:32 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class ByteUtils {

    public static int byte2Int(byte[] b, int start, int len) {
        int sum = 0;
        int end = start + len;
        for (int i = start; i < end; i++) {
            int n = ((int) b[i]) & 0xff; //取8位,十进制就是256
            n <<= (--len) * 8;//n*(index*8代表权重，0xff表示256进制第一位是256^0=2^8^0*，第二位是256^1=2^8^1，第三位:256^2=2^16)
            sum = n + sum;//加总每位权重值
        }
        return sum;
    }

    public static byte[] int2Bytes(int value, int len) {
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[len - i - 1] = (byte) ((value >> 8 * i) & 0xfff);
        }
        return b;
    }

    public static String bytes2String(byte[] b, int start, int len) {
        return new String(b, start, len);
    }

    public static byte[] string2Bytes(String string) {
        return string.getBytes();
    }

    //写入前中后三段数据
    public static byte[] bytesReplace(byte[] originalBytes, int offset, int len, byte[] replaceBytes) {
        int replaceLength = replaceBytes.length;
        int originLength = originalBytes.length;
        byte[] newBytes = new byte[originLength + (replaceLength - len)];//原始大小+新替换数据相对原先的差值
        System.arraycopy(originalBytes, 0, newBytes, 0, offset);//拷贝偏移量前的数据
        System.arraycopy(replaceBytes, 0, newBytes, offset, replaceLength);//从偏移量开始拷贝新数据
        System.arraycopy(originalBytes, offset + len, newBytes, offset + replaceLength, originLength - offset - len);
        return newBytes;
    }

    public static void main(String[] args) {
        System.out.println(0xff);
        int n = 34;//0
        int len = 2;
        n <<= (--len) * 8;
        System.out.println(n);

        byte[] by = new byte[]{1, 2};//每个int占4个字节00000001 00000010 = 256
        int constantPoolCount = byte2Int(by, 0, 2);
        System.out.println(constantPoolCount);

        System.out.println(64 >> 8 * 2);
        System.out.println(int2Bytes(258,2));
        System.out.println(Integer.toBinaryString(258));
    }
}
