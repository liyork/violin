package com.wolf.structural.proxy;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/9
 */
public interface Image {

	public void draw();

	public Extent getExtent();

	public String getFileName();

	public void insertContent(String content);
}
