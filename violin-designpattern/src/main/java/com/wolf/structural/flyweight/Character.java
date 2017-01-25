package com.wolf.structural.flyweight;

/**
 * <p> Description:多个Character大部分内容重复，少部分不同，如果每个都创建很浪费内存，大家公用一个，不同的地方通过外界传入，
 * intrinsic   extrinsic
 *
 * <p/>
 * Date: 2016/7/7
 * Time: 9:38
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Character {

	private String charCode;

	public Character(String charCode) {
		this.charCode = charCode;
	}

	public String getCharCode() {
		return charCode;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		Character character = (Character) o;

		if (charCode != null ? !charCode.equals(character.charCode) : character.charCode != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return charCode != null ? charCode.hashCode() : 0;
	}

}
