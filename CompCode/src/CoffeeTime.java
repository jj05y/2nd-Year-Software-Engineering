/* CoffeeTime
 * 
 * Daniel Conroy
 * Aaron O'Connor
 * Nikita Pavlenko
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class CoffeeTime implements Bot {
	private class Position {
		int startRow, startColumn, direction;
		
		Position (int row, int column, int orientation) {
			startRow = row;
			startColumn = column;
			direction = orientation;
			return;
		}
	}
	
	private static final int SQUARE_OK = 1;
	private static final int SQUARE_NOT_OK = 2;
	private static final int LETTER_SQUARE = 3;
	private static final int ALPHABET_SIZE = 26;

	private Word word = new Word();
	private String letters;

	@SuppressWarnings("unchecked")
	private ArrayList<String>[] anagrams = new ArrayList[Frame.MAX_TILES+1];
	@SuppressWarnings("unchecked")
	private ArrayList<Position>[] possiblePositions = new ArrayList[Frame.MAX_TILES+1];
	private int[][] sqStatus = new int[Board.SIZE][Board.SIZE];
	
	@Override
	public void reset() throws FileNotFoundException {
		word.setWord(0, 0, Word.HORIZONTAL, "HELLO");
		letters = "XYZ";
		for (int numLetters=0; numLetters<=Frame.MAX_TILES; numLetters++) {
			anagrams[numLetters] = new ArrayList<String>();
		}
		for (int numLetters=0; numLetters<=Frame.MAX_TILES; numLetters++) {
			possiblePositions[numLetters] = new ArrayList<Position>();
		}
	}
	
	/*Bot () {
		word.setWord(0, 0, Word.HORIZONTAL, "HELLO");
		letters = "XYZ";
		for (int numLetters=0; numLetters<=Frame.MAX_TILES; numLetters++) {
			anagrams[numLetters] = new ArrayList<String>();
		}
		for (int numLetters=0; numLetters<=Frame.MAX_TILES; numLetters++) {
			possiblePositions[numLetters] = new ArrayList<Position>();
		}
	}*/
	
	public int getCommand (Player player, Board board, Dictionary dictionary) throws FileNotFoundException, IOException {
		// make a decision on the play here
		// use board.getSqContents to check what is on the board
		// use Board.SQ_VALUE to check the multipliers
		// use frame.getAllTiles to check what letters you have
		// return the corresponding commandCode from UI
		// if a play, put the start position and letters into word
		// if an exchange, put the characters into letters
		int numTilesInFrame, commandCode;
		String lettersInFrame = "";
		
		for (Tile tile: player.getFrame().getAllTiles()) {
			lettersInFrame += tile.getFace();
		}
		numTilesInFrame = lettersInFrame.length();

		for (int numLetters=0; numLetters<=Frame.MAX_TILES; numLetters++) {
			anagrams[numLetters].clear();
		}
		genAnagrams(lettersInFrame);
		
		if (board.isFirstPlay()) {
			for (int col=1; col<=Board.CENTRE; col++) {
				possiblePositions[7].add(new Position(Board.CENTRE,col,Word.HORIZONTAL));
			}
			for (int col=2; col<=Board.CENTRE; col++) {
				possiblePositions[6].add(new Position(Board.CENTRE,col,Word.HORIZONTAL));
			}
			for (int col=3; col<=Board.CENTRE; col++) {
				possiblePositions[5].add(new Position(Board.CENTRE,col,Word.HORIZONTAL));
			}
			for (int numLetters=4; numLetters>1; numLetters--) {
				possiblePositions[numLetters].add(new Position(Board.CENTRE,Board.CENTRE,Word.HORIZONTAL));
			}
			word = findBest(Frame.MAX_TILES, board, dictionary);
		}
		else {
			genPossible(board);
			word = findBest(numTilesInFrame, board, dictionary);
		}
		
		if (word.getLetters().length()>0) { // Word placement found
			commandCode = UI.COMMAND_PLAY;
		}
		else {
			commandCode = UI.COMMAND_PASS;
		}
		
		return(commandCode);
	}
	
	public Word getWord () {
		// should not change
		return(word);
	}
		
	public String getLetters () {
		// should not change
		return(letters);
	}
	
	private void genAnagrams (String letters) {
		int blankTileIndex = letters.indexOf(Tile.BLANK);
		
		if (blankTileIndex>=0) {
			for (int i=0; i<ALPHABET_SIZE; i++) {
				genAnagrams(letters.substring(0, blankTileIndex)+((char) (((int) 'a')+i))
						+letters.substring(blankTileIndex+1, letters.length()));
			}
		}
		else { // No blank tile in letters
			for (String subsetOfLetters: genPowersetOfLetters(letters)) {
				anagrams[subsetOfLetters.length()].addAll(genPermutations(subsetOfLetters));
			}
		}
	}
	
	private ArrayList<String> genPowersetOfLetters (String letters) {
		ArrayList<String> powersetOfLetters = new ArrayList<String>();
		
		if (letters.length() == 0) {
			powersetOfLetters.add(letters);
		}
		else {
			String firstLetter = letters.substring(0,1);
			for (String subsetOfLetters: genPowersetOfLetters(letters.substring(1))) {
				powersetOfLetters.add(subsetOfLetters);
				powersetOfLetters.add(firstLetter+subsetOfLetters);
			}
		}
		return powersetOfLetters;
	}
	
	private ArrayList<String> genPermutations (String letters) {
		ArrayList<String> permutations = new ArrayList<String>();
		
		if (letters.length() == 0) {
			permutations.add(letters);
		}
		else {
			for (int i=0; i<letters.length(); i++) {
				for (String permutation: genPermutations(letters.substring(0,i)+letters.substring(i+1, letters.length()))) {
					permutations.add(letters.substring(i,i+1)+permutation);	
				}
			}
		}
		return permutations;
	}

	private void genPossible(Board board) {
		for (int numLetters=0; numLetters<=Frame.MAX_TILES; numLetters++) {
			possiblePositions[numLetters].clear();
		}
		genPossibleHorizontal(board);
		genPossibleVertical(board);
	}
	
	private void genPossibleHorizontal(Board board) {
		for (int row=0; row<Board.SIZE; row++) {
			for (int column=0; column<Board.SIZE; column++) {
				sqStatus[row][column] = SQUARE_NOT_OK;
			}
		}
		// Record letter positions and empty squares on which a HORIZONTAL word may be placed
		for (int row=0; row<Board.SIZE; row++) {
			int column = 0;
			do {
				while (column<Board.SIZE && board.getSqContents(row, column)==Board.EMPTY) {
					column++;
				}
				if (column<Board.SIZE) { // Letter encountered (board.getSqContents(row, column)!=Board.EMPTY)
					// Record empty squares preceding the letter that are not adjacent to a letter in a different row
					for (int c=1; c<=Frame.MAX_TILES && column>=c
							&& (row==0 || board.getSqContents(row-1, column-c)==Board.EMPTY)
							&& board.getSqContents(row, column-c)==Board.EMPTY
							&& (row==Board.SIZE-1 || board.getSqContents(row+1, column-c)==Board.EMPTY); c++) {
						sqStatus[row][column-c] = SQUARE_OK;
					}
					// Record positions of this bunch of letters
					do {
						sqStatus[row][column] = LETTER_SQUARE;
						column++;
					} while (column<Board.SIZE && board.getSqContents(row, column)!=Board.EMPTY);
					
					if (column<Board.SIZE) { // Empty square encountered after letter(s)
						// Record empty squares after the last letter that are not adjacent to a letter in a different row
						int c;
						for (c=0; c<Frame.MAX_TILES && column+c<Board.SIZE
								&& (row==0 || board.getSqContents(row-1, column+c)==Board.EMPTY)
								&& board.getSqContents(row, column+c)==Board.EMPTY
								&& (row==Board.SIZE-1 || board.getSqContents(row+1, column+c)==Board.EMPTY); c++) {
							sqStatus[row][column+c] = SQUARE_OK;
						}
						/* May have reached the end of the row or encountered another letter (possibly in a different row),
						 * or encountered Frame.MAX_TILES consecutive empty squares (the max number of OK squares after letters). */
						column += c;
					}
				}
			} while (column<Board.SIZE);
		}
		// Record possible positions of HORIZONTAL words
		for (int row=0; row<Board.SIZE; row++) {
			int column = 0;
			findPossiblePositionsInRow(row, column);
		}
	}
	
	private void findPossiblePositionsInRow(int rowNum, int colNum) {
		int row = rowNum, column = colNum;
		int numPrecedingOKSquares = 0, numOtherOKSquares = 0, firstLetterOrOKSquare = -1;
		boolean separateLetterEncountered = false;

		do {
			if (!separateLetterEncountered) {
				numPrecedingOKSquares = 0;
				numOtherOKSquares = 0;
				firstLetterOrOKSquare = -1;
			}
			// Search through the columns until you find a letter or reach the end of the row
			while (column<Board.SIZE && sqStatus[row][column]!=LETTER_SQUARE) {
				column++;
			}
			if (column<Board.SIZE) { // Letter encountered: sqStatus[row][column]==LETTER_SQUARE
				if (!separateLetterEncountered) {
					// Haven't already counted preceding OK squares or recorded firstLetterOrOKSquare
					firstLetterOrOKSquare = column;
					/* Go through squares before the letter to count preceding OK squares that are not adjacent to
					 * any other letter and update firstLetterOrOKSquare accordingly */
					while (numPrecedingOKSquares<Frame.MAX_TILES
							&& column>numPrecedingOKSquares
							&& sqStatus[row][(column-1)-numPrecedingOKSquares]==SQUARE_OK
							&& (column==numPrecedingOKSquares+1 || sqStatus[row][column-numPrecedingOKSquares-2]!=LETTER_SQUARE)) {
						firstLetterOrOKSquare = (column-1)-numPrecedingOKSquares;
						numPrecedingOKSquares++;
					}
				}
				do {
					column++;
				} while (column<Board.SIZE && sqStatus[row][column]==LETTER_SQUARE);
				// At the end of the bunch of letters (possibly the end of the row)
				
				while (numOtherOKSquares<Frame.MAX_TILES && column<Board.SIZE && sqStatus[row][column]==SQUARE_OK) {
					// There is an empty square after the bunch of letters, so count OK squares after letters
					numOtherOKSquares++;
					column++;
				}
				
				if (numOtherOKSquares<=Frame.MAX_TILES && column<Board.SIZE && sqStatus[row][column]==LETTER_SQUARE) {
					// Have reached another letter which may be encompassed by the possible positions we're currently searching for
					separateLetterEncountered = true;
					
					findPossiblePositionsInRow(row, column);
					/* Search for possible positions containing the separate letter just encountered as their first letter.
					 * Thus OK squares before it (and not adjacent to any other letter) will be considered preceding OK squares
					 * and OK squares after it will be considered other OK squares. */
				}
				else {
					// Have reached the end of the possible positions we're currently searching for and possibly the end of the row
					separateLetterEncountered = false;
						
					if (numPrecedingOKSquares>0) {						
						// Add positions beginning with preceding OK squares and which encompass only preceding OK squares
						for (int numPreceding=numPrecedingOKSquares; numPreceding>0; numPreceding--) {
							possiblePositions[numPreceding].add(
								new Position(row,firstLetterOrOKSquare+numPrecedingOKSquares-numPreceding,Word.HORIZONTAL));
						}
					}	
					if (numPrecedingOKSquares>0 && numOtherOKSquares>0) {
						/* Add positions beginning with preceding OK squares and which encompass other OK squares,
						 * but which encompass at most NUM_LETTERS OK squares */
						for (int numOthers=1; numOthers<=numOtherOKSquares; numOthers++) {
							for (int numPreceding=1; numPreceding<=numPrecedingOKSquares; numPreceding++) {
								if (numPreceding+numOthers<=Frame.MAX_TILES) {
									possiblePositions[numPreceding+numOthers].add(
										new Position(row,firstLetterOrOKSquare+numPrecedingOKSquares-numPreceding,Word.HORIZONTAL));
								}
							}
						}
					}
					if (numOtherOKSquares>0) {
						// Add positions beginning with a letter already on the board
						for (int numOthers=numOtherOKSquares; numOthers>0; numOthers--) {
							possiblePositions[numOthers].add(
								new Position(row,firstLetterOrOKSquare+numPrecedingOKSquares,Word.HORIZONTAL));
						}
					}
				}
			}
		} while (column<Board.SIZE);
	}
	
	private void genPossibleVertical(Board board) {
		for (int row=0; row<Board.SIZE; row++) {
			for (int column=0; column<Board.SIZE; column++) {
				sqStatus[row][column] = SQUARE_NOT_OK;
			}
		}
		// Record letter positions and empty squares on which a VERTICAL word may be placed
		for (int column=0; column<Board.SIZE; column++) {
			int row = 0;
			do {
				while (row<Board.SIZE && board.getSqContents(row, column)==Board.EMPTY) {
					row++;
				}
				if (row<Board.SIZE) { // Letter encountered (board.getSqContents(row, column)!=Board.EMPTY)
					// Record empty squares preceding the letter that are not adjacent to a letter in a different column
					for (int r=1; r<=Frame.MAX_TILES && row>=r
							&& (column==0 || board.getSqContents(row-r, column-1)==Board.EMPTY)
							&& board.getSqContents(row-r, column)==Board.EMPTY
							&& (column==Board.SIZE-1 || board.getSqContents(row-r, column+1)==Board.EMPTY); r++) {
						sqStatus[row-r][column] = SQUARE_OK;
					}
					// Record positions of this bunch of letters
					do {
						sqStatus[row][column] = LETTER_SQUARE;
						row++;
					} while (row<Board.SIZE && board.getSqContents(row, column)!=Board.EMPTY);
					
					if (row<Board.SIZE) { // Empty square encountered after letter(s)
						// Record empty squares after the last letter that are not adjacent to a letter in a different column
						int r;
						for (r=0; r<Frame.MAX_TILES && row+r<Board.SIZE
								&& (column==0 || board.getSqContents(row+r, column-1)==Board.EMPTY)
								&& board.getSqContents(row+r, column)==Board.EMPTY
								&& (column==Board.SIZE-1 || board.getSqContents(row+r, column+1)==Board.EMPTY); r++) {
							sqStatus[row+r][column] = SQUARE_OK;
						}
						/* May have reached the end of the row or encountered another letter (possibly in a different column),
						 * or encountered Frame.MAX_TILES consecutive empty squares (the max number of OK squares after letters). */
						row += r;
					}
				}
			} while (row<Board.SIZE);
		}
		// Record possible positions of VERTICAL words
		for (int column=0; column<Board.SIZE; column++) {
			int row = 0;
			findPossiblePositionsInColumn(row, column);
		}
	}
	
	private void findPossiblePositionsInColumn(int rowNum, int colNum) {
		int column = colNum, row = rowNum;
		int numPrecedingOKSquares = 0, numOtherOKSquares = 0, firstLetterOrOKSquare = -1;
		boolean separateLetterEncountered = false;

		do {
			if (!separateLetterEncountered) {
				numPrecedingOKSquares = 0;
				numOtherOKSquares = 0;
				firstLetterOrOKSquare = -1;
			}
			// Search through the rows until you find a letter or reach the end of the column
			while (row<Board.SIZE && sqStatus[row][column]!=LETTER_SQUARE) {
				row++;
			}
			if (row<Board.SIZE) { // Letter encountered: sqStatus[row][column]==LETTER_SQUARE
				if (!separateLetterEncountered) {
					// Haven't already counted preceding OK squares or recorded firstLetterOrOKSquare
					firstLetterOrOKSquare = row;
					/* Go through squares before the letter to count preceding OK squares that are not adjacent to
					 * any other letter and update firstLetterOrOKSquare accordingly */
					while (numPrecedingOKSquares<Frame.MAX_TILES
							&& row>numPrecedingOKSquares
							&& sqStatus[(row-1)-numPrecedingOKSquares][column]==SQUARE_OK
							&& (row==numPrecedingOKSquares+1 || sqStatus[row-numPrecedingOKSquares-2][column]!=LETTER_SQUARE)) {
						firstLetterOrOKSquare = (row-1)-numPrecedingOKSquares;
						numPrecedingOKSquares++;
					}
				}
				do {
					row++;
				} while (row<Board.SIZE && sqStatus[row][column]==LETTER_SQUARE);
				// At the end of the bunch of letters (possibly the end of the column)
				
				while (numOtherOKSquares<Frame.MAX_TILES && row<Board.SIZE && sqStatus[row][column]==SQUARE_OK) {
					// There is an empty square after the bunch of letters, so count OK squares after letters
					numOtherOKSquares++;
					row++;
				}
				
				if (numOtherOKSquares<=Frame.MAX_TILES && row<Board.SIZE && sqStatus[row][column]==LETTER_SQUARE) {
					// Have reached another letter which may be encompassed by the possible positions we're currently searching for
					separateLetterEncountered = true;
					
					findPossiblePositionsInColumn(row, column);
					/* Search for possible positions containing the separate letter just encountered as their first letter.
					 * Thus OK squares before it (and not adjacent to any other letter) will be considered preceding OK squares
					 * and OK squares after it will be considered other OK squares. */
				}
				else {
					// Have reached the end of the possible positions we're currently searching for and possibly the end of the row
					separateLetterEncountered = false;
					
					if (numPrecedingOKSquares>0) {						
						// Add positions beginning with preceding OK squares and which encompass only preceding OK squares
						for (int numPreceding=numPrecedingOKSquares; numPreceding>0; numPreceding--) {
							possiblePositions[numPreceding].add(
								new Position(firstLetterOrOKSquare+numPrecedingOKSquares-numPreceding,column,Word.VERTICAL));
						}
					}	
					if (numPrecedingOKSquares>0 && numOtherOKSquares>0) {
						/* Add positions beginning with preceding OK squares and which encompass other OK squares,
						 * but which encompass at most NUM_LETTERS OK squares */
						for (int numOthers=1; numOthers<=numOtherOKSquares; numOthers++) {
							for (int numPreceding=1; numPreceding<=numPrecedingOKSquares; numPreceding++) {
								if (numPreceding+numOthers<=Frame.MAX_TILES) {
									possiblePositions[numPreceding+numOthers].add(
										new Position(firstLetterOrOKSquare+numPrecedingOKSquares-numPreceding,column,Word.VERTICAL));
								}
							}
						}
					}
					if (numOtherOKSquares>0) {
						// Add positions beginning with a letter already on the board
						for (int numOthers=numOtherOKSquares; numOthers>0; numOthers--) {
							possiblePositions[numOthers].add(
								new Position(firstLetterOrOKSquare+numPrecedingOKSquares,column,Word.VERTICAL));
						}
					}
				}
			}
		} while (row<Board.SIZE);
	}
	
	private Word findBest(int numTilesInFrame, Board board, Dictionary dictionary) {
		Word maxWord = new Word(0, 0, Word.HORIZONTAL, "");
		Word checkWord = new Word();
		
		for (int numLetters=1; numLetters<=numTilesInFrame; numLetters++) {
			for (int currPosition=0; currPosition<possiblePositions[numLetters].size(); currPosition++) {
				int startRow = possiblePositions[numLetters].get(currPosition).startRow;
				int startColumn = possiblePositions[numLetters].get(currPosition).startColumn;
				int direction = possiblePositions[numLetters].get(currPosition).direction;
						
				for (int currAnagram=0; currAnagram<anagrams[numLetters].size(); currAnagram++) {
					ArrayList<String> checkWordInDictionary = new ArrayList<String>();
					String possibleWord = "";
					int currentScore = 0, maxScore = 0, tilesUsed = 0;
					int row = startRow, column = startColumn;
					
					while (tilesUsed<numLetters) {
						if (board.getSqContents(row, column)==Board.EMPTY) {
							possibleWord += anagrams[numLetters].get(currAnagram).charAt(tilesUsed);
							tilesUsed++;
						}
						else {
							possibleWord += board.getSqContents(row, column);
						}
						
						if (direction==Word.HORIZONTAL) {
							column++;
						}
						else { // direction==Word.VERTICAL
							row++;
						}
					}
					
					// Add any letters after the last empty square in the current position
					while (((direction==Word.HORIZONTAL && column<Board.SIZE)
							|| (direction==Word.VERTICAL && row<Board.SIZE))
						   && (board.getSqContents(row, column)!=Board.EMPTY)) {
						possibleWord += board.getSqContents(row, column);
						
						if (direction==Word.HORIZONTAL) {
							column++;
						}
						else { // direction==Word.VERTICAL
							row++;
						}
					}
					
					checkWordInDictionary.add(possibleWord.toUpperCase());
					
					if (dictionary.areWords(checkWordInDictionary)) {
						checkWord.setWord(startRow, startColumn, direction, possibleWord);
						currentScore = board.getTotalWordScore(checkWord);
						
						if (currentScore>maxScore) {
							maxScore = currentScore;
							maxWord = checkWord;
						}
					}
				}
			}
		}
		
		return maxWord;
	}
	
	/*public static void main (String[] args) throws FileNotFoundException {
		// Add this method to the frame class to run these tests
		// public void addTiles(ArrayList<Tile> tiles) {this.tiles = tiles;}
		
		Bot bot = new Bot();
		Board board = new Board();
		Pool pool = new Pool();
		Player player = new Player();
		UI ui = new UI();
		Dictionary dictionary = new Dictionary();
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		ArrayList<String> powersetOfLetters = bot.genPowersetOfLetters("ABCDEFG");
		
		System.out.println("Size of powerset of letters: "+powersetOfLetters.size());
		System.out.print("Elements of powerset:");
		for (int i=1; i<powersetOfLetters.size(); i++) {
			System.out.print(","+powersetOfLetters.get(i));
		}
		System.out.println();
		System.out.println();
		
		player.setName("Bot");
		player.getFrame().refill(pool);
		bot.getCommand(player, board, dictionary);
		
		for (int i=0; i<=Frame.MAX_TILES; i++) {
			System.out.println("Number of permutations of "+i+" letters: "+bot.anagrams[i].size());
		}
		System.out.println();
		System.out.println("Permutations of ABCD:");
		for (String permutation: bot.genPermutations("ABCD")) {
			System.out.print(permutation+" ");
		}
		System.out.println("\n");
		
		while (!player.getFrame().isEmpty()) {
			player.getFrame().removeAt(0);
		}
		
		for (int i=0; i<Frame.MAX_TILES; i++) {
			tiles.add(new Tile(Tile.BLANK));
		}
		player.getFrame().addTiles(tiles);
		board.setWord(new Word(7,7,Word.HORIZONTAL,"flowers"), player.getFrame());
		System.out.println("BOARD");
		ui.displayBoard(board);
		System.out.println("HORIZONTAL");
		bot.genPossibleHorizontal(board);
		bot.displaySqStatus();
		System.out.println("VERTICAL");
		bot.genPossibleVertical(board);
		bot.displaySqStatus();
		
		bot.genPossible(board);
		for (int numLetters=Frame.MAX_TILES; numLetters>0; numLetters--) {
			int position;
			System.out.print("Possible "+numLetters+" letter positions\nHorizontal:\t");
			for (position=0; bot.possiblePositions[numLetters].get(position).getDirection()==Word.HORIZONTAL; position++) {
				System.out.print((""+(char) (((int) 'A')+bot.possiblePositions[numLetters].get(position).getStartColumn()))
						+(bot.possiblePositions[numLetters].get(position).getStartRow()+1)+" ");
			}
			System.out.print("\nVertical:\t");
			while (position<bot.possiblePositions[numLetters].size()) {
				System.out.print((""+(char) (((int) 'A')+bot.possiblePositions[numLetters].get(position).getStartColumn()))
						+(bot.possiblePositions[numLetters].get(position).getStartRow()+1)+" ");
				position++;
			}
			System.out.println("\n");
		}
		System.out.println();
		
		for (int i=0; i<Frame.MAX_TILES; i++) {
			tiles.add(new Tile(Tile.BLANK));
		}
		player.getFrame().addTiles(tiles);
		board.setWord(new Word(1,13,Word.VERTICAL,"knights"), player.getFrame());
		System.out.println("BOARD");
		ui.displayBoard(board);
		System.out.println("HORIZONTAL");
		bot.genPossibleHorizontal(board);
		bot.displaySqStatus();
		System.out.println("VERTICAL");
		bot.genPossibleVertical(board);
		bot.displaySqStatus();
		
		bot.genPossible(board);
		for (int numLetters=Frame.MAX_TILES; numLetters>0; numLetters--) {
			int position;
			System.out.print("Possible "+numLetters+" letter positions\nHorizontal:\t");
			for (position=0; bot.possiblePositions[numLetters].get(position).getDirection()==Word.HORIZONTAL; position++) {
				System.out.print((""+(char) (((int) 'A')+bot.possiblePositions[numLetters].get(position).getStartColumn()))
						+(bot.possiblePositions[numLetters].get(position).getStartRow()+1)+" ");
			}
			System.out.print("\nVertical:\t");
			while (position<bot.possiblePositions[numLetters].size()) {
				System.out.print((""+(char) (((int) 'A')+bot.possiblePositions[numLetters].get(position).getStartColumn()))
						+(bot.possiblePositions[numLetters].get(position).getStartRow()+1)+" ");
				position++;
			}
			System.out.println("\n");
		}
		System.out.println();
		
		for (int i=0; i<Frame.MAX_TILES; i++) {
			tiles.add(new Tile(Tile.BLANK));
		}
		player.getFrame().addTiles(tiles);
		board.setWord(new Word(7,9,Word.VERTICAL,"overrun"), player.getFrame());
		System.out.println("BOARD");
		ui.displayBoard(board);
		System.out.println("HORIZONTAL");
		bot.genPossibleHorizontal(board);
		bot.displaySqStatus();
		System.out.println("VERTICAL");
		bot.genPossibleVertical(board);
		bot.displaySqStatus();
		
		bot.genPossible(board);
		for (int numLetters=Frame.MAX_TILES; numLetters>0; numLetters--) {
			int position;
			System.out.print("Possible "+numLetters+" letter positions\nHorizontal:\t");
			for (position=0; bot.possiblePositions[numLetters].get(position).getDirection()==Word.HORIZONTAL; position++) {
				System.out.print((""+(char) (((int) 'A')+bot.possiblePositions[numLetters].get(position).getStartColumn()))
						+(bot.possiblePositions[numLetters].get(position).getStartRow()+1)+" ");
			}
			System.out.print("\nVertical:\t");
			while (position<bot.possiblePositions[numLetters].size()) {
				System.out.print((""+(char) (((int) 'A')+bot.possiblePositions[numLetters].get(position).getStartColumn()))
						+(bot.possiblePositions[numLetters].get(position).getStartRow()+1)+" ");
				position++;
			}
			System.out.println("\n");
		}
		System.out.println();
		
		for (int i=0; i<Frame.MAX_TILES; i++) {
			tiles.add(new Tile(Tile.BLANK));
		}
		player.getFrame().addTiles(tiles);
		board.setWord(new Word(11,4,Word.HORIZONTAL,"terror"), player.getFrame());
		System.out.println("BOARD");
		ui.displayBoard(board);
		System.out.println("HORIZONTAL");
		bot.genPossibleHorizontal(board);
		bot.displaySqStatus();
		System.out.println("VERTICAL");
		bot.genPossibleVertical(board);
		bot.displaySqStatus();
		
		bot.genPossible(board);
		for (int numLetters=Frame.MAX_TILES; numLetters>0; numLetters--) {
			int position;
			System.out.print("Possible "+numLetters+" letter positions\nHorizontal:\t");
			for (position=0; bot.possiblePositions[numLetters].get(position).getDirection()==Word.HORIZONTAL; position++) {
				System.out.print((""+(char) (((int) 'A')+bot.possiblePositions[numLetters].get(position).getStartColumn()))
						+(bot.possiblePositions[numLetters].get(position).getStartRow()+1)+" ");
			}
			System.out.print("\nVertical:\t");
			while (position<bot.possiblePositions[numLetters].size()) {
				System.out.print((""+(char) (((int) 'A')+bot.possiblePositions[numLetters].get(position).getStartColumn()))
						+(bot.possiblePositions[numLetters].get(position).getStartRow()+1)+" ");
				position++;
			}
			System.out.println("\n");
		}
		System.out.println();
		
		for (int i=0; i<Frame.MAX_TILES; i++) {
			tiles.add(new Tile(Tile.BLANK));
		}
		player.getFrame().addTiles(tiles);
		board.setWord(new Word(5,10,Word.VERTICAL,"bow"), player.getFrame());
		System.out.println("BOARD");
		ui.displayBoard(board);
		System.out.println("HORIZONTAL");
		bot.genPossibleHorizontal(board);
		bot.displaySqStatus();
		System.out.println("VERTICAL");
		bot.genPossibleVertical(board);
		bot.displaySqStatus();
		
		bot.genPossible(board);
		for (int numLetters=Frame.MAX_TILES; numLetters>0; numLetters--) {
			int position;
			System.out.print("Possible "+numLetters+" letter positions\nHorizontal:\t");
			for (position=0; bot.possiblePositions[numLetters].get(position).getDirection()==Word.HORIZONTAL; position++) {
				System.out.print((""+(char) (((int) 'A')+bot.possiblePositions[numLetters].get(position).getStartColumn()))
						+(bot.possiblePositions[numLetters].get(position).getStartRow()+1)+" ");
			}
			System.out.print("\nVertical:\t");
			while (position<bot.possiblePositions[numLetters].size()) {
				System.out.print((""+(char) (((int) 'A')+bot.possiblePositions[numLetters].get(position).getStartColumn()))
						+(bot.possiblePositions[numLetters].get(position).getStartRow()+1)+" ");
				position++;
			}
			System.out.println("\n");
		}
		System.out.println();
		
		for (int i=0; i<Frame.MAX_TILES; i++) {
			tiles.add(new Tile(Tile.BLANK));
		}
		player.getFrame().addTiles(tiles);
		board.setWord(new Word(6,12,Word.VERTICAL,"arrow"), player.getFrame());
		System.out.println("BOARD");
		ui.displayBoard(board);
		System.out.println("HORIZONTAL");
		bot.genPossibleHorizontal(board);
		bot.displaySqStatus();
		System.out.println("VERTICAL");
		bot.genPossibleVertical(board);
		bot.displaySqStatus();
		
		bot.genPossible(board);
		for (int numLetters=Frame.MAX_TILES; numLetters>0; numLetters--) {
			int position;
			System.out.print("Possible "+numLetters+" letter positions\nHorizontal:\t");
			for (position=0; bot.possiblePositions[numLetters].get(position).getDirection()==Word.HORIZONTAL; position++) {
				System.out.print((""+(char) (((int) 'A')+bot.possiblePositions[numLetters].get(position).getStartColumn()))
						+(bot.possiblePositions[numLetters].get(position).getStartRow()+1)+" ");
			}
			System.out.print("\nVertical:\t");
			while (position<bot.possiblePositions[numLetters].size()) {
				System.out.print((""+(char) (((int) 'A')+bot.possiblePositions[numLetters].get(position).getStartColumn()))
						+(bot.possiblePositions[numLetters].get(position).getStartRow()+1)+" ");
				position++;
			}
			System.out.println("\n");
		}
		System.out.println();
	}
	
	public void displaySqStatus() {
		char column = 'A';
		System.out.print("    ");
		for (int c=0; c<Board.SIZE; c++) {
			System.out.printf("%c ",column);
			column++;
		}
		System.out.println();
		System.out.println();
		
		for (int r=0; r<Board.SIZE; r++) {
			System.out.printf("%2d  ", r+1);
			for (int c=0; c<Board.SIZE; c++) {
				if (sqStatus[r][c] == SQUARE_OK) {
					System.out.print("O ");
				} else if (sqStatus[r][c] == SQUARE_NOT_OK) {
					System.out.print("N ");
				} else {
					System.out.print("L ");
				}
			}
			System.out.printf("  %2d\n", r);
		}

		System.out.println();
		column = 'A';
		System.out.print("    ");
		for (int c=0; c<Board.SIZE; c++) {
			System.out.printf("%c ",column);
			column++;
		}
		System.out.println();
		System.out.println();
	}*/
}
