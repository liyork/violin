package com.wolf.test.mvstore;

import org.h2.mvstore.MVMap;
import org.h2.mvstore.MVStore;

/**
 * Description:
 * Created on 2021/9/14 12:03 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class MvStoreTest {

    public static void main(String[] args) {
        String fileName = "/Users/chaoli/intellijWrkSpace/violin/violin-test/src/main/java/com/wolf/test/mvstore/a.txt";
        // open the store (in-memory if fileName is null)
        MVStore s = new MVStore.Builder().
                fileName(fileName).
                encryptionKey("007".toCharArray()).
                compress().
                open();
        // 配置
        //autoCommitBufferSize: 写buffer的大小.
        //autoCommitDisabled: 禁用自动commit.
        //backgroundExceptionHandler: 用于处理后台写入时产生的异常的处理器.
        //cacheSize: 缓存大小，以MB为单位.
        //compress: 是否采用LZF算法进行快速压缩.
        //compressHigh: 是否采用Deflate算法进慢速速压缩.
        //encryptionKey: 文件加密的key.
        //fileName: 基于文件存储时，用于存储的文件名.
        //fileStore: 存储实现.
        //pageSplitSize: pages的分割点.
        //readOnly: 是否以只读形式打开存储文件.

        // create/get the map named "data"
        MVMap<Integer, String> map = s.openMap("data");

        // add and read some data
        map.put(1, "Hello World");
        System.out.println(map.get(1));

        // close the store (this will persist changes)
        s.close();
    }
}
