package com.wolf.behavioral.mediator;

/**
 * <b>功能</b>面向对象拆分小对象，交互太多，相互直接饮用。mediator pattern松散对象与对象之间的耦合，便于各个小对象的重用
 *
 * @author 李超
 * @Date 2016/7/14
 */
public class FontDialogDirector implements DialogDirector {

	private ListBox listBox;
	private Text text;
	private Button button;

	@Override
	public void createDialog(){
		listBox = new ListBox(this);
		text = new Text(this);
		button = new Button(this);

		// fill the listBox with the available font names
		// assemble the widgets in the dialog
	}

	@Override
	public void changeRelatedWidget(Widget widget) {
		if (widget instanceof Button) {
			((Button)widget).grayTheColor();
		}else if (widget instanceof ListBox) {
			text.setText(((ListBox)widget).getSelection());
		}else if (widget instanceof Text) {
			listBox.setSelection(((Text) widget).getText());
		}
	}

	public ListBox getListBox() {
		return listBox;
	}

	public Text getText() {
		return text;
	}

	public Button getButton() {
		return button;
	}
}
