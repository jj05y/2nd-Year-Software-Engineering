/*
 * Joe Duffin
 * Assignment One, 
 * Pool.java
 */
package Ass1;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * contains methods for creating and displaying tiles
 * 
 * @author joeduffin
 * @version 0.1
 * @since 03/02/15
 * 
 */
public class Tile {

	/**
	 * score - letter - keeps track of score/letter for each tile created
	 * label - img - used when displaying tiles through gui
	 */
	private int score;
	private char letter;
	private JLabel label;
	private ImageIcon img;
	private int row;
	private int col;

	/**
	 * @param s - the score value of this tile
	 * @param l - the letter value of this tile
	 */
	public Tile(int s, char l) {
		score = s;
		letter = l;
		label = new JLabel();
		label.setSize(40, 40);
		if (letter != '[') {
			img = new ImageIcon(getClass().getResource("/GUI/pics/" + new Character(letter).toString().toLowerCase() + ".png"));
		} else {
			img = new ImageIcon(getClass().getResource("/GUI/pics/blank.png"));
		}
		label.setIcon(img);
	}

	/**
	 * @return the score value of this tile is returned
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score - the value to set the tiles score with
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * @return - returns the letter value of this tile
	 */
	public char getLetter() {
		return letter;
	}

	/**
	 * @param letter - the letter to
	 */
	public void setLetter(char letter) {
		this.letter = letter;
	}

	/**
	 * @return - returns a string representation of this tile
	 */
	public String toString() {
		return new String("Letter: " + letter + "\tScore: " + score);
	}
	
	public JLabel getLabel(){
		return label;
		
	}
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

}
