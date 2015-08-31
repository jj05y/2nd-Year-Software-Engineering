package Ass4;

import java.util.ArrayList;

/**
 * 
 * @author MVP
 *	
 *	This class has a single responsibility. It searches the dictionary, (word list) using a recursive binary search.
 *
 */
public class Searcher {

	ArrayList<String> wordList;
	String lookingFor;
	
	public Searcher(ArrayList<String> l) {
		wordList = l;

		
	}
	/**
	 * 
	 * @param s - the word being searched for
	 * @return - the index in wordList of the word, or -1 if not found
	 */
	public int find(String s) {
		lookingFor = s;
		return search(0, wordList.size()-1);
	}
	
	/**
	 * 
	 * @param lower -  the lower bound of the list to search
	 * @param upper - the upper bound of the list to search
	 * @return - the index of the word in the list, OR, -1 if not found
	 */
	private int search(int lower, int upper) {
		
		if (lower > upper) {
			return  -1;
		}

		int middle = (lower+upper)/2;
		
		if (lookingFor.compareTo(wordList.get(middle)) < 0) {
			return search(lower, middle-1);
		} else if ((lookingFor.compareTo(wordList.get(middle)) > 0)) {
			return search(middle+1, upper);
		} else {
			return middle;
		}
		
	}

}
