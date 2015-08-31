package Ass2;

import java.util.ArrayList;

/**
 * this class creates and displays the board and places its multipliers.
 * 
 * @author mvp
 * @version 0.1
 * @since 18/02/15
 * 
 */
public class Board {

	/**
	 * board - created a new board array.
	 */
	private Square[][] board;

	/**
	 * creates an array of squares which constitutes the board calls
	 * setupSquares which allocates multipliers and centre square
	 */
	public Board() {
		board = new Square[Consts.BOARD_SIZE][Consts.BOARD_SIZE];
		for (int row = 0; row < Consts.BOARD_SIZE; row++) {
			for (int col = 0; col < Consts.BOARD_SIZE; col++) {
				board[row][col] = new Square();
			}
		}
		setUpSquares();
	}

	/**
	 * returns the board
	 * 
	 * @return board
	 */
	public Square[][] getBoard() {
		return board;
	}

	/**
	 * removes the multiplier from squares when used.
	 * 
	 * @param tilesToNullify
	 */
	public void nullifyTiles(ArrayList<Integer> tilesToNullify) {
		for (int k = 0; k < tilesToNullify.size(); k += 2) {
			board[tilesToNullify.get(k)][tilesToNullify.get(k + 1)]
					.setLetterMultiplier(1);
			board[tilesToNullify.get(k)][tilesToNullify.get(k + 1)]
					.setWordMultiplier(1);
		}
	}

