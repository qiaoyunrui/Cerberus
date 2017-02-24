package com.juhezi.cerberus

/**
 * Created by qiao1 on 2017/2/24.
 */
internal class Subscription(val subscriber: Any, val subscriberMethod: SubscriberMethod) {

    var active: Boolean = false

    init {
        active = false
    }
}
