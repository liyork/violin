package com.wolf.test.jvm.hotswap;

import javax.swing.text.html.HTML;

/**
 * Description:
 * <br/> Created on 11/5/17 8:22 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class HotSwapClassLoader extends ClassLoader {

    public HotSwapClassLoader() {
        super(HotSwapClassLoader.class.getClassLoader());
    }

    public Class loadByte(byte[] classByte) {
        return defineClass(null, classByte, 0, classByte.length);
    }
}
