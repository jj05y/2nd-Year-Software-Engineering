package Ass2;

import java.util.ArrayList;

import Ass1.Tile;
import Ass3.Turn;

/**
 * contains constants from throughout the program 
 * 
 * @author MVP
 * @version 0.1
 * @since 18/02/15
 * 
 */
public final class Consts {
	public static final int BOARD_SIZE = 15; 
	public static final int FRAME_SIZE = 7;
	
	public static final int TRIPLE = 3;
	public static final int DOUBLE = 2;
	public static final int SINGLE = 1;
	
	public static final int ONE = 1;
	public static final int ZERO = 0;
	
	public static final int POOL_EMPTY = -1;
	public static final int NOT_FOUND = -1;
	public static final int SUCCESS = 0;
	public static final int GAME_FINISHED = -1;
	
	public static final int FOUND_WORD_IN_DICT = 0;
	public static final int CANT_FIND_WORD_IN_DICT = -1;
	public static final int LETTERS_NOT_IN_FRAME = -1;
	public static final int NOT_IN_DICTIONARY = -3;
	public static final int MOVE_NOT_POSSIBLE = -1;
	public static final int NOT_IN_FRAME = -2;
	public static final int ADJACENT_WORD_NOT_VALID = -4;
	public static final int ALREADY_PLAYED = -5;
	public static final int FAIL = 7;

	
	public static final int FIRST_LETTER = 1;
	public static final int LAST_LETTER = 3;
	public static final int MIDDLE_LETTER = 2;
	public static final int ALL_LETTERS = 0; 
	
	public static final int MAX_NUM_PLAYERS = 4;
	
	public static final int ACROSS = 1;
	public static final int DOWN = 0;
	public static final int NA = 0;
	
	public static final char A = 'A';
	
	public static final String BLANK_SQUARE = "   ";
	public static final String DOUBLE_WORD = "d_w";
	public static final String DOUBLE_LETTER = "d_l";
	public static final String TRIPLE_WORD = "t_w";
	public static final String TRIPLE_LETTER = "t_l";
	public static final String STAR = " * ";
	
	public static final String DICTIONARY_FILE = "sowpods";
	public static final String CONVENTIONAL_TURN_INPUT_FILE = "conventional";
	public static final String ADJACENT_WORD_INPUT_FILE = "adjacent";
	public static final String PREFIX_EXTENSTION_INPUT_FILE = "prefix";
	public static final String SUFFIX_EXTENSION_INPUT_FILE = "suffix";
	public static final String BONKERS_INPUT_FILE = "bonkers";
	public static final String ONE_WORD = "oneword";
	public static final String CHALLENGE_TEST_FILE = "challenge";

	public static final String HELP_PASS_RENEW_QUIT = "helppassquit";
	public static final String INVALID_INPUT = "invalidinput";

	public static final boolean RECIEVING_INPUT = true;
	public static final boolean PLAYING = true;

	public static final String CHALLENGE_SUCCESS = "Challege success, Word does not exist in dictionary. Last turn has been undone and current player can go again.";
	public static final String WORD_EXISTS_IN_DICT = "Word exists in dictionary. Challenger loses their turn.\n";
	public static final String ENTER_PLAYER_NAMES = "***MVPScrabble***\nPlease enter up to 4 player names (\"done\" when finished)";
	public static final String SORRY_NOT_IN_FRAME = "Sorry, those letters aren't in your frame, please enter your turn again: ";
	public static final String SORRY_NOT_IN_DICTIONARY = "Sorry, that word is not in the dictionary, You loose your turn.";
	public static final String SORRY_MOVE_NOT_POSSIBLE = "Sorry, that word does not fit there, please enter your turn again: ";
	public static final String SORRY_ADJACENT_WORD_NOT_VALID = "Sorry, playing that word creates another word which is not in the dictionary, you loose your turn.";
	public static final String SORRY_ALREADY_PLAYED = "Sorry, that word has already been played, Please enter your turn again.";
	public static final String NO_LETTERS = "You didn't specify any letters to exchange.\n";
	public static final String NEED_NEW_INPUT = "Please re-enter input, type \"help\" for help.\n";
	public static final String NOT_CHALLENGABLE = "You cannot challenge at this point";
	public static final String FORCED_FRAME = "A forced frame as been built for this test:";
	public static final String NUM_TILES_IN_POOL = "Number Tiles in Pool: ";
	public static final String LETTERS_EXCHANGED = "Letters Exchanged";
	public static final String INVALID_DIR = "Invalid Direction";
	public static final String INVALID_COORDS = "Invalid Co-ordinates";
	public static final String INVALID_TURN_FORMAT = "Invalid turn format.";
	public static final String DONE = "done";
	public static final String FINISHED = "finished";
	public static final String MADE = "---Made player---\n";
	public static final String ITS_YOUR_GO = "Its your turn\n";
	public static final String HELP = "help";
	public static final String HELP_STRING = "This is the help menu *Queue elevator music*\nTo play a word, enter it in a format of coordinates, direction and the word\nFor example: '7,7 Across bananas'\nType 'Exchange XYZ' to exchange letters XYZ\nType 'Pass' to pass your go\nType 'Finished' to quit the game\nAll input is case insensitve\n";
	public static final String EXCHANGE = "exchange";
	public static final String PASS = "pass";
	public static final String ACROSS_STRING = "across";
	public static final String DOWN_STRING = "down";
	public static final String CHALLENGE = "challenge";
	
	public static final String OUT_OF_BOUNDS = null;
	public static final ArrayList<Tile> WORD_NOT_IN_FRAME = null;
	public static final Turn TURN_FINISHED = null;
	public static final Turn INCORRECT_INPUT_STRUCTURE = null;

	
	

	
	

	
	

	
}
