package Ass3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import Ass1.Frame;
import Ass1.Player;
import Ass1.Pool;
import Ass2.Consts;

/**
 * UI takes user input and connects the player to the game and the board itself.
 * 
 * @author MVP
 * @version 0.1
 * @since 18/02/15
 * 
 */

public class UI {

	/**
	 * scrabble, players, pool and the scanner for input are all instantiated
	 * here globally for this class.
	 */
	private Scrabble scrabble;
	private ArrayList<Player> players;
	private Scanner in;
	private Pool pool;
	boolean testing;
	int currentPlayer = 0;
	private boolean challangeable;

	public UI() {
		scrabble = new Scrabble();
		players = new ArrayList<Player>();
		pool = new Pool();
		in = new Scanner(System.in);
		testing = false;
		challangeable = false;
	}

	/**
	 * @param inputFile - takes in text file for efficient testing
	 */
	public void setTesting(String inputFile) {
		testing = true;
		in = new Scanner(getClass().getResourceAsStream(inputFile));
	}

	/**
	 * used to reset the pool and board
	 */
	public void reset() {
		scrabble.reset();
		pool.reset();
	}

	/**
	 * begins the game loop
	 */
	public void start() {
		if (!testing) {
			System.out.println(scrabble);
		}
		makePlayers(); // this method will say hi, get #players,
		// and player names,

		currentPlayer = 0;
		int score;
		Turn turn;

		while (Consts.PLAYING) { // this loop iterates the players

			// print things
			if (!testing) System.out.println(Consts.NUM_TILES_IN_POOL + pool.getNumTiles() + "\n"); //when testing, the pool is reset alot, this will be inaccurate
			System.out.println(Consts.ITS_YOUR_GO + players.get(currentPlayer));
			if (testing) {
				pool.reset(); // pool has to be reset in order to guarantee that
								// the test frame can be built.
				players.get(currentPlayer).getFrame().buildTestFrame(in.nextLine());
				System.out.println(Consts.FORCED_FRAME + "\n" + players.get(currentPlayer));
			}

			do { // this loop allows a players turn to be repeated if they do
					// something like ask for help
				turn = parse(in.nextLine());

				if (testing) {
					if (!in.hasNext()) { // breaks game loop after testing
						return;
					}
				}
				if (turn != Consts.TURN_FINISHED) {
					challangeable = false;
					switch (score = scrabble.go(turn.getWord(), turn.getI(), turn.getJ(), turn.getDir(), players.get(currentPlayer))) {

					case Consts.MOVE_NOT_POSSIBLE:
						System.out.println(Consts.SORRY_MOVE_NOT_POSSIBLE);
						break;

					// case Consts.NOT_IN_DICTIONARY:
					// turn = Consts.TURN_FINISHED;
					// System.out.println(Consts.SORRY_NOT_IN_DICTIONARY);
					// break;

					case Consts.NOT_IN_FRAME:
						System.out.println(Consts.SORRY_NOT_IN_FRAME);
						break;

					case Consts.ADJACENT_WORD_NOT_VALID:
						turn = Consts.TURN_FINISHED;
						System.out.println(Consts.SORRY_ADJACENT_WORD_NOT_VALID);
						break;

					case Consts.ALREADY_PLAYED:
						System.out.println(Consts.SORRY_ALREADY_PLAYED);
						break;

					default:
						players.get(currentPlayer).incrScore(score);
						System.out.println(players.get(currentPlayer).getName() + " played " + turn.getWord() + " and recieved " + score
								+ " points.");
						turn = Consts.TURN_FINISHED;
						challangeable = true;
						break;

					}
				}
			} while (turn != Consts.TURN_FINISHED);

			if (players.get(currentPlayer).getFrame().isEmpty()) {
				gameOver(currentPlayer);
			} else {
				if (!testing) {
					System.out.println(scrabble);
				}
				currentPlayer = (currentPlayer + 1) % players.size();
			}

		}

	}

