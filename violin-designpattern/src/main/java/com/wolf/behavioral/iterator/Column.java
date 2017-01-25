package com.wolf.behavioral.iterator;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/30
 * Time: 9:15
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Column extends AbstractGlyph {

	private int count;
	private Glyph[] glyphs = new Glyph[2];

	public Column(String name) {
		super(name);
	}

	@Override
	public MyIterator createIterator(IteratorType iteratorType) {
		MyIterator myIterator = null;
		if (iteratorType == IteratorType.COMMON) {
			//下面代码是由于原先栈中iterator和column中使用的一个glyphs引用，会导致glyphs为空
			// 原先使用的Glyph中含有removeAllChildren和removeChild方法(后来想不对，栈中操作就应该操作它里面的
			// 数据结构不应该操作这里的数据结构,所以方法移到了iterator中)
//			if (null == glyphs) {
//				myIterator = new NullIterator();
//			} else {
				myIterator = new ArrayIterator(glyphs);
//			}
		} else if (iteratorType == IteratorType.PRE_ORDER) {
			myIterator = new PreOrderIterator(this);
		}
		return myIterator;
	}

	@Override
	public void addChild(Glyph glyph) {
		glyphs[count++] = glyph;
	}
}
