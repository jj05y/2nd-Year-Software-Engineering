package Ass3;

import Ass2.Consts;

public class UIboundsTester {
	public static void main(String[] args) {
		UI ui = new UI();
		ui.setTesting(Consts.BONKERS_INPUT_FILE);
		ui.start();
	}
}
