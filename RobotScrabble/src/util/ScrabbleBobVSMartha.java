package util;

import java.io.FileNotFoundException;

public class ScrabbleBobVSMartha{

	private static int NUM_PLAYERS = 2;
	private static int BONUS = 50;
	private static int BOBBOT_ID = 0;
	private static int MARTHABOT_ID = 1;

	/*
	 * We modified Scrabble.java to allow it to play 2 bots together,
	 * We also added some functionality like printing average times
	 */
	public static void main(String[] args) throws FileNotFoundException {

		Board board = new Board();
		Pool pool = new Pool();
		Player[] players = new Player[NUM_PLAYERS];
		UI ui = new UI();
		Robot Bobbot = new MoreComprehensiveBot();
		Robot Marthabot = new Bot();
		int currentPlayerId = 0, prevPlayerId;
		Player currentPlayer, prevPlayer;
		Frame currentFrame, prevFrame;
		Word word;
		char lowestTile;
		boolean tileDraw = false;
		String letters;
		char[] tiles = new char[NUM_PLAYERS];
		int commandCode, checkCode, totalWordScore;
		boolean frameWasFull, turnOver = false, gameOver = false, allOverPassLimit;
		int[] unusedScore = new int[NUM_PLAYERS];
		int totalUnused;
		boolean prevWasPlay = false, challengeDone = false;
		Dictionary dictionary = new Dictionary();

		// Initialize players
		ui.displayGameStart();
		for (int i = 0; i < NUM_PLAYERS; i++) {
			players[i] = new Player();
		}
		players[BOBBOT_ID].setName("Bob");
		players[MARTHABOT_ID].setName("Martha");

		// Decide who starts
		do {
			for (int i = 0; i < NUM_PLAYERS; i++) {
				tiles[i] = pool.getRandomTile().getFace();
				ui.displayTile(players[i], tiles[i]);
			}
			lowestTile = tiles[0];
			currentPlayerId = 0;
			tileDraw = false;
			for (int i = 1; i < NUM_PLAYERS; i++) {
				if (tiles[i] < lowestTile) {
					lowestTile = tiles[i];
					currentPlayerId = i;
					tileDraw = false;
				} else if (tiles[i] == lowestTile) {
					tileDraw = true;
				}
			}
			if (!tileDraw) {
				ui.displayStarter(players[currentPlayerId]);
			} else {
				ui.displayStarterDraw();
			}
		} while (tileDraw);

		// Play the game
		gameOver = false;
		do {
			currentPlayer = players[currentPlayerId];
			currentFrame = currentPlayer.getFrame();
			currentFrame.refill(pool);
			ui.displayBoard(board);
			ui.displayScores(players);
			ui.displayPoolSize(pool);
			challengeDone = false;
			do {
				if (currentPlayerId == BOBBOT_ID) {
					ui.displayPrompt(currentPlayer);
					commandCode = Bobbot.getCommand(currentPlayer, board, dictionary);
					ui.displayCommand(players[BOBBOT_ID], commandCode, Bobbot.getWord(), Bobbot.getLetters());
				} else {
					ui.displayPrompt(currentPlayer);
					commandCode = Marthabot.getCommand(currentPlayer, board, dictionary);
					ui.displayCommand(players[MARTHABOT_ID], commandCode, Marthabot.getWord(), Marthabot.getLetters());
				}
				switch (commandCode) {
				case UI.COMMAND_QUIT:
					turnOver = true;
					gameOver = true;
					break;
				case UI.COMMAND_PASS:
					turnOver = true;
					currentPlayer.pass();
					turnOver = true;
					allOverPassLimit = true;
					for (int i = 0; i < NUM_PLAYERS; i++) {
						allOverPassLimit = allOverPassLimit && players[i].isOverPassLimit();
					}
					if (allOverPassLimit) {
						gameOver = true;
					}
					prevWasPlay = false;
					break;
				case UI.COMMAND_HELP:
					ui.displayHelp();
					turnOver = false;
					break;
				case UI.COMMAND_EXCHANGE:
					if (currentPlayerId == BOBBOT_ID) {
						letters = Bobbot.getLetters();
					} else {
						letters = Marthabot.getLetters();
					}
					if (!currentFrame.isAvailable(letters)) {
						ui.displayError(UI.EXCHANGE_NOT_AVAILABLE);
						turnOver = false;
					} else if (pool.size() < letters.length()) {
						ui.displayError(UI.EXCHANGE_NOT_ENOUGH_IN_POOL);
						turnOver = false;
					} else {
						currentFrame.exchange(letters, pool);
						turnOver = true;
						prevWasPlay = false;
					}
					break;
				case UI.COMMAND_PLAY:
					if (currentPlayerId == BOBBOT_ID) {
						word = Bobbot.getWord();
					} else {
						word = Marthabot.getWord();
					}
					checkCode = board.checkWord(word, currentFrame);
					if (checkCode != UI.WORD_OK) {
						ui.displayError(checkCode);
						turnOver = false;
					} else {
						frameWasFull = currentFrame.isFull();
						totalWordScore = board.setWord(word, currentFrame);
						if (currentFrame.isEmpty() && frameWasFull) {
							totalWordScore = totalWordScore + BONUS;
						}
						ui.displayWordScore(totalWordScore);
						currentPlayer.addScore(totalWordScore);
						turnOver = true;
						prevWasPlay = true;
						if (currentFrame.isEmpty() && pool.isEmpty()) {
							gameOver = true;
						}
					}
					break;
				case UI.COMMAND_CHALLENGE:
					if (challengeDone) {
						ui.displayError(UI.CHALLENGE_REPEAT);
						turnOver = false;
					} else if (board.isFirstPlay()) {
						ui.displayError(UI.CHALLENGE_FIRST_PLAY);
						turnOver = false;
					} else if (!prevWasPlay) {
						ui.displayError(UI.CHALLENGE_PREV_NOT_PLAY);
						turnOver = false;
					} else if (!dictionary.areWords(board.getWords())) {
						ui.displayChallengeSuccess();
						prevPlayerId = currentPlayerId - 1;
						if (prevPlayerId < 0) {
							prevPlayerId = NUM_PLAYERS - 1;
						}
						prevPlayer = players[prevPlayerId];
						prevFrame = prevPlayer.getFrame();
						board.undo();
						prevPlayer.undo();
						prevFrame.undo();
						ui.displayBoard(board);
						ui.displayScores(players);
						ui.displayPoolSize(pool);
						challengeDone = true;
						turnOver = false;
					} else {
						ui.displayChallengeFail();
						turnOver = true;
						challengeDone = true;
						prevWasPlay = false;
					}
					break;
				}
			} while (!turnOver);
			if (!gameOver) {
				currentPlayerId++;
				if (currentPlayerId > NUM_PLAYERS - 1) {
					currentPlayerId = 0;
				}
			}
		} while (!gameOver);

		totalUnused = 0;
		for (int i = 0; i < NUM_PLAYERS; i++) {
			unusedScore[i] = players[i].unusedLettersScore();
			players[i].addScore(-unusedScore[i]);
			totalUnused = totalUnused + unusedScore[i];
		}
		if (unusedScore[currentPlayerId] == 0) {
			players[currentPlayerId].addScore(totalUnused);
		}
		ui.displayResult(players);
		System.out.println("Avg time for Bob: " + computeAvgTime(Bobbot));
		System.out.println("Avg time for Martha: " + computeAvgTime(Marthabot));
		System.out.println("GAME OVER");

		return;
	}

	private static double computeAvgTime(Robot bot) {
		double sum = 0;
		try {
			for (double d : bot.getTimes()) {
				sum += d;
			}
			return sum / bot.getTimes().size();
		} catch (Exception e) {
			return 0;
		}

	}

}
