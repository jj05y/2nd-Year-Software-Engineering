package util;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class MoreComprehensiveBot implements Robot{

	private Word word;
	private String letters;
	private Permer permer;
	private ArrayList<PotentialWord> potentialWords;
	private Dictionary dict;
	private boolean challengeMade;
	private double startTime;
	private ArrayList<Double> times;
	private double time;
	private ArrayList<String> personality;
	private Random rand;

	public static final int UP_DOWN = 1;
	public static final int LEFT_RIGHT = 2;
	public static final int UP = 3;
	public static final int RIGHT = 4;
	public static final int DOWN = 5;
	public static final int LEFT = 6;
	public static final int FAIL = -1;
	public static final char BLANK = ' ';
	private static final int CENTER = 7;

	/**
	 *  instantiation!
	 */
	MoreComprehensiveBot() {
		letters = "XYZ";
		permer = new Permer();
		potentialWords = new ArrayList<PotentialWord>();
		challengeMade = false;
		times = new ArrayList<Double>();
		// tempWords = new ArrayList<String>();
		personality = getPersonality();
		rand = new Random();
		dict = null;
	}

	
	@SuppressWarnings("static-access")
	/*
	 *	This method populates the potentialWords array list,
	 *	It looks for letters on the board, determines the 'play type' (intersection, suffix, prefix)
	 *  Permutes loads of words, and any playable words are put in contention to be played.
	 *  The word with highest score is played. 
	 *  
	 *  There are secondary elements to this method that will challenge a false word, handle the first go, etc
	 *  
	 */
	public int getCommand(Player player, Board board, Dictionary dictionary) {
		if (dict == null) {
			dict = dictionary;
		}
		startTime = System.currentTimeMillis();
		
		System.out.println("\n\n" + personality.get(rand.nextInt(personality.size())) + "\n");

		int playType;
		int tempStartRow;
		int tempStartCol;
		char tempChar;
		Word tempWord;

		int row;
		int col;
		int startRow;
		int startCol;
		String foo;
		String wordb;

		if (!challengeMade) {
			challengeMade = true;

			try {
				for (String s : board.getWords()) {
					if (!isAWord(s)) {
						return (UI.COMMAND_CHALLENGE);
					}
				}
			} catch (NullPointerException e) {
				// do nothing
			}
		}

		challengeMade = false;

		if (board.getSqContents(CENTER, CENTER) == BLANK) {

			System.out.println("/n first timer yo /n");
			// center square is empty
			for (String s : getPerms(player, '$')) {
				if (board.checkWord(tempWord = new Word(CENTER, CENTER, Word.HORIZONTAL, s), player.frame) == UI.WORD_OK) {
					if (isAWord(s)) {
						potentialWords.add(new PotentialWord(CENTER, CENTER, Word.HORIZONTAL, s, board.getTotalWordScore(tempWord)));
					}
				}
			}
		}

		// find letters
		for (int intersectionRow = 0; intersectionRow < board.SIZE; intersectionRow++) {
			for (int instersectionCol = 0; instersectionCol < board.SIZE; instersectionCol++) {
				if ((tempChar = board.getSqContents(intersectionRow, instersectionCol)) != BLANK
						&& !middleOfanInstersection(intersectionRow, instersectionCol, board)) {
					switch (playType = lookForPlayType(intersectionRow, instersectionCol, board)) {

					case LEFT_RIGHT:
					case UP_DOWN:
						for (String s : getPerms(player, tempChar)) {
							for (int i = 0; i < s.length(); i++) {
								if (s.charAt(i) == tempChar) {
									// back up i spaces
									tempStartRow = intersectionRow - (playType == LEFT_RIGHT ? 0 : i);
									tempStartCol = instersectionCol - (playType == LEFT_RIGHT ? i : 0);
									if (board.checkWord(tempWord = new Word(tempStartRow, tempStartCol,
											(playType == LEFT_RIGHT ? Word.HORIZONTAL : Word.VERTICAL), s), player.frame) == UI.WORD_OK) {
										if (checkForBumps(tempWord, intersectionRow, instersectionCol, tempStartRow, tempStartCol, board)) {
											potentialWords.add(new PotentialWord(tempStartRow, tempStartCol,
													(playType == LEFT_RIGHT ? Word.HORIZONTAL : Word.VERTICAL), s, board
															.getTotalWordScore(tempWord)));
										}
									}

								}
							}
						}
						break;

					case DOWN:
					case RIGHT:
						// its a suffix
						row = intersectionRow;
						col = instersectionCol;
						wordb = "";
						foo = "";
						try {
							while (board.getSqContents(row - (playType == DOWN ? 1 : 0), col - (playType == DOWN ? 0 : 1)) != BLANK) {
								if (playType == DOWN) {
									row--;
								} else {
									col--;
								}
							}
						} catch (ArrayIndexOutOfBoundsException e) {
							// do nothing
						}
						startRow = row;
						startCol = col;
						while ((playType == DOWN ? row : col) != (playType == DOWN ? intersectionRow : instersectionCol) + 1) {
							wordb += board.getSqContents(row, col);
							if (playType == DOWN) {
								row++;
							} else {
								col++;
							}
						}
						// make perms from frame,
						for (String s : getPerms(player, '$')) {
							foo = wordb;
							foo += s;
							if (isAWord(foo)) {
								if (board.checkWord(tempWord = new Word(startRow, startCol, (playType == DOWN ? Word.VERTICAL
										: Word.HORIZONTAL), foo), player.frame) == UI.WORD_OK) {
									if (checkForBumps(tempWord, intersectionRow, instersectionCol, startRow, startCol, board)) {
										potentialWords.add(new PotentialWord(startRow, startCol, (playType == DOWN ? Word.VERTICAL
												: Word.HORIZONTAL), foo, board.getTotalWordScore(tempWord)));
									}
								}
							}
						}
						// stick on end of word
						// check if its in the dict,
						// if so, add potentials

					case UP:
					case LEFT:
						// its a prefix

						startRow = intersectionRow;
						startCol = instersectionCol;
						col = startCol;
						row = startRow;
						wordb = "";
						foo = "";
						try {
							while (board.getSqContents(row, col) != BLANK) {
								wordb += board.getSqContents(row, col);
								if (playType == UP) {
									row++;
								} else {
									col++;
								}
							}
						} catch (ArrayIndexOutOfBoundsException e) {
							// do nothing
						}

						// make perms from frame,
						for (String s : getPerms(player, '$')) {
							foo = s;
							foo += wordb;
							if (isAWord(foo)) {
								if (board.checkWord(tempWord = new Word(startRow - s.length(), startCol, (playType == UP ? Word.VERTICAL
										: Word.HORIZONTAL), foo), player.frame) == UI.WORD_OK) {
									if (checkForBumps(tempWord, intersectionRow, instersectionCol, startRow - s.length(), startCol, board)) {
										potentialWords
												.add(new PotentialWord(startRow - s.length(), startCol, (playType == UP ? Word.VERTICAL
														: Word.HORIZONTAL), foo, board.getTotalWordScore(tempWord)));
									}
								}
							}
						}
						// stick on end of word
						// check if its in the dict,
						// if so, add potentials
						break;

					default:
						continue;
					}

				}
			}
		}
		// find the best potentialWord and play it
		if (potentialWords.isEmpty()) {

			return (UI.COMMAND_PASS);
		}
		PotentialWord best = potentialWords.get(0);

		for (PotentialWord p : potentialWords) {
			// System.out.println(p);
			if (p.isGreaterThan(best)) {
				best = p;
			}
		}

		word = best.toWord();
		potentialWords.clear();
		times.add(time = (System.currentTimeMillis() - startTime)/1000);
		System.out.println("Turn time: " + time + " seconds.");
		return (UI.COMMAND_PLAY);
	}

	/**
	 * 
	 * @param row - a row index
	 * @param col - a col index 
	 * @param board = the board
	 * @return - if the row/col is surrounded, (up, down, left, right) , true is returned, false otherwise
	 */
	private boolean middleOfanInstersection(int row, int col, Board board) {

		int[] urdlI = { -1, 0, +1, 0 }; // up right down left
		int[] urdlJ = { 0, +1, 0, -1 };

		// if one is not occupied,
		// retrun false;

		for (int k = 0; k < urdlI.length; k++) {
			try {
				if (board.getSqContents(row + urdlI[k], col + urdlJ[k]) == BLANK) {
					return false;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				// do nothing
			}
		}
		return true;
	}

	private boolean isAWord(String foo) {
		ArrayList<String> words = new ArrayList<String>();
		words.add(foo);
		return dict.areWords(words);

	}

	/**
	 * A bump is defined as a connection of a word with another
	 * When a tile bumps a pre played word, it is investigated,,, does it create a new word that is in the dictionary?
	 * 
	 * @param tempWord - the word which is being tested for bumping
	 * @param interRow - the index of tempWord that connects correctly
	 * @param interCol - the col index of tempWord that connects correctly
	 * @param startRow - the start row index of the word that is being tested
	 * @param startCol - the start col index of the word that is being tested
	 * @param board - the board
	 * @return - true if no bumps OR bump creates legitimate word, false is bump creates a non word.
	 */
	private boolean checkForBumps(Word tempWord, int interRow, int interCol, int startRow, int startCol, Board board) {
		int row = startRow;
		int col = startCol;
		int k = 0;
		int[] urdlI = { -1, 0, +1, 0 }; // up right down left
		int[] urdlJ = { 0, +1, 0, -1 };

		try {
			if (tempWord.isVertical()) {
				if (board.getSqContents(row + urdlI[0], col + urdlJ[0]) != BLANK) {
					return false;
				}
			} else {
				if (board.getSqContents(row + urdlI[3], col + urdlJ[3]) != BLANK) {
					return false;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// do nothing
		}

		// now we are at the start of the word,, and havn't bumped,
		for (k = 0; k < tempWord.getLength(); k++) {
			if (row == interRow && col == interCol) {
				if (tempWord.isVertical()) {
					row++;
				} else {
					col++;
				}
				k++;
				if (k >= tempWord.getLength()) {
					return true;
				}
			}
			try {
				if (tempWord.isVertical()) {
					// dir up down, look right
					if (board.getSqContents(row + urdlI[1], col + urdlJ[1]) != BLANK) {
						if (!checkAdjWord(row, col, Word.HORIZONTAL, board, tempWord.getLetters().charAt(k))) {
							return false;
						}
					}

				} else {
					// dir left right,, look up
					if (board.getSqContents(row + urdlI[0], col + urdlJ[0]) != BLANK) {
						if (!checkAdjWord(row, col, Word.VERTICAL, board, tempWord.getLetter(k))) {
							return false;
						}
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				// do nothing
			}

			try {
				if (tempWord.isVertical()) {
					// dir up down, look left
					if (board.getSqContents(row + urdlI[3], col + urdlJ[3]) != BLANK) {
						if (!checkAdjWord(row, col, Word.HORIZONTAL, board, tempWord.getLetter(k)))
							return false;
					}
				} else {
					// dir left right,, look down
					if (board.getSqContents(row + urdlI[2], col + urdlJ[2]) != BLANK) {
						if (!checkAdjWord(row, col, Word.VERTICAL, board, tempWord.getLetter(k))) {
							return false;
						}
					}
				}

			} catch (ArrayIndexOutOfBoundsException e) {
				// do nothing
			}

			if (tempWord.isVertical()) {
				row++;
			} else {
				col++;
			}
			try {
				// System.out.println("Row: " + row + "\tCol: " + col +
				// " \tValue: " + board.getSqContents(row, col));
			} catch (ArrayIndexOutOfBoundsException e) {
				// do nothing
			}
		}

		// now we are at the end of the word! :O
		try {
			if (board.getSqContents(row, col) != BLANK) {
				return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// do nothing
		}

		return true;
	}

	/**
	 * This method builds a string from the word created upon 'bumping'
	 * It is then checked in the dictionary
	 * 
	 * @param r - the row index of the bump
	 * @param c - the col index of the bump
	 * @param direction - the direction of the newly created word
	 * @param board - the board
	 * @param d - the character which creates the bump, but is not get on the board
	 * @return - true if created word is in the dictionary,,, false otherwise
	 */
	private boolean checkAdjWord(int r, int c, int direction, Board board, char d) {
		// return false;
		String s = "";
		int row = r;
		int col = c;
		// back up,
		// System.out.println("Found a bump, intersect is at row: " + r +
		// " col: " + c + " direction: " + direction);
		try {
			while (board.getSqContents(row, col) != BLANK || (row == r && col == c)) {
				if (direction == Word.HORIZONTAL) {
					col--;
				} else {
					row--;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// do nothing
		}

		if (direction == Word.HORIZONTAL) {
			col++;
		} else {
			row++;
		}
		// System.out.println("start of our bump is row: " + row + " col: " +
		// col);

		// build word,
		try {
			while (board.getSqContents(row, col) != BLANK || (row == r && col == c)) {
				if (row == r && col == c) {
					s += d;
				} else {
					s += board.getSqContents(row, col);
				}

				if (direction == Word.HORIZONTAL) {
					col++;
				} else {
					row++;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// do nothing
		}
		// System.out.println("Bump ends at row: " + row + " col: " + col);
		// check dict
		// System.out.println("Our Bump is " + s);
		// System.out.println("is it in the dictionary? " +
		// isAWord(s.toUpperCase()));
		return (isAWord(s.toUpperCase()));

	}

	/**
	 * @param i - the row index of the character on the board
	 * @param j - the j index of the character on the board
	 * @param board - the board
	 * @return - a constant indicating the potential for the passed coordinates, ie, if you can play an intersection, suffix, etc from that tile 
	 * 				FAIL (-1) is returned if that tile cannot be played from
	 */
	private int lookForPlayType(int i, int j, Board board) {
		int row = i;
		int col = j;
		int[] urdlI = { -1, 0, +1, 0 };
		int[] urdlJ = { 0, +1, 0, -1 };

		try {
			if (board.getSqContents(row + urdlI[0], col + urdlJ[0]) == ' ' && board.getSqContents(row + urdlI[2], col + urdlJ[2]) == ' ') {
				// then we have up down
				return UP_DOWN;

			}
		} catch (ArrayIndexOutOfBoundsException e) {

		}
		try {
			if (board.getSqContents(row + urdlI[1], col + urdlJ[1]) == ' ' && board.getSqContents(row + urdlI[3], col + urdlJ[3]) == ' ') {
				// then we have left right if (checkDiagonals(board, row, col))
				return LEFT_RIGHT;
			}

		} catch (ArrayIndexOutOfBoundsException e) {

		}

		try {
			if (board.getSqContents(row + urdlI[1], col + urdlJ[1]) == BLANK) {
				// then right free
				return RIGHT;

			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// nope
		}
		try {
			// now its not both,
			if (board.getSqContents(row + urdlI[0], col + urdlJ[0]) == ' ') {
				// then we have up free
				return UP;

			}

		} catch (ArrayIndexOutOfBoundsException e) {

		}
		try {
			if (board.getSqContents(row + urdlI[2], col + urdlJ[2]) == ' ') {
				// then down free
				return DOWN;

			}

		} catch (ArrayIndexOutOfBoundsException e) {

		}
		try {
			if (board.getSqContents(row + urdlI[3], col + urdlJ[3]) == ' ') {
				// then left free
				return LEFT;

			}

		} catch (ArrayIndexOutOfBoundsException e) {

		}

		return FAIL;
	}

	/**
	 * This method is the access to the Permer object, 
	 * If a blank tile is present, a loop is started to instert each of the 26 letters into the permuations
	 * 
	 * @param player - the players whose frame is to be permuted
	 * @param c - the character (from the board) which is to be appended to the letters from the frame
	 * @return - an array list of permutations is returned
	 */
	private ArrayList<String> getPerms(Player player, char c) {
		boolean foundABlank = false;
		String lettersToPermute = "";
		for (Tile t : player.frame.getAllTiles()) {
			if (t.isBlank()) {
				foundABlank = true;
			} else {
				lettersToPermute += t.getFace();
			}
		}
		if (c != '$')
			lettersToPermute += c;
		if (foundABlank) {
			ArrayList<String> permsWithBlankOption = new ArrayList<String>();
			String s;
			for (String wordWithoutBlank : (c == '$' ? permer.getAllPerms(lettersToPermute) : permer.getLegitPerms(lettersToPermute))) {
				permsWithBlankOption.add(wordWithoutBlank);
				for (char blankTile = 'a'; blankTile <= 'z'; blankTile++) {
					for (int i = 0; i <= wordWithoutBlank.length(); i++) {
						if (isAWord(s = new StringBuilder(wordWithoutBlank).insert(i, blankTile).toString())) {
							permsWithBlankOption.add(s);
						}
					}
				}
			}

			return permsWithBlankOption;
		} else {
			return (c == '$' ? permer.getAllPerms(lettersToPermute) : permer.getLegitPerms(lettersToPermute));
		}
	}

	/**
	 * Getters and Setters:
	 */
	public Word getWord() {
		// should not change
		return (word);
	}

	public String getLetters() {
		// should not change
		return (letters);
	}
	
	public ArrayList<Double> getTimes() {
		return times;
	}
	
	/**
	 * 
	 * @return - a personality is returned
	 */
	private ArrayList<String> getPersonality() {
		ArrayList<String> sents = new ArrayList<String>();
		Scanner in = new Scanner(getClass().getResourceAsStream("DontLookInHere"));
		while (in.hasNext()) {
			sents.add(in.nextLine());
		}
		in.close();
		return sents;
	}


}
