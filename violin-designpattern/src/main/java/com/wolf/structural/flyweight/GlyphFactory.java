package com.wolf.structural.flyweight;

import java.util.HashMap;
import java.util.Map;

/**
 * <p> Description:按照类别创建固定数量对象
 * <p/>
 * Date: 2016/7/7
 * Time: 9:44
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class GlyphFactory {

	private int count;

	Map<String,Character> characters = new HashMap<String, Character>();

	public Character getCharacter(String charCode){
		Character character = characters.get(charCode);
		if (null == character) {
			character = new Character(charCode);
			characters.put(charCode, character);
		}
		return character;
	}

	public int getAndIncreaseCount() {
		return count++;
	}
}
