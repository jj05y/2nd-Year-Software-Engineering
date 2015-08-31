package Ass3;

import Ass1.Testing;
import Ass2.Consts;

public class UITester extends Testing {

	public static void main(String[] args) {
		int numPasses = 0;
		UI ui = new UI();

		// Assert: Input is parsed to a Turn and word placed correctly
		System.out.println("#########################################################################");
		System.out.println("TESTING PARSING:");
		System.out.println("#########################################################################");
		System.out.println("\n--------------------------------------------------------------------");
		System.out.println("TESTING PARSING CORRECT INPUT");
		System.out.println("--------------------------------------------------------------------");
		ui.setTesting(Consts.ONE_WORD);
		ui.start();
		System.out.println("After parsing the string \"7,7 ACROSS STAR\"");
		System.out.println("Checking each letter of \"STAR\" individually.");
		char[] temparr = { 'S', 'T', 'A', 'R' };
		for (int i = 0; i < 4; i++) {
			if (check(ui.getScrabble().getSquares()[7][7 + i].getTile().getLetter(), temparr[i])) {
				System.out.println("'" + temparr[i] + "'" + " is there.\n");
				numPasses++;
			} else {
				System.out.println("'" + temparr[i] + "'" + " is not there.\n");
			}
		}
		
		//INVALID INPUT
		System.out.println("\n--------------------------------------------------------------------");
		System.out.println("TESTING PARSING INCORRECT INPUT");
		System.out.println("--------------------------------------------------------------------");
		System.out.println("Here Bob plays a lonely game and firstly inputs incorrect input\n and then inputs an incorrect direction");
		ui.reset();
		ui.setTesting(Consts.INVALID_INPUT);
		ui.start();
		
		// HELP PASS QUIT
		System.out.println("\n--------------------------------------------------------------------");
		System.out.println("TESTING HELP PASS RENEW QUIT:");
		System.out.println("--------------------------------------------------------------------");
		System.out.println("\nHere a scripted game is played, with two players.\nPlayer one, Bob, asks for help and then passes his go.\nPlayer two, Ted, then changes the letters ING and forgoes his turn.\nBob then quits the game.\n");
		ui.reset();
		ui.setTesting(Consts.HELP_PASS_RENEW_QUIT);
		ui.start();
		
		

		// Assert: Score is correct for all cases (loads)
		System.out.println("#########################################################################");
		System.out.println("TESTING SCORING:");
		System.out.println("#########################################################################");

		System.out.println("\n--------------------------------------------------------------------");
		System.out.println("TESTING CONVENTIONAL SCORING:");
		System.out.println("--------------------------------------------------------------------");
		ui.reset();
		ui.setTesting(Consts.CONVENTIONAL_TURN_INPUT_FILE);
		ui.start();
		System.out.println("Tests for conventional scoring");
		System.out.println("------------------------------");
		if (check(ui.getPlayers().get(0).getScore(), 16)) {
			System.out.println("*PASS* Correct score given for first word played\n");
			numPasses++;
		} else {
			System.out.println("*FAIL* Correct score not given for first word played\n");
		}
		if (check(ui.getPlayers().get(1).getScore(), 12)) {
			System.out.println("*PASS* Correct score given for 2nd word played conventionaly\n");
			numPasses++;
		} else {
			System.out.println("*FAIL* Correct score not given for 2nd word played conventionaly\n");
		}

		System.out.println("\n--------------------------------------------------------------------");
		System.out.println("TESTING ADJACENT WORD SCORING:");
		System.out.println("--------------------------------------------------------------------");
		ui.reset();
		ui.setTesting(Consts.ADJACENT_WORD_INPUT_FILE);
		ui.start();
		System.out.println("Tests for adjacent word scoring");
		System.out.println("-------------------------------");
		if (check(ui.getPlayers().get(0).getScore(), 8)) {
			System.out.println("*PASS* Correct score given for first word played\n");
			numPasses++;
		} else {
			System.out.println("*FAIL* Correct score not given for first word played\n");
		}
		if (check(ui.getPlayers().get(1).getScore(), 4)) {
			System.out.println("*PASS* Correct score given for 2nd word played creating adjacent words\n");
			numPasses++;
		} else {
			System.out.println("*FAIL* Correct score not given for 2nd word played creating adjacent words\n");
		}

		System.out.println("\n--------------------------------------------------------------------");
		System.out.println("TESTING SUFFIX WORD SCORING:");
		System.out.println("--------------------------------------------------------------------");
		ui.reset();
		ui.setTesting(Consts.SUFFIX_EXTENSION_INPUT_FILE);
		ui.start();
		System.out.println("Tests for suffix word scoring");
		System.out.println("-----------------------------");
		if (check(ui.getPlayers().get(0).getScore(), 9)) {
			System.out.println("*PASS* Correct score given for first word played\n");
			numPasses++;
		} else {
			System.out.println("*FAIL* Correct score not given for first word played\n");
		}
		if (check(ui.getPlayers().get(1).getScore(), 6)) {
			System.out.println("*PASS* Correct score given for 2nd word played using a suffix\n");
			numPasses++;
		} else {
			System.out.println("*FAIL* Correct score not given for 2nd word played using a suffix\n");
		}

		System.out.println("\n--------------------------------------------------------------------");
		System.out.println("TESTING PREFIX WORD SCORING:");
		System.out.println("--------------------------------------------------------------------");
		ui.reset();
		ui.setTesting(Consts.PREFIX_EXTENSTION_INPUT_FILE);
		ui.start();
		System.out.println("Tests for prefix word scoring");
		System.out.println("------------------------------");

		if (check(ui.getPlayers().get(0).getScore(), 10)) {
			System.out.println("*PASS* Correct score given for first word played\n");
			numPasses++;
		} else {
			System.out.println("*FAIL* Correct score not given for first word played\n");
		}
		if (check(ui.getPlayers().get(1).getScore(), 8)) {
			System.out.println("*PASS* Correct score given for 2nd word played using a prefix\n");
			numPasses++;
		} else {
			System.out.println("*FAIL* Correct score not given for 2nd word played using a prefix\n");
		}

	
		// Assert: Help, Exit, etc work 
		

		
		//final
		System.out.println("\n\nConclusion:\nNumber of automated tests passed vs number of automated tests");
		if (check(numPasses, numTests)) {
			System.out.println("***All Tests Passed***\n\n");
		} else {
			System.out.println("***Some Tests Failed***\n\n");
		}
	}
}
