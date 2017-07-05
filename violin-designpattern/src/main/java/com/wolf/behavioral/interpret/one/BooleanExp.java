package com.wolf.behavioral.interpret.one;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/13
 */
public interface BooleanExp {

    public boolean evaluate(Context context);

    /**
     * 替换功能，遇到表达式时，终结节点拷贝或者new，其他节点都进行new
     *
     * @param target
     * @param replaceExp
     * @return
     */
    public BooleanExp replace(String target, BooleanExp replaceExp);

    public BooleanExp copy();
}
