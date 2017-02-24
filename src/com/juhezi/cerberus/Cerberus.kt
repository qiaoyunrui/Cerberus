package com.juhezi.cerberus

import java.util.*

/**
 * Created by qiao1 on 2017/2/24.
 */
class Cerberus internal constructor(builder: CerberusBuilder = Cerberus.DEFAULT_BUILDER) {

    private object Holder {
        val sInstance = Cerberus()
    }

    companion object {

        private val TAG = Cerberus::class.simpleName

        private var defaultInstane: Cerberus? = null

        private val DEFAULT_BUILDER = CerberusBuilder()

        fun getDefault(): Cerberus = Holder.sInstance

        fun builder(): CerberusBuilder = CerberusBuilder()

    }

    private val subscriberMethodFinder = SubscriberMethodFinder()

    init {
        //把Builder中的数据转移到Cerberus中
    }

    fun register(subscriber: Object) {
        var subscriberClass = subscriber.`class`
        var subscriberMethods = subscriberMethodFinder.findSubscriberMethods(subscriberClass)
    }

    fun unregister(subscriber: Object) {

    }

    fun subscribe() {

    }

    fun post(event: Object) {

    }
}