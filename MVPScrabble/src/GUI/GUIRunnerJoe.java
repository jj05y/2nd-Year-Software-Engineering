package GUI;

import Ass1.Frame;
import Ass1.Player;
import Ass1.Pool;
import Ass3.Scrabble;

public class GUIRunnerJoe {
	public static void main (String[] args) {
		@SuppressWarnings("unused")
		BoardDrawerJoe bd = new BoardDrawerJoe(new Scrabble().getSquares(), new Player("Bob", new Frame(new Pool())));
	}

}
