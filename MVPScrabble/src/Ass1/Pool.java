/*
 * Joe Duffin
 * Assignment One, 
 * Pool.java
 */

package Ass1;

import java.util.ArrayList;
import java.util.Random;

import Ass2.Consts;

/**
 * creates the pool of tiles from which tiles are pulled
 * 
 * @author joeduffin
 * @version 0.1
 * @since 03/02/15
 * 
 */
public class Pool {

	
	/**
	 * scores - quantities - array of numbers for the score for each tile and the ammount of each tile
	 * tiles - creates the arraylist of tiles
	 * rand - random function to ensure pool is shuffled
	 */
	private final static int[] scores = { 1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1,
			3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10, 0 };
	private final static int[] quantities = { 9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2,
			6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1, 2 };
	private ArrayList<Tile> tiles;
	private static Random rand;

	/**
	 * Instantiates and array list of tiles, and instantiates a random object
	 */
	public Pool() {
		tiles = new ArrayList<Tile>();
		setTiles();
		rand = new Random();
	}

	/**
	 * void method to assign a letter and score to each tile
	 */
	private void setTiles() {
		char l = Consts.A;
		for (int i = 0; i < scores.length; i++) {
			for (int j = 0; j < quantities[i]; j++) {
				tiles.add(new Tile(scores[i], l));
			}
			l++;
		}
		tiles.get(tiles.size() - 1).setLetter('_');
		tiles.get(tiles.size() - 2).setLetter('_');

	}

	/** 
	 *  refills the pool with a brand new set of tile
	 * @return returns the reset pool 
	 */
	public Pool reset() {
		tiles.clear();
		setTiles();
		return this;
	}

	/**
	 *  @return - a random tile is returned from the pool
	 */
	public Tile getRandTile() {
		if (!isEmpty()) {
			return tiles.remove(rand.nextInt(tiles.size()));
		} else {
			return null;
		}
	}

	/**
	 * @return true if pool is empty, false if not
	 */
	public boolean isEmpty() {
		return tiles.isEmpty();
	}

	/**
	 * @return - returns number of tiles in pool
	 */
	public int getNumTiles() {
		return tiles.size();
	}
	
	/**
	 * @return - returns the ArrayList of tiles
	 */
	public ArrayList<Tile> getTiles() {
		return tiles;
	}
	
	/**
	 * @param - the rand of the specific tile to be returned (not removed from pool)
	 * @return - the tile at rank i is returned, null if not present
	 */
	public Tile getSpecificTile(int i) {
		return tiles.get(i);
	}
	
	/**
	 * @param i - the rank of tile to remove,
	 * @return - the removed tile is returned
	 */
	public Tile removeSpecificTile(int i) {
		return tiles.remove(i);
	}

	/**
	 * @return - a string representation of the tile pool
	 */
	public String toString() {
		if (isEmpty()) return "POOL EMPTY";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tiles.size(); i++) {
			sb.append(tiles.get(i) + "\n");
		}
		return new String(sb);
	}
	
	/**
	 * should remove 96 tiles and leave just 4 (for testin purposes only)
	 */
	public void zap96Tiles() {
		while (tiles.size() > 4) {
			tiles.remove(0);
		}
	}
}
