Assigment 4

MVP Submission
  
Contents: 
  - Src/util/ 
  		Files created by us are:
  		- Bot.java			We implemented the get command method, which establishes which word to play
		- MoreComprehensiveBot.java	This bot is much slower but much more comprehensive
  		- Permer.java			This class does the permutations of letters passed to it
  		- PermerTester.java 		This class tests the Permer
  		- ScrabbleBobVSMartha.java	This class is an adaptation of Scrrabble.java, where 2 bots play against each other
						One of the bots is comprehensive and slow
						The other bot has some comprimises but is much faster
		- Robot.java			This is an interface to allow the SrabbleBobVsMartha.java to work
						with 2 different types of robots
		- PotentialWord.java		Objects instantiated from this class are stored in an array list, the best
						potential word is then chosen to be played


  - BobVSMarthaExampleOutput.txt 	This is the output from running ScrabbleBobVsMartha.java, where 2 bots, Bob and Martha, play against each other.
  - BobVsMartha.jar 			This jar file creates bots, Bob and Martha, outputs their game.
					Bob is the comprehensive bot.
					Martha is the Fast bot.

  - Scrabble.jar 			This jar file allows a human to play against the bot.
  - ScrabbleExampleOuput.txt 		An example output where a human plays against the bot, 
  
  - PermerTester.jar 			This jar file creates and outputs all the permutations of either "ABCD" or a command line argument
  - PermerTesterExampleOutput.txt 	An example output of PermerTester

  - Assignment5ScrumNotes.pdf 		Scrum Notes from this sprint
  - HowOurRobotWords.pdf		A brief description of the algorithm process
  - botClassDiagram.png 		UML Diagram of 4 classes, Bot.java, Permer.java, PermerTester.java and ScrabbleBobVSMartha.java
  - Sowpods.txt 			The dictionary file

  - README.txt - this