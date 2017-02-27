package com.juhezi.cerberus;

/**
 * Created by qiao1 on 2017/2/27.
 */
public class Main {

    public Main() {
        Cerberus.Companion.getDefault().register(this);
    }

    @Subscribe
    public void saySomething(String string) {
        System.out.println(string);
    }

    /**
     * 基本数据类型一定装箱
     *
     * @param num
     */
    @Subscribe
    public void inc(Integer num) {
        System.out.println(num);
    }


    public static void main(String[] args) {

    }

}
