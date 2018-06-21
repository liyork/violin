package com.wolf.test.classloader.hotswap;

/**
 * Description:提供修改常量池中常量字符
 * <br/> Created on 11/5/17 8:24 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class ClassModifier {

    //常量池起始偏移量
    private static final int CONSTANT_POOL_COUNT_INDEX = 8;

    //tag标志
    private static final int CONSTANT_Utf8_info = 1;

    //11中常量占用长度，CONSTANT_Utf8_info除外，他不是定长的
    private static final int[] CONSTANT_ITEM_LENGTH = {-1, -1, -1, 5, 5, 9, 9, 3, 3, 5, 5, 5, 5};

    private static final int u1 = 1;
    private static final int u2 = 2;

    private byte[] classByte;

    public ClassModifier(byte[] classByte) {
        this.classByte = classByte;
    }

    /**
     * 修改常量池中CONSTANT_Utf8_info中内容
     * @param oldStr
     * @param newStr
     * @return
     */
    public byte[] modifyUTF8Constant(String oldStr, String newStr) {
        int cpc = getConstantPoolCount();
        int offset = CONSTANT_POOL_COUNT_INDEX + u2;
        for (int i = 0; i < cpc; i++) {
            int tag = ByteUtils.byte2Int(classByte, offset, u1);
            if (tag == CONSTANT_Utf8_info) {
                int len = ByteUtils.byte2Int(classByte, offset + u1, u2);//CONSTANT_Utf8_info后两个字节表示常量数量
                offset += (u1 + u2);
                String str = ByteUtils.bytes2String(classByte, offset, len);
                if (str.equalsIgnoreCase(oldStr)) {
                    byte[] strBytes = ByteUtils.string2Bytes(newStr);
                    byte[] strLen = ByteUtils.int2Bytes(newStr.length(), u2);
                    classByte = ByteUtils.bytesReplace(classByte, offset - u2, u2, strLen);//写入两字节的长度
                    classByte = ByteUtils.bytesReplace(classByte, offset, len, strBytes);//写入实际内容
                    return classByte;
                } else {
                    offset += len;
                }
            } else {
                offset += CONSTANT_ITEM_LENGTH[tag];
            }
        }

        return classByte;
    }

    public int getConstantPoolCount() {
        //常量池从8开始，2个字节代表后续的常量数量。
        return ByteUtils.byte2Int(classByte, CONSTANT_POOL_COUNT_INDEX, u2);
    }
}
