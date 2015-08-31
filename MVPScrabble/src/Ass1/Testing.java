/*
 * Testing.java
 */
package Ass1;

import Ass2.Consts;

/**
 * 
 * Every method in this class returns a boolean indicating if the two passed
 * values are the same
 *
 */
public class Testing {
	
	protected static int numTests = 0;

	protected static boolean check(String s1, String s2) {
		System.out.println("Actual result: " + s1 + "\nExpected result: " + s2);
		numTests++;
		return (s1.compareTo(s2) == Consts.ZERO ? true : false);
	}

	protected static boolean check(Boolean b1, Boolean b2) {
		System.out.println("Actual result: " + b1 + "\nExpected result: " + b2);
		numTests++;
		return b1 == b2;
	}

	protected static boolean check(int i1, int i2) {
		System.out.println("Actual result: " + i1 + "\nExpected result: " + i2);
		numTests++;
		return i1 == i2;
	}

	protected static boolean check(char c1, char c2) {
		System.out.println("Actual result: " + c1 + "\nExpected result: " + c2);
		numTests++;
		return c1 == c2;
	}

	protected static boolean check(Tile t1, Tile t2) {
		System.out.println("Actual result: " + t1 + "\nExpected result: " + t2);
		numTests++;
		return t1 == t2;
	}

	protected static boolean check(int i1, int i2, int i3) {
		System.out.println("Actual result 1: " + i1 + "\nActual result 2: "
				+ i2 + "\nExpected result: " + i3);
		numTests++;
		return i1 == i2 && i2 == i3;
	}

	protected static boolean check(Frame f1, Frame f2) {
		System.out.println("Frame 1: " + f1 + "\nFrame 2: " + f2);
		numTests++;
		return f1 == f2;
	}
	
	protected static boolean check(int i1) {
		System.out.println("Actual result: " + i1 + "\nExpected result: > 0");
		numTests++;
		return i1 > 0;
	}

}
