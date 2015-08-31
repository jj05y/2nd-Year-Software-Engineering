package Extra;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SignerMaker {

	private String line;
	private int[] scores = { 1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10 };
	private String fName;
	private ArrayList<Word> dictionary;
	
	
	public SignerMaker(String fn) {
		dictionary = new ArrayList<Word>();
		fName = fn;
		readWords();
		signWords();
		Collections.sort(dictionary);
		scoreWords(); 		
	}
	
	public ArrayList<Word> getDictionary() {
		return dictionary;
	}

	private void readWords() {
		try {
			InputStream is = getClass().getResourceAsStream(fName);
			InputStreamReader reader = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(reader);
			line = br.readLine();
			while (line != null) {
				dictionary.add(new Word(line));
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			System.out.println("File not found!");
			e.printStackTrace();
		}
	}

	private void signWords() {
		for (int i = 0; i < dictionary.size(); i++) {
			char[] chars = dictionary.get(i).getWord().toCharArray();
			Arrays.sort(chars);
			dictionary.get(i).setSig(new String(chars));
			
		}
	}

	private void scoreWords() {
		for (int i = 0; i < dictionary.size(); i++) {
			for (int j = 0; j < dictionary.get(i).getWord().length(); j++) {
				if ((int)dictionary.get(i).getWord().charAt(j) <= 122 && (int)dictionary.get(i).getWord().charAt(j) >= 97) {
					dictionary.get(i).setScore(scores[((int)dictionary.get(i).getWord().charAt(j))-97]);
				}
			}
		}
	}


}
