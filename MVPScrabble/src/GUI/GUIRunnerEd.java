package GUI;

import Ass1.Frame;
import Ass1.Player;
import Ass1.Pool;
import Ass3.Scrabble;

public class GUIRunnerEd {
	public static void main (String[] args) {
		Pool pool = new Pool();
		@SuppressWarnings("unused")
		BoardDrawerEd bd = new BoardDrawerEd(new Scrabble(), new Player("Bob", new Frame(pool)), new Player("Jim", new Frame(pool)));
	}

}