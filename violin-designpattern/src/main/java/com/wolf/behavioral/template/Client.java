package com.wolf.behavioral.template;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/18
 * Time: 8:28
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Client {

	public static void main(String[] args) {
		Application drawApplication = new DrawApplication();
		drawApplication.openDocument("xx.doc");

		Application spreadsheetApplication = new SpreadsheetApplication();
		spreadsheetApplication.openDocument("aa.doc");
	}
}
