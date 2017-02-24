package com.juhezi.cerberus


/**
 * Created by qiao1 on 2017/2/24.
 */
class Temp {

    @Subscribe(tag = 10)
    fun test(message: String) {    //17
        println(message)
    }

    fun hello(tag: Int) {}  //17

    private fun priHello() {}   //18

    protected fun proHello() {} //20

    internal fun interHello() {}    //17

    companion object {
        fun staticHello() {}
    }

    init {
        Cerberus.getDefault().register(this as Object)
    }

}

fun main(args: Array<String>) {
    Temp()
}
