package com.wolf.behavioral.interpret.one;

import java.util.HashMap;
import java.util.Map;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/13
 */
public class Context {

	private Map<String, Boolean> map = new HashMap<String, Boolean>();

	public boolean lookup(String string) {
		//这里如果取不到，会报错空指针，是由于运行时要将null转换成boolean导致的。
		return map.get(string);
	}

	public void assign(String key,Boolean value) {
		map.put(key, value);
	}

}
