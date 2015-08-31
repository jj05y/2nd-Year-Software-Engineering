/*
 * Gerard Fogarty
 * Assignment One, 
 * Player.java
 */

package Ass1;

import Ass2.Consts;

/**
 * contains methods associated with each player
 * 
 * @author gerardfogarty
 * @version 0.1
 * @since 03/02/15
 * 
 */

public class Player implements Comparable<Player>{
	
	/**
	 * name - the name of each player
	 * score - players current score
	 * frame - creates new instance of frame
	 */
	private String name;
	private int score;
	private Frame frame;

	/**
	 * Score is initialised to zero
	 * 
	 * @param n
	 *            - the name of the player
	 * @param f
	 *            - the frame that the player will use
	 */
	public Player(String n, Frame f) {
		score = Consts.ZERO;
		frame = f;
		name = n;
	}

	/**
	 * @param n
	 *            - the new name for this player
	 * @param f
	 *            - the new frame for this player
	 */
	public void reset(String n, Frame f) {
		score = Consts.ZERO;
		frame = f;
		name = n;
	}

	/**
	 * @return - the frame of this player is returned
	 */
	public Frame getFrame() {
		return frame;
	}

	/**
	 * @return - the name of this player is returned
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param s
	 *            - the amount to set the current score at
	 */
	public void setScore(int s) {
		score = s;
	}

	/**
	 * @return - this players score is returned
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @return - Returns a string representation of the player
	 */
	public String toString() {
		return new String("Player name: " + name + "\n" + "Current score : "
				+ score + "\n" + frame);

	}

	/**
	 * @param n
	 *            - the amount to increment the score by
	 * @return - the incremented score is returned
	 */
	public int incrScore(int n) {
		score += n;
		return score;

	}

	public int compareTo(Player p) {
		
		if(this.score<p.score){
			return 1;
		}else if(this.score>p.score){
			return -1;
		}else{
		return 0;
		}
	}
}
