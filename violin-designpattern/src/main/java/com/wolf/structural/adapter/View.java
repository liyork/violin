package com.wolf.structural.adapter;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/4
 * Time: 10:13
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public interface View {

	public void showOnScreen(Double origin, Integer width, Integer height);

	public void isEmpty();
}