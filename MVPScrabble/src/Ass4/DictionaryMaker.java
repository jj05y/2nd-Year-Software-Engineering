package Ass4;

import java.util.ArrayList;
/**
 * 
 * @author MVP
 * 
 * This class uses a instantiates a thread which will read and populate the dictionary.
 *
 */
public class DictionaryMaker {

	private ArrayList<String> dictionary;
	@SuppressWarnings("unused")
	private Thread myThread;

	public DictionaryMaker() {
		dictionary = new ArrayList<String>();
		myThread = new Thread(new ThreadedFileReader(dictionary), "myThread");

		

	}

	public ArrayList<String> getDictionary() {
		return dictionary;
	}

	public void SetDictionary(ArrayList<String> d) {
		dictionary = d;
	}

}
