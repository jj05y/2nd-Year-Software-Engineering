package Ass1;

import Ass2.Consts;

public class PlayerTest extends Testing {

	public static void main(String args[]) {

		Pool myPool = new Pool();
		Player player1 = new Player("Harold", new Frame(myPool));
		Player player2 = new Player("Ted", new Frame(myPool));
		Frame testFrame = new Frame(myPool);
		int numPasses = Consts.ZERO;
		

		// ///////////////////////////////////////////////////////////////////////
		// PLAYER TESTS
		// ///////////////////////////////////////////////////////////////////////

		System.out.println("\n___Player Testing:___\n");

		if (check(player1.getName(), "Harold")) {
			System.out.println("*PASS* Player one's name has been set correctly!\n");
			numPasses++;
		} else {
			System.out.println("*FAIL* Wheres player one's name??\n");
		}

		if (check(player2.getName(), "Ted")) {
			System.out.println("*PASS* Player two's name has been set correctly!\n");
			numPasses++;
		} else {
			System.out.println("*FAIL* Wheres player two's name??\n");
		}

		if (check(player1.getScore(), player2.getScore(), 0)) {
			System.out.println("*PASS* Both players are initialised to score = 0\n");
			numPasses++;
		} else {
			System.out
					.println("*FAIL* There is something wrong with the player scores!\n");
		}

		if (!check(player1.getFrame(), player2.getFrame())) {
			System.out.println("*PASS* Players frames have been set correctly\n");
			numPasses++;
		} else {
			System.out.println("*FAIL* Why are the Frames the exact same????\n");
		}
		
		player1.incrScore(4);
		if (check(player1.getScore(), 4)) {
			System.out.println("*PASS* The score incremented properly\n");
			numPasses++;
		} else {
			System.out.println("*FAIL* The score isn't incrementing properly.\n");
		}

		// ///////////////////////////////////////////////////////////////////////
		// FRAME TESTS
		// ///////////////////////////////////////////////////////////////////////

		System.out.println("\n___Testing for frame___\n");
		
		if (check(testFrame.isEmpty(), false)) {
			System.out.println("*PASS* Frame.isEmpty is working correctly\n");
			numPasses++;
		} else {
			System.out.println("*FAIL* Frame.isEmpty is not working correctly\n");
		}

		if (check(testFrame.getCurrentFrameSize(), 7)) {
			System.out.println("*PASS* Get current tiles is working. frame full\n");
			numPasses++;
		} else {
			System.out.println("*FAIL* Get current tiles is not working\n");
		}

		System.out.println("Building testFrame which contains 'BANGING'...");
		testFrame.buildTestFrame("BANGING");
		System.out.println(testFrame);

		//System.out.println("\nReturned ArrayList after checking for 'BANG'");
		//System.out.println(testFrame.checkFrameForWord("BANG") +"\n");
		//System.out.println(testFrame);
		
		System.out.println("\nChecking if the frame can remove specific letters .. ING..");
		testFrame.renewSelectTiles("ING");
		System.out.println(testFrame + "\n");

		System.out.println("Removing the first tile from frame:");
		System.out.println(testFrame.removeTile(0) +"\n");

		if (check(testFrame.getCurrentFrameSize(), 6)) {
			System.out.println("*PASS* First Tile successully removed\n");
			numPasses++;
		} else {
			System.out.println("*FAIL* First Tile not successully removed\n");
		}
		testFrame.fillFrame();

		System.out.println("Full frame after random re-filling:");
		System.out.println(testFrame + "\n");

		if (check(testFrame.getCurrentFrameSize(), 7)) {
			System.out.println("*PASS* fill frame is working, frame has 7 tiles\n");
			numPasses++;
		} else {
			System.out.println("*FAIL* fill frame is not working");
		}
		

		// ///////////////////////////////////////////////////////////////////////
		// POOL TESTS
		// ///////////////////////////////////////////////////////////////////////

		System.out.println("\n___Pool Testing___\n");
		
		System.out.println("Reseting pool for testing\n");
		myPool.reset();
		if (check(myPool.getNumTiles(), 100)) {
			System.out.println("*PASS* Pool is reset and full\n");
			numPasses++;
		} else {
			System.out.println("*FAIL* Pool is not reset and not full\n");
		}

		if (check(myPool.getTiles().get(0).getLetter(), 'A')) {
			System.out.println("*PASS* Letter a was initialised correctly\n");
			numPasses++;
		} else {
			System.out.println("*FAIL* Letter a was NOT initialised correctly\n");
		}

		if (check(myPool.getTiles().get(9).getScore(), 3)) {
			System.out.println("*PASS* Letter b's score was initialised correctly\n");
			numPasses++;
		} else {
			System.out
					.println("*FAIL* Letter b's score was NOT initialised correctly\n");
		}

		if (check(myPool.isEmpty(), false)) {
			System.out.println("*PASS* Pool.isEmpty is working\n");
			numPasses++;
		} else {
			System.out.println("*FAIL* Pool.isEmpty is NOT working\n");
		}

		System.out.println("Removing random tile... ");
		System.out.println("Random tile removed: " + myPool.getRandTile()+"\n");

		if (check(myPool.getNumTiles(), 99)) {
			System.out.println("*PASS* NumTiles still is correct\n");
			numPasses++;
		} else {
			System.out.println("*FAIL* numTiles is NOT correct\n");
		}

		System.out.println("Reseting pool\n");
		myPool.reset();

		if (check(myPool.getNumTiles(), 100)) {
			System.out.println("*PASS* NumTiles is correct, its back up to 100\n");
			numPasses++;
		} else {
			System.out.println("*FAIL* ssnumTiles is NOT correct\n");
		}
		
		// ///////////////////////////////////////////////////////////////////////
		// EMPTYING TESTING
		// ///////////////////////////////////////////////////////////////////////

		System.out.println("\n___Emptying Testing___\n");


		myPool.zap96Tiles();
		if (check(myPool.getNumTiles(), 4)) {
			System.out.println("*PASS* After deleting 96 tiles only 4 remain\n");
			numPasses++;
		} else {
			System.out.println("*FAIL* After deleting 96 tiles a number other than 4 remain\n");
		}
		
		Frame newFrame = new Frame(myPool);
		System.out.println("Playing 4 letter word\n");
		newFrame.checkFrameForWord("yz__");
		
		
		if (check(myPool.isEmpty(), true)) {
			System.out.println("*PASS* After playing 4 letter word frame is now empty\n");
			numPasses++;
		} else {
			System.out.println("*FAIL* After playing 4 letter word frame is not empty\n");
		}
		
		
		if (check(newFrame.fillFrame(), -1)) {
			System.out.println("*PASS* Fill frame returned correct error message when trying to fill from empty pool\n");
			numPasses++;
		} else {
			System.out.println("*FAIL* Fill frame did not return correct error message when trying to fill from empty pool\n");
		}
		
		
		// ///////////////////////////////////////////////////////////////////////
		// CONCLUSION
		// ///////////////////////////////////////////////////////////////////////

		System.out.println("\n___Conclusion___\n");
		if (check(numPasses, numTests)) {
			System.out.println("ALL TESTS PASSED\n");
		} else {
			System.out.println("NOT ALL TESTS PASSED\n");
		}
		

	}

	
	
}
