package com.lfyun.test;

public class Main {

	public static void main(String[] args) {
		Class<?> clazz = Test.deduceMainApplicationClass();
		System.out.println(clazz.getName());

	}
}
