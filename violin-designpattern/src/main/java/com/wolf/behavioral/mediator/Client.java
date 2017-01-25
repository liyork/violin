package com.wolf.behavioral.mediator;

/**
 * <b>功能</b>
 * The mediator serves as an intermediary that keeps objects in the group from referring
 * to each other explicitly.It acts as a hub of communication for widgets
 * Widgets communicate with each other only indirectly, through the director.
 * <p/>
 * applicability
 * • a set of objects communicate in well-defined but complex ways. The resulting
 * interdependencies are unstructured and difficult to understand.
 * • reusing an object is difficult because it refers to and communicates with many
 * other objects.
 * • a behavior that's distributed between several classes should be customizable
 * without a lot of subclassing.
 * <p/>
 * A mediator replaces many-to-many interactions
 * with one-to-many interactions between the mediator and its colleagues.
 * <p/>
 * Facade differs from Mediator in that it abstracts a subsystem of objects
 * to provide a more convenient interface. Its protocol is unidirectional; In
 * contrast, Mediator enables cooperative behavior that colleague objects don't
 * or can't provide, and the protocol is multidirectional
 *
 * @author 李超
 * @Date 2016/7/14
 */
public class Client {

	public static void main(String[] args) {
		FontDialogDirector dialogDirector = new FontDialogDirector();
		dialogDirector.createDialog();

		Text text = dialogDirector.getText();
		ListBox listBox = dialogDirector.getListBox();
		Button button = dialogDirector.getButton();

		listBox.change();

		text.change();

		button.change();

	}
}
