package util;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException; // Needs to be imported for Dictionary



public class JohnBot {

	private String letters;
	private Word word = new Word();

	private Word currWord = new Word();
	private Word maxWord = new Word();
	private Frame frame;
	private Frame copyFrame;
	private Frame tempFrame;
	private Board board;
	private Lexicon lexicon;
	private int currScore;
	private int maxScore;
	private int startRow;
	private int startCol;
	private int passCounter = 0;

	JohnBot () {
		word.setWord(Board.CENTRE, Board.CENTRE, Word.HORIZONTAL, "");
		letters = "";
	}

	public int getCommand (Player player, Board board, Dictionary dictionary) throws FileNotFoundException {
		this.frame = player.getFrame();
		this.board = board;
		this.lexicon = new Lexicon();
		this.maxWord.setWord(Board.CENTRE, Board.CENTRE, Word.HORIZONTAL, "");
		this.currWord.setWord(Board.CENTRE, Board.CENTRE, Word.HORIZONTAL, "");
		ArrayList<Tile> tiles = player.getFrame().getAllTiles(); 	

		// Try and find a word
		genWord(Word.HORIZONTAL);
		genWord(Word.VERTICAL);

		// Detect if a new word was found this turn, and play it
		System.out.println("\n-----------------------------");
		if (!maxWord.getLetters().equals("")) {
			System.out.println("Playing word!");
			word = maxWord;
			return UI.COMMAND_PLAY;
		}

		// Don't keep trying if nothing is found
		if (passCounter >= 1) {
			passCounter = 0;
			letters = "";

			for (Tile tile : tiles) {
				if (tile.getFace() != '*') //can't replace blank (causes problems)
					letters += Character.toString(tile.getFace()); 
			}
			return UI.COMMAND_EXCHANGE;
		}

		passCounter++;
		return UI.COMMAND_PASS;

	}


	/** 
	 * Algorithm inspired by
	 * http://www.cs.cmu.edu/afs/cs/academic/class/15451-s06/www/lectures/scrabble.pdf
	 */

