package Ass2;

import Ass1.Frame;
import Ass1.Player;
import Ass1.Pool;
import Ass3.Scrabble;
//import GUI.BoardDrawerJoe;

public class BoardExample {
	
	/**
	 * This a sample run through main which bob plays some sets of words,
	 * the board is reset between each set of words
	 */
	public static void main (String[] args) {

		Scrabble board = new Scrabble();
		Pool pool = new Pool();
		Player bob = new Player("Bob", new Frame(pool));
		String word;
		
		word = "BANG";
		testPlay(board, word, 7, 5, Consts.ACROSS, bob);
		pool.reset();
		word = "STAR";
		testPlay(board, word, 5, 6, Consts.DOWN, bob);
		pool.reset();
		word = "STATE";
		testPlay(board, word, 5, 6, Consts.ACROSS, bob);
		pool.reset();
		word = "EATERY";
		testPlay(board, word, 5, 10, Consts.DOWN, bob);
		pool.reset();
		word = "HAIRY";
		testPlay(board, word, 10, 6, Consts.ACROSS, bob);
		pool.reset();
		word = "HAM";
		testPlay(board, word, 10, 6, Consts.DOWN, bob);
		pool.reset();
		word = "LOCUM";
		testPlay(board, word, 12, 2, Consts.ACROSS, bob);
	//	new BoardDrawerJoe(board.getSquares(), bob); 

		
		System.out.println("#################RESET#####################\n");
		board.reset();
		bob.reset("Bob", new Frame(pool));
		

		pool.reset();
		word = "ON";
		testPlay(board, word, 7, 7, Consts.ACROSS, bob);
		pool.reset();
		word = "NO";
		testPlay(board, word, 6, 7, Consts.ACROSS, bob);
		
		System.out.println("#################RESET#####################\n");
		board.reset();
		bob.reset("Bob", new Frame(pool));
		
		pool.reset();
		word = "STAR";
		testPlay(board, word, 7, 5, Consts.ACROSS, bob);
		pool.reset();
		word = "SARCASM";
		testPlay(board, word, 7, 5, Consts.ACROSS, bob);
		pool.reset();
		word = "STARS";
		testPlay(board, word, 7, 5, Consts.DOWN, bob);
		pool.reset();
		word = "STARS";
		testPlay(board, word, 7, 5, Consts.ACROSS, bob);
		
		System.out.println("#################RESET#####################\n");
		board.reset();
		bob.reset("Bob", new Frame(pool));
		
		pool.reset();
		word = "STAR";
		testPlay(board, word, 7, 5, Consts.ACROSS, bob);
		pool.reset();
		word = "STAR";
		testPlay(board, word, 7, 5, Consts.ACROSS, bob);
		
		
	}

	/**
	 * @param b - the board on which to play the word
	 * @param s - the word to play
	 * @param i - the i index of the start of the word
	 * @param j - the j index of the start of the word
	 * @param dir - the direction in which to play the word
	 * @param p - the player playing the word
	 */
	private static void testPlay(Scrabble b, String s, int i, int j, int dir, Player p) {
		
		p.getFrame().buildTestFrame(s);
		int score;
		
		switch (score = b.go(s, i, j, dir, p)) {
			case Consts.MOVE_NOT_POSSIBLE:
				System.out.println("Playing " + s + " is not possible at " + i + "," + j + "\n");
				break;
			case Consts.NOT_IN_FRAME:
				System.out.println(s + " not in frame\n"); 
				break;
			case Consts.NOT_IN_DICTIONARY:
				System.out.println(s + " not in dictionary\n");
				break;
			case Consts.ADJACENT_WORD_NOT_VALID:
				System.out.println("Adjacent Word Not Valid\n");
				break;
			case Consts.ALREADY_PLAYED:
				System.out.println(Consts.SORRY_ALREADY_PLAYED);
				break;
			default:
				System.out.println("Played " + s + " Score: " + score);
				p.incrScore(score);
				System.out.println(b);
				System.out.println(p + "\n");
		}	
	}
}
