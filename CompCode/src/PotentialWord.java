
/**
 * 
 * This class is used to to create potential words,
 * A potential word is one which has passed all the checks and will
 * be played if its score is high enough
 *
 */
public class PotentialWord{
	
	private String word;
	private int direction;
	private int row;
	private int column;
	private int score;
	
	
	public PotentialWord(int tempRow, int tempCol, int vERTICAL, String s,
			int wordScore) {
	direction = vERTICAL;
	row = tempRow;
	column = tempCol;
	score = wordScore;
	word = s;
	}
	
	public Word toWord(){
		return new Word(row,column, direction, word);
	}
	
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}

	public String toString() {
		return ("Potential Word: " + word + " row: " + row + " col: " + column + " dir: " + direction + " score: " + score);
	}


	public boolean isGreaterThan(PotentialWord w) {
		if(this.score > w.getScore()) return true;

		return false;
	}
	
	
	
	

}
