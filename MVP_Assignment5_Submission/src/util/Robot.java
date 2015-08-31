package util;

import java.util.ArrayList;

/**
 * 
 * @author MVP
 *	This interface defines a Robot
 *
 */
public interface Robot {
	ArrayList<Double> getTimes();
	int getCommand(Player currentPlayer, Board board, Dictionary dictionary);
	Word getWord();
	String getLetters();
}
