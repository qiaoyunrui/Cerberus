package com.juhezi.cerberus

import java.lang.reflect.Method

/**
 * Created by qiao1 on 2017/2/24.
 */
class SubscriberMethod(internal val method: Method, internal val eventType: Class<*>) {
    internal var methodString: String? = null
}
