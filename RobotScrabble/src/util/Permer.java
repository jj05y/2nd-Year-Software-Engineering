package util;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Permer {

	ArrayList<String> perms;
	Dictionary dict;
	boolean all;

	/**
	 * this constructor instantiates the global fields for this class
	 */
	public Permer() {
		all = false;
		perms = new ArrayList<String>();
		try {
			dict = new Dictionary();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param str - the string to  permute
	 * @return - An ArrayList of permutations of the given string which are legitimate scrabble words
	 */
	public ArrayList<String> getLegitPerms(String str) {
		perms.clear();
		char[] arr = str.toCharArray();

		for (int shift = 0; shift < str.length() + 1; shift++) {

			for (int size = 0; size < str.length() + 1; ++size) {
				boolean[] visited = new boolean[size];
				for (int i = 0; i < size; i++)
					visited[i] = false;
				char[] branch2 = new char[size];
				generatePermutations(arr, size, branch2, -1, visited);
			}
			arr = shift(arr);

		}
		return perms;
	}

	/**
	 * @param letters - the string to permute
	 * @return - returns an array list of all permutations of a given string
	 */
	public ArrayList<String> getAllPerms(String letters) {
		all = true;
		getLegitPerms(letters);
		all = false;
		return perms;
	}

	/** 
	 * 
	 * @param c this method cycles (shifts) an array one index to the side.
	 * @return - the shifted array is returned
	 */
	public char[] shift(char[] c) {
		char[] temp = new char[c.length];
		try {
			System.arraycopy(c, 1, temp, 0, c.length - 1);
		} catch (ArrayIndexOutOfBoundsException e) {
			return c;
		}
		temp[c.length - 1] = c[0];
		return temp;
	}

	/**
	 * 
	 * @param arr - the array original array of characters made from the string
	 * @param size - the size of the permutation we want from the array
	 * @param branch - the array which is getting a char inserted into it before another recursive call
	 * @param level - the level is used as the stopping condition,,, when the recursive calls are deep enough, (the permuted word is complete), the word is added to an array list
	 * @param visited - this boolean array is used to stop duplicate branches
	 */
	public void generatePermutations(char[] arr, int size, char[] branch, int level, boolean[] visited) {
		if (level >= size - 1) {
			if (all) {
				checkAddWithOUTDictionary(new String(branch));
			} else {
				checkAddWithDictionary(new String(branch));
			}
			// System.out.println("Added: " + new String(branch));
			return;
		}

		for (int i = 0; i < size; i++) {
			if (!visited[i]) {
				branch[++level] = arr[i];
				visited[i] = true;
				generatePermutations(arr, size, branch, level, visited);
				visited[i] = false;
				level--;
			}
		}
	}

	/**
	 * @param string - if this string is in the dictionary, and is not a duplicate, it is added to the perms arraylist.
	 */
	private void checkAddWithDictionary(String string) {
		for (String s : perms) {
			if (string.equals(s))
				return;
		}
		ArrayList<String> foo = new ArrayList<String>();
		String ss = new String(string);

		foo.add(ss.toUpperCase());
		if (dict.areWords(foo)) {
			perms.add(string);
		}
	}

	/**
	 * @param string - this string is added to the array list of perms if it is not a duplicate 
	 */
	private void checkAddWithOUTDictionary(String string) {
		for (String s : perms) {
			if (string.equals(s))
				return;
		}

		perms.add(string);
	}

}
