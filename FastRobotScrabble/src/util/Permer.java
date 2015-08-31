package util;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Permer {

	ArrayList<String> perms;
	Dictionary dict;
	boolean all;

	public Permer() {
		all = false;
		perms = new ArrayList<String>();
		try {
			dict = new Dictionary();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

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

	public ArrayList<String> getAllPerms(String letters) {
		all = true;
		getLegitPerms(letters);
		all = false;
		return perms;
	}

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

	private void checkAddWithOUTDictionary(String string) {
		for (String s : perms) {
			if (string.equals(s))
				return;
		}

		perms.add(string);
	}

}
