package com.wolf.behavioral.interpret.one;

/**
 * <b>功能</b>many kinds of operations can "interpret" a sentence
 *
 * Given a language, define a representation for its grammar along with an
 * interpreter that uses the representation to interpret sentences in the language.
 *
 * If a particular kind of problem occurs often enough, then it might be
 * worthwhile to express instances of the problem as sentences in a simple
 * language. Then you can build an interpreter that solves the problem by
 * interpreting these sentences.
 *
 * The Interpreter pattern describes how to define a grammar for simple
 * languages, represent sentences in the language, and interpret these sentences.
 *
 * You don't have to define the other Interpret operation in the expression subClasses.
 * For example, a grammar for a programming language will have many operations on abstract syntax trees,
 * such as as type-checking, optimization, code generation, and so on. It will
 * be more likely to use a visitor to avoid defining these operations one very grammar class.
 *
 *
 * BooleanExp ::= VariableExp | Constant | OrExp | AndExp | NotExp |
 * '(' BooleanExp ')'
 * AndExp ::= BooleanExp 'and' BooleanExp
 * OrExp ::= BooleanExp 'or' BooleanExp
 * NotExp ::= 'not' BooleanExp
 * Constant ::= 'true' | 'false'
 * VariableExp ::= 'A' | 'B' | ... | 'X' | 'Y' | 'Z'
 *
 * @author 李超
 * @Date 2016/7/13
 */
public class Client {

    public static void main(String[] args) {
        //define the Boolean expression use interpret pattern
        // (true and x) or (y and (not x))

        ConstantExp constantExp = new ConstantExp(true);
        VariableExp x = new VariableExp("x");
        AndExp andExp1 = new AndExp(constantExp, x);

        VariableExp y = new VariableExp("y");
        //flyweight
        NotExp notExp = new NotExp(x);
        AndExp andExp2 = new AndExp(y, notExp);

        OrExp orExp = new OrExp(andExp1, andExp2);

        Context context = new Context();
        context.assign("x", false);
        context.assign("y", false);

        boolean evaluate = orExp.evaluate(context);
        System.out.println(evaluate);

        VariableExp z = new VariableExp("z");
        NotExp notExpZ = new NotExp(z);
        BooleanExp y1 = orExp.replace("y", notExpZ);

        context.assign("z", true);
        boolean evaluate1 = y1.evaluate(context);
        System.out.println(evaluate1);
    }
}
