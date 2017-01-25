package com.wolf.behavioral.iterator;

import com.wolf.utils.ObjectCopyUtils;

import java.util.Stack;

/**
 * <p> Description:前序遍历,保证用户端的代码while(first,!isDone,next)不变
 * <p/>
 * Date: 2016/6/30
 * Time: 10:10
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class PreOrderIterator extends AbstractIterator {

	private Stack<MyIterator> stack = new Stack<MyIterator>();
	private int currentLevel;
	protected Glyph glyph;

	public PreOrderIterator(Glyph glyph) {
		try {
			//不破坏原来结构，便于其他iterator重复只用原值，或者不影响调用者
			this.glyph = ObjectCopyUtils.copyByStream(Glyph.class, glyph);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void moveFirst() {
		currentLevel = 1;
		MyIterator iterator = glyph.createIterator(IteratorType.COMMON);
		//叶子节点，就无意义了
		if (!iterator.isDone()) {
			iterator.moveFirst();
			stack.push(iterator);
		}
	}

	@Override
	public Glyph getCurrentItem() {
		//防止空指针
		if (!stack.isEmpty()) {
			return stack.peek().getCurrentItem();
		}
		return null;
	}

	/**
	 * The Next operation gets the top iterator on the stack and asks its current item
	 to create an iterator, in an effort to descend the glyph structure as far as possible
	 (this is a preorder traversal, after all). Next sets the new iterator to the first
	 item in the traversal and pushes it on the stack. Then Next tests the latest iterator;
	 if its IsDone operation returns true, then we've finished traversing the
	 current subtree (or leaf) in the traversal. In that case, Next pops the top iterator
	 off the stack and repeats this process until it findsthe next incomplete traversal,
	 if there is one; if not, then we have finished traversing the structure.

	 void PreorderIterator::Next () {
	 Iterator<Glyph*>* i = _iterators.Top()->CurrentItem()->CreateIterator();
	 i->First();
	 _iterators.Push(i);
	 while ( _iterators.Size() > 0 && _iterators.Top()->IsDone() ) {
	 delete _iterators.Pop();
	 _iterators.Top()->Next();
	 }
	 }
	 */
	@Override
	public void next() {
		MyIterator peek = stack.peek();
		Glyph currentItem = peek.getCurrentItem();
		MyIterator iterator = currentItem.createIterator(IteratorType.COMMON);
		if (!iterator.isDone()) {
			iterator.moveFirst();
			stack.push(iterator);
		} else {//已经到叶子节点了，树该向上走了
			peek.next();
			// 查看iterator如果已经迭代完了,就弹出当前iterator，以便下回使用者的getCurrentItem返回栈中新值,
			// 如此反复，直到栈为空或者上层iterator还有值
			while (null != peek && peek.isDone()) {
				stack.pop();
				peek = null;
				if (!stack.isEmpty()) {
					peek = stack.peek();
					peek.next();
				}
			}
		}
	}

	@Override
	public boolean isDone() {
		return stack.isEmpty();
	}

}
