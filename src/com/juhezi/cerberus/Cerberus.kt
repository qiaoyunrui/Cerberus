package com.juhezi.cerberus

import sun.rmi.runtime.Log
import java.lang.reflect.InvocationTargetException
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

        private val eventTypesCache = HashMap<Class<*>, MutableList<Subscription>>()    //缓存

        fun getDefault(): Cerberus = Holder.sInstance

        fun builder(): CerberusBuilder = CerberusBuilder()

    }

    private val subscriberMethodFinder = SubscriberMethodFinder()
    private val subscriptionsByEventType = HashMap<Class<*>, MutableList<Subscription>>()
    private val typesBySubscriber = HashMap<Any, MutableList<Class<*>>>()
    private val currentPostingThreadState = object : ThreadLocal<PostingThreadState>() {
        override fun initialValue(): PostingThreadState {
            return PostingThreadState()
        }
    }

    init {
        //把Builder中的数据转移到Cerberus中
    }

    fun register(subscriber: Any) {
        var subscriberClass = subscriber.javaClass
        var subscriberMethods = subscriberMethodFinder.findSubscriberMethods(subscriberClass)
        subscriberMethods.forEach { subscribe(subscriber, it) }
    }

    fun unregister(subscriber: Any) {
        var subscribedTypes = typesBySubscriber[subscriber]
        if (subscribedTypes != null) {
            subscribedTypes.forEach { unsubscribeByEventType(subscriber, it) }
            typesBySubscriber.remove(subscriber)
        } else {
            throw Exception("Subscriber to unregister was not registered before: ${subscriber.javaClass}")
        }
    }

    private fun subscribe(subscriber: Any, subscriberMethod: SubscriberMethod) {
        var eventType = subscriberMethod.eventType
        var newSubscription = Subscription(subscriber, subscriberMethod)
        var subscriptions = subscriptionsByEventType[eventType]
        if (subscriptions == null) {
            subscriptions = ArrayList<Subscription>()
            subscriptionsByEventType.put(eventType, subscriptions)
        } else {
            if (subscriptions.contains(newSubscription)) {
                throw Exception("Subscriber ${subscriber.javaClass} already registered to event " + eventType)
            }
        }

        subscriptions.add(newSubscription)

        var subscriberedEvents = typesBySubscriber[subscriber]  //获取事件类型
        if (subscriberedEvents == null) {
            subscriberedEvents = ArrayList<Class<*>>()
            typesBySubscriber.put(subscriber, subscriberedEvents)
        }
        subscriberedEvents.add(eventType)
    }

    private fun unsubscribeByEventType(subscriber: Any, eventType: Class<*>) {
        var subscriptions = subscriptionsByEventType[eventType]
        if (subscriptions != null) {
            var iterator = subscriptions.iterator()
            iterator.forEach {
                if (it.subscriber == subscriber) {
                    it.active = false
                    iterator.remove()
                }
            }
        }
    }

    fun post(event: Any) {
        var postingState = currentPostingThreadState.get()
        var eventQueue = postingState.eventQueue
        eventQueue.add(event)
        if (!postingState.isPosting) {  //开始发送事件
            postingState.isPosting = true
            if (postingState.canceled) {
                throw Exception("Internal error. Abort state was not reset")
            }
            try {
                while (!eventQueue.isEmpty()) {
                    println()
                    postSingleEvent(eventQueue.removeAt(0), postingState)
                }
            } finally {
                postingState.isPosting = false
            }
        }
    }


    private fun postSingleEvent(event: Any, postingThreadState: PostingThreadState) {
        var eventClass = event.javaClass
        var subscriptionFound = postSingleEventForEventType(event, postingThreadState, eventClass)
        if (!subscriptionFound) {
            println("The subscription not found!")
        }
    }

    private fun postSingleEventForEventType(event: Any, postingThreadState: PostingThreadState,
                                            eventType: Class<*>): Boolean {
        var subscriptions = subscriptionsByEventType[eventType]
        if (subscriptions != null && !subscriptions.isEmpty()) {
            subscriptions.forEach {
                postingThreadState.event = event
                postingThreadState.subscription = it
                var aborted: Boolean
                try {
                    postToSubscription(it, event)
                    aborted = postingThreadState.canceled
                } finally {
                    postingThreadState.event = null
                    postingThreadState.subscription = null
                    postingThreadState.canceled = false
                }
                if (aborted) {
                    return@forEach
                }
            }
            return true
        }
        return false
    }

    private fun postToSubscription(subscription: Subscription, event: Any) {
        invokeSubscriber(subscription, event)
    }

    private fun invokeSubscriber(subscription: Subscription, event: Any) {
        try {
            subscription.subscriberMethod.method.invoke(subscription.subscriber, event)
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }

    internal class PostingThreadState {
        val eventQueue = ArrayList<Any>()
        var isPosting = false
        var canceled = false
        var subscription: Subscription? = null
        var event: Any? = null
    }

}