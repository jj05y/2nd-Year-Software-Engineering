package Ass3;

import java.util.ArrayList;

import Ass1.Player;
import Ass1.Pool;
import Ass1.Tile;
import Ass2.Board;
import Ass2.Consts;
import Ass2.Square;
import Ass4.DictionaryMaker;
import Ass4.Searcher;

/**
 * scrabble creates the game ie: here is where the board is interacted with
 * through each players turn
 * 
 * @author MVP
 * @version 0.1
 * @since 02/03/15
 */

public class Scrabble {

	/**
	 * board - creates new instance of the board searcher - used for searching
	 * dictionary for words prePlayedWords - shows words previously played on
	 * the board tilesToNullify - shows which tiles will no longer have a
	 * multiplier
	 */

	private Searcher searcher;
	private Board board;
	private ArrayList<Turn> prePlayedWords;
	private ArrayList<Integer> tilesToNullify;
	private LastTurn lastTurn;

	/**
	 * Initializes a bland board and allocates score multipliers and characters
	 * to special squares Also instantiates a searcher to search the dictionary
	 */
	public Scrabble() {

		prePlayedWords = new ArrayList<Turn>();
		tilesToNullify = new ArrayList<Integer>();
		board = new Board();
		searcher = new Searcher((new DictionaryMaker().getDictionary()));
		lastTurn = new LastTurn();
	}

	/**
	 * @param word
	 * @param i - the row index of the start of the word
	 * @param j - the column index of the start of the word
	 * @param dir - the direction of the word to be played
	 * @param p - player playing the word
	 * @return - returns the score for the played word or appropriate error
	 */
	public int go(String word, int i, int j, int dir, Player p) {
		// System.out.println("PREPLAYED:");
		// for (int k = 0; k < prePlayedWords.size(); k++) {
		// System.out.println("pre played: " + prePlayedWords.get(k));
		// }
		lastTurn.reset();
		System.out.println();
		tilesToNullify.clear();
		ArrayList<Tile> wordTiles;
		String newWord;
		int score;
		if (!alreadyBeenPlayed(new Turn(word, i, j, dir))) {
			// if (searcher.find(word) != -1) { //auto dictionary check
			if ((newWord = checkForMove(word, i, j, dir)) != null) {
				if ((wordTiles = p.getFrame().checkFrameForWord(newWord)) != null) {
					if ((score = placeWord(wordTiles, i, j, dir, word)) != Consts.ADJACENT_WORD_NOT_VALID) {
						board.nullifyTiles(tilesToNullify);
						lastTurn.setScoreRecievedOnLastTurn(score);
						return score;
					} else {
						return Consts.ADJACENT_WORD_NOT_VALID;
					}
				} else {
					return Consts.NOT_IN_FRAME;
				}
			} else {
				return Consts.MOVE_NOT_POSSIBLE;
			}
			// } else {
			// return Consts.NOT_IN_DICTIONARY;
			// }
		} else {
			return Consts.ALREADY_PLAYED;
		}
	}

