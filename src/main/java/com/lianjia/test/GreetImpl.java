package com.lianjia.test;

public class GreetImpl implements Greet {

    public void sayHello(String name)
    {
        System.out.println("Hello " + name);
    }
    public void goodBye()
    {
        System.out.println("Good bye.");
    }
}
