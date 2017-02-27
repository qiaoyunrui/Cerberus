# Cerberus

一个单线程的Eventbus。

## 把Cerberus添加到你的项目里

在你项目中添加这个jar包作为依赖

## 如何使用

1. 准备订阅者，使用`@Subscribe`对方法（只能拥有一个参数）进行注解

```java
@Subscribe
public void sayHello(String message) {
    System.out.println(message);
}
```

2. 注册订阅者

Java
```Java
Cerberus.Companion.getDefault().register(this);
```
    
Kotlin
```kotlin
Cerberus.getDefault().register(this)
```

3. 发送事件

   
Java
```java
Cerberus.Companion.getDefault().post("HelloWorld");
```
   
Kotlin
```kotlin
Cerberus.getDefault().post("HelloWorld")
```

## 其他操作

### 取消订阅

Java
```Java
Cerberus.Companion.getDefault().unregister(this);
```
    
Kotlin
```kotlin
Cerberus.getDefault().unregister(this)
```



## 注意

Cerberus中发送的事件不支持Java中未装箱的基本数据类型，也不支持Kotlin中的基本数据类型。
