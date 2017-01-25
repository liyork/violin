package com.wolf.behavioral.memento.onepackage;

/**
 * <b>功能</b>
 * Only the originator can store and retrieve information from the
 * memento—the memento is "opaque" to other objects.
 *
 * @author 李超
 * @Date 2016/7/15
 */
public class ConstraintResolver {

	private double aPosition;
	private double bPosition;

	public ConstraintResolver(double aPosition, double bPosition) {
		this.aPosition = aPosition;
		this.bPosition = bPosition;
	}

	public ResolverMemento createCurrentMemento() {
		return new ResolverMemento(aPosition, bPosition);
	}

	public void restoreMemento(ResolverMemento memento) {
		this.aPosition = memento.aPosition;
		this.bPosition = memento.bPosition;
		System.out.println("restore position by using memento");
	}

	public void increase() {
		this.aPosition++;
		this.bPosition++;
	}

	public void solve() {
		System.out.println("solve the line between two object" + this.aPosition + "," + this.bPosition);
	}
}
