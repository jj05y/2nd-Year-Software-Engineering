package Ass2;

import Ass1.Frame;
import Ass1.Player;
import Ass1.Pool;
import Ass1.Testing;
import Ass3.Scrabble;

public class BoardTest extends Testing {

	public static void main(String[] args) {

		Scrabble board = new Scrabble();
		Pool pool = new Pool();
		Player bob = new Player("Bob", new Frame(pool));
		String word;
		int numPass = 0;

		/*
		 * TESTING TIME!!
		 * 
		 * In this class, every time a word is going to be played, a test frame is filled including that
		 * word. (unless trying to play a word not in the frame).
		 */
		
		System.out.println("Testing board.java and associated classes.\n");
		/*
		 * Check a sample square for score value
		 */
		System.out.println("Checking that word multiplier at 0,0 is 3");
		if (check(board.getSquares()[0][0].getWordMultiplier(), Consts.TRIPLE)) {
			System.out.println("Word mulitpier of square 0,0 has been set correctly.\n");
			numPass++;
		} else {
			System.out.println("Word mulitpier of square 0,0 has not been set correctly.\n");
		}

		/*
		 * Testing whether or not a word can be played out of bounds.
		 */
		System.out.println("Playing a word out of bounds Expected: Error.");
		bob.getFrame().buildTestFrame(word = "BOUNDS");

		if (check(board.go(word, 17, 3, Consts.ACROSS, bob), Consts.MOVE_NOT_POSSIBLE)) {
			System.out.println("Error: Could not play word at (17, 3): Out of bounds.\n");
			numPass++;
		} else {
			System.out.println("Whoops test for word out of bounds failed.\n Unexpected result.\n");
		}

		/*
		 * Playing a word that doesn't connect to another.
		 */
		System.out.println("Playing a word that doesn't connect to another. Expected: Error.");
		board.reset();
		pool.reset();
		bob.getFrame().buildTestFrame(word = "BOUNDS");
		board.go(word, 7, 7, Consts.ACROSS, bob); // the first word is played as
												// normal
		pool.reset();
		bob.getFrame().buildTestFrame(word = "WHOOPS");
		if (check(board.go(word, 9, 7, Consts.ACROSS, bob), Consts.MOVE_NOT_POSSIBLE)) {
			System.out.println("Error: Could not play word at (9, 7): Doesn't connect to \"BOUNDS\".\n");
			numPass++;
		} else {
			System.out.println("Whoops test for word that doesn't connect failed.\n Unexpected result.\n");
		}

		/*
		 * Playing a word that isn't in the Frame.
		 */
		System.out.println("Third test: Playing a word that isn't in the Frame. Expected: Error.");
		board.reset();
		pool.reset();
		bob.getFrame().buildTestFrame("CHEESE");
		word = "BANANAS";
		if (check(board.go(word, 7, 7, Consts.ACROSS, bob), Consts.NOT_IN_FRAME)) {
			System.out.println("Error: Could not play \"Bananas\" at (7, 7): Word not in frame.\n");
			numPass++;
		} else {
			System.out.println("Whoops test for word that isn't in the Frame failed.\n Unexpected result.\n");
		}

		/*
		 * Plays a word on the start location, we will also test that score
		 * increments properly here.
		 */
		System.out.println("Playing a word on the start location. Expected pass.");
		board.reset();
		pool.reset();
		bob.getFrame().buildTestFrame(word = "STAR");
		int score = board.go(word, 7, 7, Consts.ACROSS, bob);
		bob.incrScore(score);
		if (check(bob.getScore(), 8)) {
			System.out.println("Passed: Word \"Star\" was played successfully and score of 8 was incremented.\n");
			numPass++;
		} else {
			System.out.println("Whoops test for word played and scroe incrememnted failed.\n Unexpected result.\n");
		}

		/*
		 * Check that the letters 'S' 'T' 'A' and 'R' are infact there
		 */
		System.out.println("Checking each letter of \"STAR\" individually.");
		char[] temparr = {'S','T','A','R'};
		for (int i = 0; i < 4; i++) {
			if (check(board.getSquares()[7][7+i].getTile().getLetter(), temparr[i])) {
				System.out.println("'" + temparr[i] + "'" + " is there.\n");
				numPass++;
			} else {
				System.out.println("'" + temparr[i] + "'" + " is not there.\n");
			}
		}

		/*
		 * Play an adjacent word to one previously played.
		 */
		System.out.println("Plays an adjacent word to one previously played. Expected pass.");
		board.reset();
		pool.reset();
		bob.getFrame().buildTestFrame(word = "NO");
		board.go(word, 7, 7, Consts.ACROSS, bob);
		pool.reset();
		bob.getFrame().buildTestFrame(word = "ON");
		if (check(board.go(word, 6, 7, Consts.ACROSS, bob))) {
			System.out.println("Passed: Word \"ON\" was played adjacent to \"NO\"\n");
			numPass++;
		} else {
			System.out.println("Whoops test for adjacent word being played failed.\n Unexpected result.\n");
		}

		/*
		 * To play an intersecting word.
		 */
		System.out.println("Plays an intersecting word to one previously played. Expected pass.");
		board.reset();
		pool.reset();
		bob.getFrame().buildTestFrame(word = "SUPER");
		board.go(word, 7, 7, Consts.ACROSS, bob);
		// System.out.println(board);
		pool.reset();
		bob.getFrame().buildTestFrame(word = "STAR");
		// System.out.println(bob);
		if (check(board.go(word, 7, 7, Consts.DOWN, bob))) {
			System.out.println("Passed: Word \"STAR\" was played successfully intersecting to \"SUPER\"\n");
			// System.out.println(board);
			numPass++;
		} else {
			System.out.println("Whoops test for intersecting word failed.\n Unexpected result.\n");
		}

		/*
		 * To play an extension to a word.
		 */
		System.out.println("Plays an extension to a word previously played. Expected pass.");
		board.reset();
		pool.reset();
		bob.getFrame().buildTestFrame(word = "STAR");
		board.go(word, 7, 4, Consts.ACROSS, bob);
		pool.reset();
		bob.getFrame().buildTestFrame("STRUCK");
		word = "STARSTRUCK";
		if (check(board.go(word, 7, 4, Consts.ACROSS, bob))) {
			System.out.println("Passed: Word \"STARSTRUCK\" was played successfully extending \"STAR\"\n");
			numPass++;
		} else {
			System.out.println("Whoops test for word being extended failed.\n Unexpected result.\n");
		}

		/*
		 * To play an overlapping word.
		 */
		System.out.println("Plays an overlapping word on a word previously played. Expected error.");
		board.reset();
		pool.reset();
		bob.getFrame().buildTestFrame(word = "STAR");
		board.go(word, 7, 7, Consts.ACROSS, bob);
		pool.reset();
		bob.getFrame().buildTestFrame(word = "BANANAS");
		if (check(board.go(word, 7, 7, Consts.ACROSS, bob), Consts.MOVE_NOT_POSSIBLE)) {
			System.out.println("Error, cannot play word \"Bananas\" on top of word \"Star\"\n");
			numPass++;
		} else {
			System.out.println("Whoops test for overlapping word failed.\n Unexpected result.\n");
		}

		/*
		 * Print the board.
		 */
		//play some words first:
		pool.reset();
		bob.getFrame().buildTestFrame(word = "SUPER");
		board.go(word, 7, 7, Consts.DOWN, bob);
		pool.reset();
		bob.getFrame().buildTestFrame(word = "AWESOME");
		board.go(word, 10, 1, Consts.ACROSS, bob);
		pool.reset();
		bob.getFrame().buildTestFrame(word = "WOW");
		board.go(word, 8, 2, Consts.DOWN, bob);
		
		System.out.println("Board before reseting. (with some random words)\n" + board);

		/*
		 * Reset the board.
		 */
		board.reset();
		System.out.println("Board after reseting.\n" + board);

		/*
		 * Conclusion
		 */
		System.out.println("Test Results:");
		if (check(numPass, numTests)) {
			System.out.println("***All tests PASSED***\n");
		} else {
			System.out.println("***Some tests FAILED***\n");
		}

	}

}
