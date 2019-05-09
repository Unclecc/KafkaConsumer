package com.chenwen.action;


import java.util.Iterator;

/**
 *
 * 消费者动作抽象类
 *
 */
public interface IAction {

    /**
     * 处理动作
     */
    public Object doAction(Iterator iterator);

}
