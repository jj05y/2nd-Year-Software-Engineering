package Ass4;

import Ass2.Consts;
import Ass3.UI;

public class ChallengeTester {
	public static void main(String[] args) {
		UI ui = new UI();
		ui.setTesting(Consts.CHALLENGE_TEST_FILE);
		ui.start();
	}
}