	/**
	 * Goes though each square on the board (unless it is the first play) and calls
	 * the extendLeftOrUp method to try and find words.
	 * 
	 * @param orientation either horizontal or vertical
	 */
	private void genWord(int orientation) {
		if (board.isFirstPlay()) {
			extendLeftOrUp(Board.CENTRE, Board.CENTRE, orientation);
		}
		else {
			for (int row = 0; row < Board.SIZE; row++) {
				for (int col = 0; col < Board.SIZE; col++) {
					if (board.getSqContents(row, col) == Board.EMPTY) {
						extendLeftOrUp(row, col, orientation);
					}
				}
			}
		}
	}

	
	/**
	 * Finds a good starting point for the word by going either left or
	 * up (depending on what it's told to do). It looks for the last 
	 * square before the next empty square which may be empty or contain
	 * another tile.
	 * 
	 * @param row the row to start from
	 * @param col the column to start from
	 * @param orientation which way to go: either horizontal or vertical
	 */
	private void extendLeftOrUp(int row, int col, int orientation) {
		int start = 0;
		Node n = lexicon.root;
		tempFrame = new Frame(frame);

		//need to move back one before we start checking
		if (orientation == Word.VERTICAL)	start = row--;
		else	start = col--;

		while ( !((row <= 0) && (orientation == Word.VERTICAL)) &&
				!((col <= 0) && (orientation == Word.HORIZONTAL)) ) {
			if (board.getSqContents(row,col) == Board.EMPTY)
				break;
			else {
				if (orientation == Word.VERTICAL)	start = row--;
				else	start = col--;
			}
		}

		if (orientation == Word.VERTICAL){
			startRow = start;
			startCol = col;
		}
		else {
			startRow = row;
			startCol = start;
		}
		
		moveRightOrDown(startRow, startCol, n, "", orientation);
	}

	
	/**
	 * This function goes either right or down and tries to build a word with
	 * what it already has from the previous call. If the square on the board 
	 * is blank, it uses a tile from the frame, otherwise it uses what is already 
	 * on the board. For each new tile, it checks in the dictionary to see if it
	 * is worth trying the next tile.
	 * 
	 * @param row the current row
	 * @param col the current column
	 * @param n the current node in the lexicon
	 * @param prefix what letters have been stored so far
	 * @param orientation either up or down
	 */
	private void moveRightOrDown(int row, int col, Node n, String prefix, int orientation) {
		char currentLetter;

		if (board.getSqContents(row, col) != Board.EMPTY) {
			currentLetter = Character.toUpperCase(board.getSqContents(row, col));	
		}
		else {
			if (!tempFrame.isEmpty()) { 
				currentLetter = tempFrame.getTile(0).getFace();
				tempFrame.removeAt(0);
			}
			else
				return;
		}

		if (currentLetter == '*') { //for the moment just ignore blanks
			moveRightOrDown(row, col, n, prefix, orientation);
		}
		else if (n.isChild(currentLetter) && //there are more possible letters after this so keep going
				(((col+1 < 15) && (orientation == Word.HORIZONTAL)) || 
				((row+1 < 15) && (orientation == Word.VERTICAL)))) {
			n = n.getChild(currentLetter);
			prefix += currentLetter;
			if (orientation == Word.VERTICAL) {
				moveRightOrDown(row+1, col, n, prefix, orientation);
			}
			else {
				moveRightOrDown(row, col+1, n, prefix, orientation);
			}
		}
		else if (n.isEndOfWord()) { //we have found a complete word, make sure it is playable
			copyFrame = new Frame(frame);
			currWord.setWord(startRow, startCol, orientation, prefix);
			if (board.checkWord(currWord, copyFrame) == UI.WORD_OK) {
				currScore = board.setWord(currWord, copyFrame);
				if (lexicon.areWords(board.getWords())) {
					maxScore = board.getTotalWordScore(maxWord);
					if (currScore > maxScore) { //if word is better than our best so far, replace the best one with it
						maxWord.setWord(currWord.getStartRow(), currWord.getStartColumn(), currWord.getDirection(), currWord.getLetters()); 
					}
				}
				board.undo();
			}
			return;
		}
		else { //we've reached a stub so just go back
			return;
		}

	}


	public Word getWord () {
		// should not change
		return(word);
	}

	public String getLetters () {
		// should not change
		return(letters);
	}


}


/*
 * exact same as Dictionary except that root node is public instead of private and
 * we need this to implement our algorithm
 */
class Lexicon {
	private String inputFileName = "sowpods.txt";
	protected Node root;

	Lexicon () throws FileNotFoundException {
		String word = "";
		Node currentNode;
		char currentLetter;

		root = new Node();
		File inputFile = new File(inputFileName);
		Scanner in = new Scanner(inputFile);
		while (in.hasNextLine()) {
			word = in.nextLine();
			currentNode = root;
			for (int i=0; i<word.length(); i++) {
				currentLetter = word.charAt(i);
				if (currentNode.isChild(currentLetter)) {
					currentNode = currentNode.getChild(currentLetter);
				}
				else {
					currentNode = currentNode.addChild(currentLetter);
				}
			}
			currentNode.setEndOfWord();
		}
		in.close();
		return;
	}

	public boolean areWords (ArrayList<String> words) {
		Node currentNode;
		String currentWord;
		char currentLetter;
		boolean found = true;

		for (int w=0; (w<words.size()) && found; w++) {
			currentWord = words.get(w).toUpperCase();
			currentNode = root;
			for (int i=0; (i<currentWord.length()) && found; i++) {
				currentLetter = currentWord.charAt(i);
				if (currentNode.isChild(currentLetter)) {
					currentNode = currentNode.getChild(currentLetter);
				}
				else {
					found = false;
				}
			}
			if (!currentNode.isEndOfWord()) {
				found = false;
			}
		}

		return(found);
	}
}
