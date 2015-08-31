/*
 * Edwin Keville
 * Assignment One, 
 * Frame.java
 */

package Ass1;

import java.util.ArrayList;

import Ass2.Consts;

/**
 * Creates and interacts with the frame of tiles each player has
 * 
 * @author edwinkeville
 * @version 0.1
 * @since 03/02/15
 * 
 */

public class Frame {

	/**
	 * maxTileinFrame - predefined total number of tiles allowed
	 * frame - creates a new frame
	 * pool - allows access to the pool from which the frames are filled
	 */
	private int maxTileinFrame;
	private ArrayList<Tile> frame;
	private Pool pool;

	/**
	 * @param pool - frame is filled with tiles from pool
	 */
	public Frame(Pool p) {
		maxTileinFrame = Consts.FRAME_SIZE;
		frame = new ArrayList<Tile>();
		pool = p;
		fillFrame();

	}

	/**
	 * 
	 * @param pool - fills frame from pool
	 * @return 0 - returns -1 if pool is empty and cannot fully fill
	 * 
	 */
	public int fillFrame() {
		while (frame.size() < maxTileinFrame) {
			if (pool.isEmpty()) {
				return Consts.POOL_EMPTY;
			}
			frame.add(pool.getRandTile());
		}
		return Consts.SUCCESS;
	}

	/**
	 * 
	 * @return - returns true if frame is empty. false if anything else.
	 */
	public boolean isEmpty() {
		return !(frame.size() > Consts.ZERO);
	}

	/**
	 * 
	 * @return - returns contents of the frame as a string.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < frame.size(); i++) {
			sb.append("|" + frame.get(i).getLetter() + " " + frame.get(i).getScore() + "|");
		}
		return new String(sb + "\n");
	}

	/**
	 * 
	 * @param i - index of tile to remove
	 * @return returns the removed tile.
	 */
	public Tile removeTile(int i) {
		return frame.remove(i);
	}

	/**
	 * returns all current tiles to the pool and fills a new frame.
	 */
	public void renewFrame() {
		while (!frame.isEmpty()) {
			pool.getTiles().add(frame.remove(0));
		}
		fillFrame();
	}
	
	/**
	 * 
	 * @param letters - takes letters to return to pool
	 * @return error or 1
	 */
	public int renewSelectTiles(String letters){
		ArrayList<Tile> tempF = new ArrayList<Tile>();
		
		tempF = checkFrameForWord(letters);
		
		if(tempF != null){
			for(int i=0; i<letters.length();i++){
				pool.getTiles().add(tempF.remove(0));
			}
		    return Consts.SUCCESS;
		}else{
			return Consts.LETTERS_NOT_IN_FRAME;
		}
	}

	/**
	 * 
	 * @return - returns this frame
	 */
	public Frame getFrame() {
		return this;
	}

	public ArrayList<Tile> getTiles() {
		return frame;
	}

	/**
	 * takes letter from the user and searches for it in the frame
	 * 
	 * @param c
	 * @return - true returns index of letter. or will return false
	 */
	public int letterSearch(char c) {
		for (int i = 0; i < frame.size(); i++) {
			if (c == frame.get(i).getLetter()) {
				return i;
			}
		}
		return Consts.NOT_FOUND;
	}

	/**
	 * @return - returns current quantity of tiles
	 */
	public int getCurrentFrameSize() {
		return frame.size();
	}

	/**
	 * .
	 * 
	 * @param word - lets user know if the word they are choosing is playable
	 *            from their frame
	 * @return array list - returns if word is there. null if not
	 */
	public ArrayList<Tile> checkFrameForWord(String word) {
		ArrayList<Tile> tempFrame = new ArrayList<Tile>();
		boolean isFound;
		for (int i = 0; i < word.length(); i++) {
			isFound = false;
			for (int j = 0; j < frame.size(); j++) {
				if (word.charAt(i) == frame.get(j).getLetter()) {
					tempFrame.add(frame.remove(j));
					isFound = true;
					break;
				}
			}
			if (!isFound) {
				for (int k = 0; k < frame.size(); k++) {
					if (frame.get(k).getLetter() == '_') {
						frame.get(k).setLetter(word.toUpperCase().charAt(i));
						tempFrame.add(frame.remove(k));
						isFound = true;
						break;
					}
				}
			}
		}

		if (tempFrame.size() == word.length()) {
			fillFrame();
			return tempFrame;
		} else {
			int tempFrameSize = tempFrame.size();
			for (int i = 0; i < tempFrameSize; i++) {
				frame.add(tempFrame.remove(0));
			}
			return Consts.WORD_NOT_IN_FRAME;
		}
	}

	/**
	 * testing frame. will use particular word to test pool and frame usage.
	 * 
	 * @param word
	 */
	public void buildTestFrame(String word) {
		// PRE CONDITION, YOU MUST NOT ABUSE THIS FUNCTION, ITS VUNERABLE AND
		// FOR TESTING PURPOSES ONLY, ALWAYS RESET POOL BEFORE CALLING
		frame.clear();
		for (int i = 0; i < word.length(); i++) {
			for (int j = 0; j < pool.getNumTiles(); j++) {
				if (word.charAt(i) == pool.getSpecificTile(j).getLetter()) {
					frame.add(pool.removeSpecificTile(j));
					break;
				}
			}
		}

		fillFrame();
	}

	/**
	 * calculates score of tiles remaining in frame
	 */
	public int scoreInFrame() {
		int frameScore = 0;

		for (int i = 0; i < frame.size(); i++) {
			frameScore += frame.get(i).getScore();
		}
		return frameScore;
	}

}