	private boolean alreadyBeenPlayed(Turn turn) {
		for (int k = 0; k < prePlayedWords.size(); k++) {
			if (prePlayedWords.get(k).sameAs(turn)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Currently this only returns the score of the word played, not score of
	 * words created, eg, adjacent words
	 * 
	 * @param i - the row index of the start of the word
	 * @param j - the column index of the start of the word
	 * @param dir - direction that word is going
	 * @return - returns the score yielded by the word
	 */
	private int computeScore(String word, int row, int col, int dir) {

		int score = Consts.ZERO;
		int wordMult = Consts.ONE;
		int i = row;
		int j = col;
		int adjScores = 0;

		while (board.getBoard()[i][j].getTile() != null) {
			score += board.getBoard()[i][j].getTile().getScore() * board.getBoard()[i][j].getLetterMultiplier();
			wordMult *= board.getBoard()[i][j].getWordMultiplier();

			if (board.getBoard()[i][j].getLetterMultiplier() != 1 || board.getBoard()[i][j].getWordMultiplier() != 1) {
				tilesToNullify.add(i);
				tilesToNullify.add(j);
			}

			if (dir == Consts.DOWN) {
				i++;
			} else {
				j++;
			}
			// if we hit the edge of the board
			if (i >= Consts.BOARD_SIZE || j >= Consts.BOARD_SIZE) {
				break;
			}

		}

		// when word is null, it means that we are computing an adjacent word
		// score
		if (word == null) {
			return score * wordMult;
		} else if ((adjScores = lookForAdjacentWords(row, col, dir)) == Consts.ADJACENT_WORD_NOT_VALID) {
			return Consts.ADJACENT_WORD_NOT_VALID;
		} else {
			return (score * wordMult) + adjScores;
		}

	}

	private int lookForAdjacentWords(int row, int col, int dir) {
		// now i need to look at the word entered, and see whats up,

		int i = row;
		int j = col;
		int adjWordDir = 0;
		int adjWordScores = 0;
		int indexForAdjTileMove;
		String word;
		int i2, j2;
		Turn tempTurn = null;
		int adjWordStartI;
		int adjWordStartJ;

		while (board.getBoard()[i][j].getTile() != null) { // for each letter,

			word = "";

			// if first, look up down n left,
			if (i == col && j == row) {
				indexForAdjTileMove = isThereAnAdjacantTile(i, j, dir, Consts.FIRST_LETTER);
				// the next line,,, if it hits an edge OR has a null tile after
				// it
			} else if (((i + (dir == Consts.DOWN ? 1 : 0)) >= Consts.BOARD_SIZE || (j + (dir == Consts.ACROSS ? 1 : 0)) >= Consts.BOARD_SIZE)
					|| board.getBoard()[i + (dir == Consts.DOWN ? 1 : 0)][j + (dir == Consts.ACROSS ? 1 : 0)].getTile() == null) {
				// if last look up, down n right
				indexForAdjTileMove = isThereAnAdjacantTile(i, j, dir, Consts.LAST_LETTER);
			} else {
				indexForAdjTileMove = isThereAnAdjacantTile(i, j, dir, Consts.MIDDLE_LETTER);
			}

			// if an adjacent word is found by one of the above
			// System.out.println("foo :" + indexForAdjTileMove);
			if (indexForAdjTileMove >= 0 && indexForAdjTileMove < 4) {

				// work out direction of adjacent word
				if (indexForAdjTileMove == 1 || indexForAdjTileMove == 3) {
					adjWordDir = Consts.ACROSS;
				}
				if (indexForAdjTileMove == 0 || indexForAdjTileMove == 2) {
					adjWordDir = Consts.DOWN;
				}

				// find start of that word
				i2 = i;
				j2 = j;
				// back up until we hit the start of the word OR the edge
				while ((i2 - (adjWordDir == Consts.DOWN ? 1 : 0) >= Consts.ZERO && j2 - (adjWordDir == Consts.ACROSS ? 1 : 0) >= Consts.ZERO)
						&& board.getBoard()[i2 - (adjWordDir == Consts.DOWN ? 1 : 0)][j2 - (adjWordDir == Consts.ACROSS ? 1 : 0)].getTile() != null) {
					// increment
					if (adjWordDir == Consts.DOWN) {
						i2--;
					} else {
						j2--;
					}

				}

				adjWordStartI = i2;
				adjWordStartJ = j2;

				// System.out.println("Start and dir of adjacent word (0=down): "
				// + i2 + "," + j2 + " " + adjWordDir);

				// NOW build it into a string:
				while (board.getBoard()[i2][j2].getTile() != null) {
					word += board.getBoard()[i2][j2].getTile().getLetter();
					if (adjWordDir == Consts.DOWN) {
						i2++;
					} else {
						j2++;
					}
					// if we hit the edge of the board
					if (i2 >= Consts.BOARD_SIZE || j2 >= Consts.BOARD_SIZE) {
						break;
					}
				}
				// System.out.println("The adjacent word found is: " + word);
				// check if its in dictionary
				// System.out.println("The word searching dictionary for is: " +
				// word);
				// if (searcher.find(word) < 0) {
				// return Consts.ADJACENT_WORD_NOT_VALID;
				// }
				// if good

				// check if that word has been played already
				for (int k = 0; k < prePlayedWords.size(); k++) {
					tempTurn = new Turn(word, adjWordStartI, adjWordStartJ, adjWordDir);
					// System.out.println("looking for " + tempTurn +
					// " in prePlayedWords");
					if (prePlayedWords.get(k).sameAs(tempTurn)) {
						tempTurn = null;
						// System.out.println(word +
						// " has already been played, dont care, size of premade words is "
						// + prePlayedWords.size());
						break;
					}
				}

				// handling findings
				if (tempTurn != null) {
					prePlayedWords.add(tempTurn);
					lastTurn.addToWordsCreatedLastTurn(tempTurn);
					// System.out.println("added " + tempTurn +
					// " to prePlayedWords");
					adjWordScores += computeScore(null, i, j, adjWordDir);
					// System.out.println("adjWordScors: " + adjWordScores);
				}

			}
			// increment to next letter of originally played word
			if (dir == Consts.DOWN) {
				i++;
			} else {
				j++;
			}
			// if we hit the edge of the board
			if (i >= Consts.BOARD_SIZE || j >= Consts.BOARD_SIZE) {
				break;
			}
		} // end of while loop that checks each letter or oringally played
			// letter.

		return adjWordScores;

	}

	/**
	 * @param word - the word attempted to be played
	 * @param startI - the row index of the start of the word
	 * @param startJ - the column index of the start of the word
	 * @param dir - direction that word is going
	 * @return
	 */
	private String checkForMove(String word, int startI, int startJ, int dir) {

		StringBuilder sb = new StringBuilder();
		boolean golden = false;

		int i = startI, j = startJ;
		for (int k = 0; k < word.length(); k++) {
			if (i > 14 || i < 0 || j > 14 || j < 0) {
				return Consts.OUT_OF_BOUNDS;
			}
			if (board.getBoard()[i][j].getTile() != null) {
				golden = true;
			} else if (i == 7 && j == 7 || (isThereAnAdjacantTile(i, j, Consts.NA, Consts.ALL_LETTERS) >= 0)) {
				golden = true;
				sb.append(word.charAt(k));
			} else {
				sb.append(word.charAt(k));
			}

			if (dir == Consts.DOWN) {
				i++;
			} else {
				j++;
			}

		}

		i = startI;
		j = startJ;

		for (int k = 0; k < word.length(); k++) {
			if (board.getBoard()[i][j].getTile() != null) {
				if (board.getBoard()[i][j].getTile().getLetter() != word.charAt(k)) {
					golden = false;
				}
			}
			if (dir == Consts.DOWN) {
				i++;
			} else {
				j++;
			}
		}

		if (!golden) {
			return null;
		} else {
			// System.out.println(sb);
			return new String(sb);
		}

	}

	/**
	 * @param i - the i index of the tile being checked
	 * @param j - the j index of the tile being checked
	 * @return - returns index of move if there is an adjacent tile, -1
	 *         otherwise
	 */
	private int isThereAnAdjacantTile(int i, int j, int dir, int option) {

		int[][] moves = { { -1, 1, 1, -1, 0 }, { 0, 1, -1, -1, 1 } };
		boolean flag = false;

		for (int k = 0; k < moves[0].length; k++) {
			flag = false;

			switch (option) {

			case Consts.FIRST_LETTER:
				if (k == 1 && dir == Consts.ACROSS)
					flag = true;
				if (k == 2 && dir == Consts.DOWN)
					flag = true;
				break;

			case Consts.MIDDLE_LETTER:
				if ((k == 1 || k == 3) && dir == Consts.ACROSS)
					flag = true;
				if ((k == 0 || k == 2) && dir == Consts.DOWN)
					flag = true;
				break;

			case Consts.LAST_LETTER:
				if (k == 3 && dir == Consts.ACROSS)
					flag = true;
				if (k == 0 && dir == Consts.DOWN)
					flag = true;
				break;

			default:
				break;
			}

			i += moves[0][k];
			j += moves[1][k];

			if (!flag) {
				if (i <= 14 && i >= 0 && j <= 14 && j >= 0) {
					if (board.getBoard()[i][j].getTile() != null) {
						// System.out.println("k i'm sending back is " + k);
						return k;
					}
				}
			}
		}
		return -1;
	}

	/**
	 * Resets the board to initial state
	 */
	public void reset() {

		prePlayedWords.clear();
		board.reset();
	}

	/**
	 * @param wordTiles - An array list of tiles to be put on the board
	 * @param i - the i index of the start of the word
	 * @param j - the j index of the start of the word
	 * @param dir - the direction the word is going
	 */
	public int placeWord(ArrayList<Tile> wordTiles, int i, int j, int dir, String word) {
		int row = i;
		int col = j;
		for (int k = 0; k < wordTiles.size(); k++) {
			if (board.getBoard()[i][j].getTile() == null) {
				lastTurn.addToTilesThatCameFromFrame(wordTiles.get(k));
				board.getBoard()[i][j].setTile(wordTiles.get(k));
				board.getBoard()[i][j].getTile().setRow(i);
				board.getBoard()[i][j].getTile().setCol(j);
			} else {
				k--;
			}
			// System.out.println(board[i][j].getTile().getLetter());
			if (dir == Consts.DOWN) {
				i++;
			} else {
				j++;
			}
		}

		int score;
		if ((score = computeScore(word, row, col, dir)) != Consts.ADJACENT_WORD_NOT_VALID) {
			prePlayedWords.add(new Turn(word, row, col, dir));
			lastTurn.addToWordsCreatedLastTurn(new Turn(word, row, col, dir));

			// System.out.println("added " + word + " to prePlayedWords");
			return score;
		} else {
			return Consts.ADJACENT_WORD_NOT_VALID;
		}
	}

	/**
	 * @return - returns a String representation of the board
	 */
	public String toString() {
		return board.toString();

	}

	/**
	 * @return - the 2D array of squares is returned.
	 */
	public Square[][] getSquares() {
		return board.getBoard();
	}

	/* trying out new method.. may delete after */
	public Square getSquare(int row, int column) {
		return board.getBoard()[row][column];
	}

	/**
	 * 
	 * @param lastPlayer - this is the player who's turn is being challenged,
	 *            (not the challenger)
	 * @param pool - the pool the game is using
	 * @return - returns 0 if challenged last played word in in dictionary, -1 if word is not in dictionary.s
	 */
	public int challenge(Player lastPlayer, Pool pool) {

		// iterating through last played words created, checking if they're all
		// in dictionary
		for (Turn t : lastTurn.getWordsCreatedLastTurn()) {
			if (searcher.find(t.getWord()) == Consts.CANT_FIND_WORD_IN_DICT) {
				undoLastTurn(lastPlayer, pool);
				return Consts.CANT_FIND_WORD_IN_DICT;
			}
		}

		return Consts.FOUND_WORD_IN_DICT;
	}

	/**
	 * 
	 * @param lastPlayer - this is the player who's turn is being challenged,
	 *            (not the challenger)
	 * @param pool - the pool the game is using
	 */
	private void undoLastTurn(Player lastPlayer, Pool pool) {

		// 1. Reset multipliers - GERARD
		for (Tile t : lastTurn.getTilesThatCameFromFrame()) {
			board.getBoard()[t.getRow()][t.getCol()].setLetterMultiplier(board.getBoard()[t.getRow()][t.getCol()].getDefaultLetterMulti());
			board.getBoard()[t.getRow()][t.getCol()].setWordMultiplier(board.getBoard()[t.getRow()][t.getCol()].getDefaultWordMulti());
		}

		// 2 & 3 - Joe & Ed
		while(!lastTurn.getTilesThatCameFromFrame().isEmpty()){
			//take the letters last added to player frame and pop em back in to the pool (working with the index of the last tile
			pool.getTiles().add(lastPlayer.getFrame().getTiles().remove(lastPlayer.getFrame().getTiles().size() - 1 ));

			//blank the board
			board.getBoard()[lastTurn.getTilesThatCameFromFrame().get(0).getRow()][lastTurn.getTilesThatCameFromFrame().get(0).getCol()].setTile(null);
			
			//put tiles back in frame
			lastPlayer.getFrame().getTiles().add(lastTurn.getTilesThatCameFromFrame().remove(0));		
		}
		
		// 4. remove words from preplayedwords array list - ED
		for (int i = 0; i<lastTurn.wordsCreatedLastTurn.size();i++){
			prePlayedWords.remove(prePlayedWords.size()-1);
		}
		
		// 5. deduct score from lastplayer - ED
		lastPlayer.incrScore(-lastTurn.scoreRecievedOnLastTurn);
		
	}

	public LastTurn getLastTurn() {
		return lastTurn;
	}
	
	/**
	 * 
	 *  This inner class is mostly getters and setters,
	 *  It contains the information needed to undo the previous turn.
	 *
	 */
	public class LastTurn {

		private ArrayList<Tile> tilesThatCameFromFrame;
		private int scoreRecievedOnLastTurn;
		private ArrayList<Turn> wordsCreatedLastTurn;

		public LastTurn() {
			tilesThatCameFromFrame = new ArrayList<Tile>();
			wordsCreatedLastTurn = new ArrayList<Turn>();
		}

		public void reset() {
			tilesThatCameFromFrame.clear();
			wordsCreatedLastTurn.clear();
			scoreRecievedOnLastTurn = 0;
		}

		public ArrayList<Tile> getTilesThatCameFromFrame() {
			return tilesThatCameFromFrame;
		}

		public void addToTilesThatCameFromFrame(Tile t) {
			tilesThatCameFromFrame.add(t);
		}

		public int getScoreRecievedOnLastTurn() {
			return scoreRecievedOnLastTurn;
		}

		public void setScoreRecievedOnLastTurn(int scoreRecievedOnLastTurn) {
			this.scoreRecievedOnLastTurn = scoreRecievedOnLastTurn;
		}

		public ArrayList<Turn> getWordsCreatedLastTurn() {
			return wordsCreatedLastTurn;
		}

		public void addToWordsCreatedLastTurn(Turn t) {
			wordsCreatedLastTurn.add(t);
		}

		public String toString() {
			return ("\nLast Turn Info:\nTiles: " + tilesThatCameFromFrame + "\nWords: " + wordsCreatedLastTurn + "\nScore: "
					+ scoreRecievedOnLastTurn + "\n");
		}

	}



}
