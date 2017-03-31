package com.lianjia.test;

public class CollectionTest {

	
	public static void main(String[] args) {
		Box<String> name = new Box<String>("corn");
		Box<Integer> age = new Box<Integer>(712);
		Box<Number> number = new Box<Number>(314);
		
		System.out.println("name class:" + name.getClass());      // com.qqyumidi.Box
		System.out.println("age class:" + age.getClass());        // com.qqyumidi.Box
		System.out.println(name.getClass() == age.getClass());    // true
		
        getData(name);
        getData(age);
        getData(number);
	}

    public static void getData(Box<?> data) {
    	//Box<?>不需要提前声明， Box<T>需要提前声明
    	//?表示位置类型，不是
    	//?此处是类型实参，而不是类型形参！且Box<?>在逻辑上是Box<Integer>、Box<Number>...等所有Box<具体类型实参>的父类
    	//Class<T>在实例化的时候，T要替换成具体类
    	//Class<?>它是个通配泛型，?可以代表任何类型
        System.out.println("data :" + data.getData());
    }	
}

class Box<T> {

    private T data;

    public Box() {

    }

    public Box(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

}