package com.wolf.structural.flyweight;

/**
 * <p> Description:
 * Use sharing to support large numbers of fine-grained objects efficiently.
 * flyweight 应该是对于每种分类的object进行池化，对于池化里面的object在context中有通过对应的特定位置标记处于什么状态。
 * applicability:
 * An application uses a large number of objects,storage costs are high,most state can be made extrinsic.
 * The savings increase with the amount of shared state
 * 书上使用的btree维护character的颜色,非叶子节点表示字符数量，叶子节点表示字符颜色
 * <p/>
 * Date: 2016/7/7
 * Time: 9:47
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Client {

	//todo 似乎有些丑，以后有待改进
	public static void main(String[] args) {
		GlyphFactory glyphFactory = new GlyphFactory();
		Character a1 = glyphFactory.getCharacter("a");
		int a1Count = glyphFactory.getAndIncreaseCount();
		Character b = glyphFactory.getCharacter("b");
		int bCount = glyphFactory.getAndIncreaseCount();
		Character c = glyphFactory.getCharacter("c");
		int cCount = glyphFactory.getAndIncreaseCount();
		Character a2 = glyphFactory.getCharacter("a");
		int a2Count = glyphFactory.getAndIncreaseCount();

		Row row = new Row(a1Count);
		row.add(a1);
		row.add(b);
		row.add(c);
		row.add(a2);

		GlyphContext redContext = new GlyphContext();
		redContext.addColor(a1, "red",a1Count);
		redContext.addColor(b, "red",bCount);
		redContext.addColor(c, "red",cCount);
		redContext.addColor(a2, "green",a2Count);

		row.traversal(redContext);

	}
}
