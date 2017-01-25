package com.wolf.behavioral.chain.practical;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/19
 * Time: 12:53
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class FilterChain {

	private int score;
	private String desc;
	private FilterChain next;

	public FilterChain(int score, String desc) {
		this.score = score;
		this.desc = desc;
	}

	public void setNext(FilterChain next) {
		this.next = next;
	}

	public String handle(int score) {
		if (score > this.score) {
			return this.desc;
		}else if (null != next) {
			return next.handle(score);
		}else {
			return "";
		}
	}
}
