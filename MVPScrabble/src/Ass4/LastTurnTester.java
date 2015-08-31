package Ass4;

import Ass2.Consts;
import Ass3.UI;

public class LastTurnTester {
	public static void main(String[] args) {
		UI ui = new UI();
		ui.setTesting(Consts.ADJACENT_WORD_INPUT_FILE);
		ui.start();
		System.out.println(ui.getScrabble().getLastTurn());
	}
}
