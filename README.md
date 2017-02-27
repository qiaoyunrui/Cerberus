# Cerberus
A Single-Thread EventBus

## Add Cerberus to your project

add the jar to your project.
[Click to download the jar](jar/Cerberus.jar)

## Start

1. Prepare subscribers,Declare and annotate your subscribing method

```java
@Subscribe
public void sayHello(String message) {
    System.out.println(message);
}
```

2. Register your subscriber

In Java
```Java
Cerberus.Companion.getDefault().register(this);
```
    
In Kotlin
```kotlin
Cerberus.getDefault().register(this)
```
    
3. Post Event
   
In Java
```java
Cerberus.Companion.getDefault().post("HelloWorld");
```
   
In Kotlin
```kotlin
Cerberus.getDefault().post("HelloWorld")
```

## Attention

The event in Cerberus dont't support the `int`、`float`... in java,but support `Integer`、`String`、`Float`...
And it don't support the `Int`、`Float` in Kotlin.
 
[中文介绍](README_ZH.md)
