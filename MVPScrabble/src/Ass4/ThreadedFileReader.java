package Ass4;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import Ass2.Consts;
/**
 * 
 * @author MVP
 * 
 * This class is Runnable, and therefore executed in its own thread.
 *
 */
public class ThreadedFileReader implements Runnable {

	private ArrayList<String> dictionary;

	public ThreadedFileReader(ArrayList<String> d) {
		dictionary = d;
		readWords();
		Collections.sort(dictionary); //just incase the dictionary isn't sorted
		
	}

	/**
	 * This method opens the dictionary, and populates the dictionary with the words
	 */
	private void readWords() {
		String line;
		try {
			InputStream is = getClass().getResourceAsStream(Consts.DICTIONARY_FILE);
			InputStreamReader reader = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(reader);
			line = br.readLine();
			while (line != null) {
				dictionary.add(line);
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			System.out.println("File not found!");
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Override
	public void run() {

	}
}