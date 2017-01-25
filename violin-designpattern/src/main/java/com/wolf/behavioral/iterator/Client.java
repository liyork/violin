package com.wolf.behavioral.iterator;

/**
 * <p> Description:
 * 					row
 * 			column      text2
 * 		text1    graph
 * <p/>
 * Date: 2016/6/30
 * Time: 9:23
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Client {

	public static void main(String[] args) {
//		simpleTestOneLevel();
		preOrderTraversal();
	}

	//简单测试1层，通过iterator统一了所有数据的访问，隐藏了底层数据结构细节。
	private static void simpleTestOneLevel() {
		Glyph row = new Row("row glyph");
		Glyph column1 = new Column("column1 glyph");
		Glyph column2 = new Column("column2 glyph");
		row.addChild(column1);
		row.addChild(column2);

		Glyph column3 = new Column("column3 glyph");
		Glyph text1 = new Column("text1 glyph");
		Glyph text2 = new Column("text2 glyph");
		column3.addChild(text1);
		column3.addChild(text2);


		traversal(row, row.createIterator(IteratorType.COMMON));
		traversal(column3, column3.createIterator(IteratorType.COMMON));
	}


	//前序遍历
	private static void preOrderTraversal() {

		Glyph top = new Row("top");
		Glyph row = new Row("row glyph");
		Glyph column = new Column("column glyph");
		Glyph text1 = new Text("text1 content");
		Glyph text2 = new Text("text2 content");
		Glyph graph = new Graph("graph glyph");

		column.addChild(text1);
		column.addChild(graph);

		row.addChild(column);
		row.addChild(text2);

		top.addChild(row);

		traversal(top, top.createIterator(IteratorType.PRE_ORDER));
	}

	private static void traversal(Glyph top, MyIterator iterator) {
		System.out.println(top.getName());
		for (iterator.moveFirst(); !iterator.isDone(); iterator.next()) {
			Glyph currentGlyph = iterator.getCurrentItem();
			System.out.println(currentGlyph.getName());
		}
	}
}
