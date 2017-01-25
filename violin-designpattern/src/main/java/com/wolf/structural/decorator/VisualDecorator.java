package com.wolf.structural.decorator;

/**
 * <p> Description:
 * Adapter (157): A decorator is different from an adapter in that a decorator only
 * changes an object's responsibilities, not its interface; an adapter will give
 * an object a completely new interface.
 * <p/>
 * Composite (183): A decorator can be viewed as a degenerate composite with only
 * one component. However, a decorator adds additional responsibilities—it isn't
 * intended for object aggregation.
 * <p/>
 * Strategy (349): A decorator lets you change the skin of an object; a strategy
 * lets you change the guts. These are two alternative ways of changing an object
 * <p/>
 * Attach additional responsibilities to an object dynamically. Decorators provide a flexible alternative to subclassing for extending functionality.
 * Sometimes we want to add responsibilities to individual objects, not to an entire class
 * Instead of trying to support all foreseeable features in a complex, customizable class, you can
 *define a simple class and add functionality incrementally with Decorator objects
 *
 * Keeping Component classes lightweight
 *
 * Strategies are a better choice in situations where the Component class is intrinsically heavyweight,
 * thereby making the Decorator pattern too costly to apply
 * Date: 2016/7/5
 * Time: 9:13
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public interface VisualDecorator {

}