	/**
	 * resets the board entirely
	 */
	public void reset() {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				board[row][col].setDisplayChar(Consts.BLANK_SQUARE);
				board[row][col].setTile(null);
			}
		}
		setUpSquares();

	}

	/**
	 * to string method for printing our the board and displaying visualy
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int row = 0; row < board.length; row++) {
			if (row == 0) {
				sb.append("      0   1   2   3   4   5   6   7   8   9  10  11  12  13  14  \n");
				sb.append("     ----------------------------------------------------------- \n");
			} else {
				sb.append("    |---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|\n");
			}
			sb.append(String.format(" %2d ", row));
			for (int col = 0; col < board[0].length; col++) {
				sb.append("|");
				if (board[row][col].getTile() == null) {

					sb.append(board[row][col].getDisplayChar());
				} else {
					sb.append("[" + board[row][col].getTile().getLetter() + "]");
				}

			}
			sb.append("| " + row + " \n");
		}

		sb.append("     ----------------------------------------------------------- \n");
		sb.append("      0   1   2   3   4   5   6   7   8   9  10  11  12  13  14  \n");

		return new String(sb);

	}

	/**
	 * Initializes the special squares
	 */
	private void setUpSquares() {
		board[0][0].setWordMultiplier(Consts.TRIPLE);
		board[0][0].setDisplayChar(Consts.TRIPLE_WORD);
		board[0][7].setWordMultiplier(Consts.TRIPLE);
		board[0][7].setDisplayChar(Consts.TRIPLE_WORD);
		board[0][14].setWordMultiplier(Consts.TRIPLE);
		board[0][14].setDisplayChar(Consts.TRIPLE_WORD);
		board[7][0].setWordMultiplier(Consts.TRIPLE);
		board[7][0].setDisplayChar(Consts.TRIPLE_WORD);
		board[7][14].setWordMultiplier(Consts.TRIPLE);
		board[7][14].setDisplayChar(Consts.TRIPLE_WORD);
		board[14][0].setWordMultiplier(Consts.TRIPLE);
		board[14][0].setDisplayChar(Consts.TRIPLE_WORD);
		board[14][7].setWordMultiplier(Consts.TRIPLE);
		board[14][7].setDisplayChar(Consts.TRIPLE_WORD);
		board[14][14].setWordMultiplier(Consts.TRIPLE);
		board[14][14].setDisplayChar(Consts.TRIPLE_WORD);

		board[1][1].setWordMultiplier(Consts.DOUBLE);
		board[1][1].setDisplayChar(Consts.DOUBLE_WORD);
		board[1][13].setWordMultiplier(Consts.DOUBLE);
		board[1][13].setDisplayChar(Consts.DOUBLE_WORD);
		board[2][2].setWordMultiplier(Consts.DOUBLE);
		board[2][2].setDisplayChar(Consts.DOUBLE_WORD);
		board[2][12].setWordMultiplier(Consts.DOUBLE);
		board[2][12].setDisplayChar(Consts.DOUBLE_WORD);
		board[3][3].setWordMultiplier(Consts.DOUBLE);
		board[3][3].setDisplayChar(Consts.DOUBLE_WORD);
		board[3][11].setWordMultiplier(Consts.DOUBLE);
		board[3][11].setDisplayChar(Consts.DOUBLE_WORD);
		board[4][4].setWordMultiplier(Consts.DOUBLE);
		board[4][4].setDisplayChar(Consts.DOUBLE_WORD);
		board[4][10].setWordMultiplier(Consts.DOUBLE);
		board[4][10].setDisplayChar(Consts.DOUBLE_WORD);
		board[7][7].setWordMultiplier(Consts.DOUBLE);
		board[7][7].setDisplayChar(Consts.STAR);
		board[10][4].setWordMultiplier(Consts.DOUBLE);
		board[10][4].setDisplayChar(Consts.DOUBLE_WORD);
		board[10][10].setWordMultiplier(Consts.DOUBLE);
		board[10][10].setDisplayChar(Consts.DOUBLE_WORD);
		board[11][3].setWordMultiplier(Consts.DOUBLE);
		board[11][3].setDisplayChar(Consts.DOUBLE_WORD);
		board[11][11].setWordMultiplier(Consts.DOUBLE);
		board[11][11].setDisplayChar(Consts.DOUBLE_WORD);
		board[12][2].setWordMultiplier(Consts.DOUBLE);
		board[12][2].setDisplayChar(Consts.DOUBLE_WORD);
		board[12][12].setWordMultiplier(Consts.DOUBLE);
		board[12][12].setDisplayChar(Consts.DOUBLE_WORD);
		board[13][1].setWordMultiplier(Consts.DOUBLE);
		board[13][1].setDisplayChar(Consts.DOUBLE_WORD);
		board[13][13].setWordMultiplier(Consts.DOUBLE);
		board[13][13].setDisplayChar(Consts.DOUBLE_WORD);

		board[1][5].setLetterMultiplier(Consts.TRIPLE);
		board[1][5].setDisplayChar(Consts.TRIPLE_LETTER);
		board[1][9].setLetterMultiplier(Consts.TRIPLE);
		board[1][9].setDisplayChar(Consts.TRIPLE_LETTER);
		board[5][1].setLetterMultiplier(Consts.TRIPLE);
		board[5][1].setDisplayChar(Consts.TRIPLE_LETTER);
		board[5][5].setLetterMultiplier(Consts.TRIPLE);
		board[5][5].setDisplayChar(Consts.TRIPLE_LETTER);
		board[5][9].setLetterMultiplier(Consts.TRIPLE);
		board[5][9].setDisplayChar(Consts.TRIPLE_LETTER);
		board[5][13].setLetterMultiplier(Consts.TRIPLE);
		board[5][13].setDisplayChar(Consts.TRIPLE_LETTER);
		board[9][1].setLetterMultiplier(Consts.TRIPLE);
		board[9][1].setDisplayChar(Consts.TRIPLE_LETTER);
		board[9][5].setLetterMultiplier(Consts.TRIPLE);
		board[9][5].setDisplayChar(Consts.TRIPLE_LETTER);
		board[9][9].setLetterMultiplier(Consts.TRIPLE);
		board[9][9].setDisplayChar(Consts.TRIPLE_LETTER);
		board[9][13].setLetterMultiplier(Consts.TRIPLE);
		board[9][13].setDisplayChar(Consts.TRIPLE_LETTER);
		board[13][5].setLetterMultiplier(Consts.TRIPLE);
		board[13][5].setDisplayChar(Consts.TRIPLE_LETTER);
		board[13][9].setLetterMultiplier(Consts.TRIPLE);
		board[13][9].setDisplayChar(Consts.TRIPLE_LETTER);

		board[0][3].setLetterMultiplier(Consts.DOUBLE);
		board[0][3].setDisplayChar(Consts.DOUBLE_LETTER);
		board[0][11].setLetterMultiplier(Consts.DOUBLE);
		board[0][11].setDisplayChar(Consts.DOUBLE_LETTER);
		board[2][6].setLetterMultiplier(Consts.DOUBLE);
		board[2][6].setDisplayChar(Consts.DOUBLE_LETTER);
		board[2][8].setLetterMultiplier(Consts.DOUBLE);
		board[2][8].setDisplayChar(Consts.DOUBLE_LETTER);
		board[3][0].setLetterMultiplier(Consts.DOUBLE);
		board[3][0].setDisplayChar(Consts.DOUBLE_LETTER);
		board[3][7].setLetterMultiplier(Consts.DOUBLE);
		board[3][7].setDisplayChar(Consts.DOUBLE_LETTER);
		board[3][14].setLetterMultiplier(Consts.DOUBLE);
		board[3][14].setDisplayChar(Consts.DOUBLE_LETTER);
		board[6][2].setLetterMultiplier(Consts.DOUBLE);
		board[6][2].setDisplayChar(Consts.DOUBLE_LETTER);
		board[6][6].setLetterMultiplier(Consts.DOUBLE);
		board[6][6].setDisplayChar(Consts.DOUBLE_LETTER);
		board[6][8].setLetterMultiplier(Consts.DOUBLE);
		board[6][8].setDisplayChar(Consts.DOUBLE_LETTER);
		board[6][12].setLetterMultiplier(Consts.DOUBLE);
		board[6][12].setDisplayChar(Consts.DOUBLE_LETTER);
		board[7][3].setLetterMultiplier(Consts.DOUBLE);
		board[7][3].setDisplayChar(Consts.DOUBLE_LETTER);
		board[7][11].setLetterMultiplier(Consts.DOUBLE);
		board[7][11].setDisplayChar(Consts.DOUBLE_LETTER);
		board[8][2].setLetterMultiplier(Consts.DOUBLE);
		board[8][2].setDisplayChar(Consts.DOUBLE_LETTER);
		board[8][6].setLetterMultiplier(Consts.DOUBLE);
		board[8][6].setDisplayChar(Consts.DOUBLE_LETTER);
		board[8][8].setLetterMultiplier(Consts.DOUBLE);
		board[8][8].setDisplayChar(Consts.DOUBLE_LETTER);
		board[8][12].setLetterMultiplier(Consts.DOUBLE);
		board[8][12].setDisplayChar(Consts.DOUBLE_LETTER);
		board[11][0].setLetterMultiplier(Consts.DOUBLE);
		board[11][0].setDisplayChar(Consts.DOUBLE_LETTER);
		board[11][7].setLetterMultiplier(Consts.DOUBLE);
		board[11][7].setDisplayChar(Consts.DOUBLE_LETTER);
		board[11][14].setLetterMultiplier(Consts.DOUBLE);
		board[11][14].setDisplayChar(Consts.DOUBLE_LETTER);
		board[12][6].setLetterMultiplier(Consts.DOUBLE);
		board[12][6].setDisplayChar(Consts.DOUBLE_LETTER);
		board[12][8].setLetterMultiplier(Consts.DOUBLE);
		board[12][8].setDisplayChar(Consts.DOUBLE_LETTER);
		board[14][3].setLetterMultiplier(Consts.DOUBLE);
		board[14][3].setDisplayChar(Consts.DOUBLE_LETTER);
		board[14][11].setLetterMultiplier(Consts.DOUBLE);
		board[14][11].setDisplayChar(Consts.DOUBLE_LETTER);

		/*
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				if (board[i][j].getDisplayChar().equals(Consts.DOUBLE_LETTER)) {
					board[i][j].setLetterMultiplier(Consts.DOUBLE);
				} else if (board[i][j].getDisplayChar().equals(Consts.TRIPLE_LETTER)) {
					board[i][j].setLetterMultiplier(Consts.TRIPLE);
				} else if (board[i][j].getDisplayChar().equals(Consts.DOUBLE_WORD)) {
					board[i][j].setWordMultiplier(Consts.DOUBLE);
				} else if (board[i][j].getDisplayChar().equals(Consts.TRIPLE_WORD)) {
					board[i][j].setWordMultiplier(Consts.TRIPLE);
				}
			}
		}
		*/
		
		//instantiate the defaults
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				if (board[i][j].getLetterMultiplier() == Consts.DOUBLE) {
					board[i][j].setDefaultLetterMulti(Consts.DOUBLE);
				} else if (board[i][j].getLetterMultiplier() == Consts.TRIPLE) {
					board[i][j].setDefaultLetterMulti(Consts.TRIPLE);
				} else if (board[i][j].getWordMultiplier() == Consts.TRIPLE) {
					board[i][j].setDefaultWordMulti(Consts.TRIPLE);
				} else if (board[i][j].getWordMultiplier() == Consts.DOUBLE) {
					board[i][j].setDefaultWordMulti(Consts.DOUBLE);
				}
			}
		}

	}

}
