package com.wolf.test.mvstore;

import org.h2.mvstore.MVStore;
import org.h2.mvstore.rtree.MVRTreeMap;
import org.h2.mvstore.rtree.SpatialKey;

import java.util.Iterator;

/**
 * Description: MVRTreeMap是一个用于快速的R-Tree实现
 * Created on 2021/9/14 12:08 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class MVRTreeMapTest {
    public static void main(String[] args) {
        // create an in-memory store
        MVStore s = MVStore.open(null);

// open an R-tree map
        MVRTreeMap<String> r = s.openMap("data",
                new MVRTreeMap.Builder<String>());

// add two key-value pairs
// the first value is the key id (to make the key unique)
// then the min x, max x, min y, max y
        r.add(new SpatialKey(0, -3f, -2f, 2f, 3f), "left");
        r.add(new SpatialKey(1, 3f, 4f, 4f, 5f), "right");

// iterate over the intersecting keys
        Iterator<SpatialKey> it =
                r.findIntersectingKeys(new SpatialKey(0, 0f, 9f, 3f, 6f));
        for (SpatialKey k; it.hasNext(); ) {
            k = it.next();
            System.out.println(k + ": " + r.get(k));
        }
        s.close();
    }
}