	/**
	 * Parses user input
	 * 
	 * @param input - the initial user input
	 * @return - returns a correctly parsed turn, OR, null if the players go is
	 *         to be skipped
	 */
	private Turn parse(String input) {

		while (Consts.RECIEVING_INPUT) {
			switch (input.split(" ")[0].toLowerCase()) {
			case Consts.CHALLENGE:
				if (!challangeable) {
					System.out.println(Consts.NOT_CHALLENGABLE);
					break;
				}
				if (scrabble.challenge(players.get(currentPlayer == 0 ? players.size()-1 : currentPlayer - 1), pool) == Consts.CANT_FIND_WORD_IN_DICT) {
					System.out.println(Consts.CHALLENGE_SUCCESS);
					System.out.println(scrabble);
					if (!testing) System.out.println(Consts.NUM_TILES_IN_POOL + pool.getNumTiles() + "\n"); //when testing, the pool is reset alot, this will be inaccurate
					System.out.println(players.get(currentPlayer));
					break;
				} else {
					System.out.println(Consts.WORD_EXISTS_IN_DICT);
					return Consts.TURN_FINISHED;
				}

			case Consts.FINISHED:
				gameOver(Consts.GAME_FINISHED);
				return Consts.TURN_FINISHED;
			case Consts.HELP:
				System.out.println(Consts.HELP_STRING);
				break;
			case Consts.EXCHANGE:
				challangeable = false;
				try {
					if (players.get(currentPlayer).getFrame().renewSelectTiles(input.split(" ")[1].toUpperCase()) == Consts.SUCCESS) {
						System.out.println(Consts.LETTERS_EXCHANGED + "\n" + players.get(currentPlayer));
						return Consts.TURN_FINISHED;

					}
				} catch (Exception e) {
				System.out.println(Consts.NO_LETTERS);
					break;
				}
				break;
			case Consts.PASS:
				System.out.println("Turn Passed\n");
				challangeable = false;
				return Consts.TURN_FINISHED;
			default:
				int row,
				col,
				dir;
				String word;
				try {
					row = Integer.parseInt(input.split(" ")[0].split(",")[0]);
					col = Integer.parseInt(input.split(" ")[0].split(",")[1]);
					if ((input.split(" ")[1].toLowerCase().compareTo(Consts.ACROSS_STRING) == 0)) {
						dir = 1;
					} else if (input.split(" ")[1].toLowerCase().compareTo(Consts.DOWN_STRING) == 0) {
						dir = 0;
					} else {
						dir = 7;
					}
					word = input.split(" ")[2].toUpperCase();
				} catch (Exception e) {
					System.out.println(Consts.INVALID_TURN_FORMAT);
					break;
				}

				if (testing)
					System.out.println("row: " + row + " col: " + col + " dir: " + dir + " word: " + word);
				if (row < 0 || row > 14 || col < 0 || col > 14) {
					System.out.println(Consts.INVALID_COORDS);
					break;
				}
				if (dir > 1 || dir < 0) {
					System.out.println(Consts.INVALID_DIR);
					break;
				}
				return new Turn(word, row, col, dir);

			}
			if (testing) {
				players.get(currentPlayer).getFrame().buildTestFrame(in.nextLine());
			}
			System.out.println(Consts.NEED_NEW_INPUT);
			input = in.nextLine();

		}

	}

	/**
	 * interacts with player/frame class to create multiple players for the game
	 * and assign them a frame.
	 */
	private void makePlayers() {
		if (!players.isEmpty()) {
			players.clear();
		}
		System.out.println(Consts.ENTER_PLAYER_NAMES);
		String name;
		int i = 0;
		while ((name = in.nextLine()).compareToIgnoreCase(Consts.DONE) != 0 && players.size() <= Consts.MAX_NUM_PLAYERS) {
			players.add(new Player(name, new Frame(pool)));
			System.out.println(Consts.MADE + players.get(i++).getName() + "\n");
		}

	}

	/**
	 * prints final state of the board wraps up and exits the game. calculates
	 * final scores and bonus/negative points and declares winner
	 * 
	 * @param finisher - this is the player who has won by emptying their frame.
	 */
	private void gameOver(int finisher) {
		int bonusPoints = 0;

		System.out.println("Game Over, final board state:");

		// final board
		System.out.println(scrabble);

		if (finisher != -1) {
			// subtract value of score in frame from total score.
			for (int k = 0; k < players.size(); k++) {
				if (!players.get(k).getFrame().isEmpty()) {
					players.get(k).incrScore(-players.get(k).getFrame().scoreInFrame());
					bonusPoints += players.get(k).getFrame().scoreInFrame();
				}
			}

			// add bonus points to emptied frame
			players.get(finisher).incrScore(bonusPoints);
		}

		Collections.sort(players);

		// prints players in order
		for (int i = 0; i < players.size(); i++) {
			System.out.println(i + 1 + ":\t" + players.get(i).getName() + "\t" + players.get(i).getScore());
		}

		// declare the winner.
		System.out.println("Congratulations " + players.get(0).getName() + "!!!!");
		if (!testing) {
			System.exit(0);
		} else {
			return;
		}
	}

	/**
	 * gets the players
	 * 
	 * @return players
	 */
	public ArrayList<Player> getPlayers() {
		return players;
	}

	/**
	 * gets the scrabble game
	 * 
	 * @return scrabble
	 */
	public Scrabble getScrabble() {
		return scrabble;
	}
}