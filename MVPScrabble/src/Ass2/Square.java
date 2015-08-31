package Ass2;

import Ass1.Tile;

/**
* methods defining each individual square on the game board
* 
* @author MVP
* @version 0.1
* @since 18/02/15
* 
*/
public class Square {
	
	/**
	 * wordMulti - letterMultiplier - whether square has multiplier or not
	 * tile - allows square to hold a tile
	 * displayChar - string to visually show the letter present in the square
	 */
	private Tile tile;
	private int letterMultiplier;
	private int wordMulti;
	private int defaultWordMulti;
	private int defaultLetterMulti;

	private String displayChar;
	
	/**
	 * Initializes every thing to a default value
	 */
	public Square() {
		tile = null;
		letterMultiplier = Consts.SINGLE;
		wordMulti = Consts.SINGLE;
		defaultWordMulti = Consts.SINGLE;
		defaultLetterMulti = Consts.SINGLE;
		displayChar = Consts.BLANK_SQUARE;
	}

	/*
	 * GETTERS AND SETTERS 
	 */
	
	public Tile getTile() {
		return tile;
	}

	public int getDefaultWordMulti() {
		return defaultWordMulti;
	}

	public void setDefaultWordMulti(int defaultWordMulti) {
		this.defaultWordMulti = defaultWordMulti;
	}

	public int getDefaultLetterMulti() {
		return defaultLetterMulti;
	}

	public void setDefaultLetterMulti(int defaultLetterMulti) {
		this.defaultLetterMulti = defaultLetterMulti;
	}

	public void setTile(Tile t) {
		this.tile = t;
	}

	public int getLetterMultiplier() {
		return letterMultiplier;
	}

	public void setLetterMultiplier(int letterMultiplier) {
		this.letterMultiplier = letterMultiplier;
	}
	
	public int getWordMultiplier() {
		return wordMulti;
	}


	public void setWordMultiplier(int wordMulti) {
		this.wordMulti = wordMulti;
	}
	public String getDisplayChar() {
		return displayChar;
	}

	public void setDisplayChar(String displayChar) {
		this.displayChar = displayChar;
	}
	
	
}
