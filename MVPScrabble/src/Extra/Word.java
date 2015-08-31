package Extra;

public class Word implements Comparable<Word> {
	private int score;
	private String word;
	private String sig;

	public Word(String w) {
		word = w;
		score = 0;
	}

	/**
	 * Allows the use of Collections.sort();
	 */
	public int compareTo(Word w) {
		if (this.sig.compareTo(w.getSig()) < 0) {
			return -1;
		} else if (this.sig.compareTo(w.getSig()) > 0) {
			return 1;
		} else {
			return 0;
		}
	}
	
	/**
	 *  @return - The words signature, word and score is returned
	 */
	public String toString() {
		return (sig +" "+ word +" "+ score);
	}
	
	/*
	 * 
	 * GETTERS AND SETTERS
	 * 
	 */
	
	public int getScore() {
		return score;
	}

	public void setScore(int n) {
		this.score += n;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String w) {
		this.word = w;
	}

	public String getSig() {
		return sig;
	}

	public void setSig(String s) {
		this.sig = s;
	}






}
