package com.wolf.behavioral.interpret.two;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/14
 * Time: 14:29
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Client {

	public static void main(String[] args) {
		//(a*b)/(a-b+2)
		VariableExp a = new VariableExp("a");
		VariableExp b = new VariableExp("b");

		MultipleExp multipleExp = new MultipleExp(a, b);

		SubtractExp subtractExp = new SubtractExp(a, b);
		AddExp addExp = new AddExp(subtractExp, new ConstantExp(2));

		DivisionExp divisionExp = new DivisionExp(multipleExp, addExp);

		Context context = new Context();
		context.assign("a",7);
		context.assign("b",8);

		Integer compute = divisionExp.compute(context);
		System.out.println(compute);

	}
}
