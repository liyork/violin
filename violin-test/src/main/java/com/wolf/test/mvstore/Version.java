package com.wolf.test.mvstore;

import org.h2.mvstore.MVMap;
import org.h2.mvstore.MVStore;

/**
 * Description:
 * Created on 2021/9/14 12:11 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class Version {
    public static void main(String[] args) {
        String fileName = "/Users/chaoli/intellijWrkSpace/violin/violin-test/src/main/java/com/wolf/test/mvstore/a.txt";
        MVStore s = MVStore.open(fileName);
        // create/get the map named "data"
        MVMap<Integer, String> map = s.openMap("data");

// add some data
        map.put(1, "Hello");
        map.put(2, "World");

// get the current version, for later use
        long oldVersion = s.getCurrentVersion();

// from now on, the old version is read-only
        s.commit();

// more changes, in the new version
// changes can be rolled back if required
// changes always go into "head" (the newest version)
        map.put(1, "Hi");
        map.remove(2);

// access the old data (before the commit)
        MVMap<Integer, String> oldMap = map.openVersion(oldVersion);

// print the old version (can be done
// concurrently with further modifications)
// this will print "Hello" and "World":
        System.out.println(oldMap.get(1));
        System.out.println(oldMap.get(2));

// print the newest version ("Hi")
        System.out.println(map.get(1));
    }
}
