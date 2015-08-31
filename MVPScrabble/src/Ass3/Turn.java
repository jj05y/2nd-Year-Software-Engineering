package Ass3;

/**
* methods involving each players turn in the game
* 
* @author MVP
* @version 0.1
* @since 02/03/15
* 
*/

public class Turn {
	
	/**
	 * i - j translate to the grid co-ordinates when placing a word on the board
	 * dir - dictates which direction the word will go (across or down)
	 * word - word entered by user
	 */
	private String word;
	private int i;
	private int j;
	private int dir;

	public Turn(String word, int i, int j, int dir) {
		this.word = word;
		this.i = i;
		this.j = j;
		this.dir = dir;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public int getDir() {
		return dir;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}

	public boolean sameAs(Turn t) {
		return ((word.equals(t.word) && i == t.getI() && j == t.getJ() && dir == t.getDir()) ? true : false);
	}
	public String toString() {
		return (word + " " + i + "," + j + " " + dir);
	}

}
