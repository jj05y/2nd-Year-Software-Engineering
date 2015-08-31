
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


/*
 * TEAM: 	SCRBBL
 * 			Oisin Kyne - 13398651
 * 			Conor Kelleher - 13395381
 * 			Jack Beegan - 
 * 
 */

public class Scrbbl implements Bot {

	private Word word = new Word();
	private String letters;
	private boolean[][] available;
	private boolean[][] previousAvailable;
	private Dictionary dictionary;
	private boolean challenged;
	private ArrayList<String> previousWords;
	private static final int BLANK_MINIMUM_SCORE = 10;
	private static final int TIME_LIMIT = 30000;
	
	private String vowels;
	private String consonants;
	
	public void reset ()  throws FileNotFoundException{
		word.setWord(0, 0, Word.HORIZONTAL, "HELLO");
		letters = "XYZ";
		previousWords = new ArrayList<String>();
		challenged = false;
		available = new boolean[15][15];
		previousAvailable = new boolean[15][15];
		for(boolean[] row : available)
			Arrays.fill(row, false);
		for(boolean[] row : previousAvailable)
			Arrays.fill(row, false);
		vowels = "UIOAE"; //vowels sorted most to least valuable by tactics book
		consonants = "QZJXKVCYFWHPMBGDLNTRS";
	}
	
	public int getCommand (Player player, Board board, Dictionary dict) {
		// make a decision on the play here
		// use board.getSqContents to check what is on the board
		// use Board.SQ_VALUE to check the multipliers
		// use frame.getAllTiles to check what letters you have
		// return the corresponding commandCode from UI
		// if a play, put the start position and letters into word
		// if an exchange, put the characters into letters
		
		if(dictionary == null) {
			dictionary = dict;
		}
		
		available = findAvailable(board);
		
		if(board.isFirstPlay()){
			word = (Word) highestScoringWord(generateLegalWords(generateWords(player, ""), new Word(7,7,0,""), player, board), board)[1];
			previousAvailable = updateAvailable(available, board);
			return(UI.COMMAND_PLAY);  //Create firstMove method later
		}
		
		previousWords = board.getWords();
		
		
		//can this be easily updated to only check new words for time reasons? start checking newPreviousWords at index previousWords.size?
		if(!challenged){
			boolean wordPlaced = false;
			for(int i=0; i<15 && !wordPlaced; i++){
				for(int j=0; j<15 && !wordPlaced; j++){
					if(previousAvailable[i][j] != available[i][j]){
						wordPlaced = true;
					}
				}
			}
			if(wordPlaced){
				if(!dictionary.areWords(previousWords)){
					challenged = true;
					return(UI.COMMAND_CHALLENGE);
				}
			}
		}
		
		boolean check = false;
		for(int i=0; i< player.frame.size() && !check; i++){
			if(player.frame.getTile(i).getFace()=='*'){
				check = true;
			}
		}
		if(check){
			Word temp = getBlankPlacement(board, player);
			if(temp.getLength()>0){
				word = temp;
				challenged = false;
				previousAvailable = updateAvailable(available, board);
				return(UI.COMMAND_PLAY);
			}
		}
		
		word = (Word) findLocations(board, player, TIME_LIMIT)[1];
		if(word.getLetters()==""){
			word = getSingle(player.frame.getAllTiles(), board);
		}
		
		
		if(word.getLetters()=="" || word.getLength()<1) {
			int numExchanged = 0;		//int numExchanged = exchangeLetters(player); //needs to be changed to this if exchange works
			if( numExchanged > 0) {
				System.out.println("Exchanging" + letters);
				challenged = false;
				previousAvailable = findAvailable(board);
				return(UI.COMMAND_PASS); //return(UI.COMMAND_EXCHANGE);
			}
			else {
				challenged = false;
				previousAvailable = findAvailable(board);
				return(UI.COMMAND_PASS); //worst case scenario - no moves found at all, pass
			}
		}
		challenged = false;
		previousAvailable = updateAvailable(available, board);
		return(UI.COMMAND_PLAY);
		
	}
	
	
	/**
	 * Method that takes a player and a string of characters on the board (or an empty string) and generates and arraylist of possible solutions.
	 * @param player The current player, i.e. the bot.
	 * @param boardLetters The letters we have to make use of.
	 * @return An arraylist of words that can be made using the letters on the board with our frame tiles.
	 * 
	 * There is currently no provision for blank tiles substituting real tiles.
	 */
	private ArrayList<String> generateWords(Player player, String boardLetters)
	{
		//long ti = System.nanoTime();
		ArrayList<String> words;
		String letters = new String();
		for(Tile t : player.frame.getAllTiles())
		{
			if(t.getValue() == 0) //It's a blank tile. 
				continue;
			letters += t.getFace();
		}
		letters += '@';			//@ is our place holder for what will become the board letters.
		int iter = 0;
		words = permute("", letters, new ArrayList<String>());
		
		long size = words.size();
		ArrayList<String> stupidList = new ArrayList<String>();
		stupidList.add("Test");
		String tester = new String();
		String output = "";
		for(iter = 0; iter < size; iter++)
		{	
			tester = words.get(iter);
			for(int i=0;i<tester.length();i++) {
				if(tester.charAt(i) == '@')
				{
					output += boardLetters;
				}
				else {
					output+= tester.charAt(i);
				}
			}
				words.set(iter, output);
		
			stupidList.remove(0);	//Empty the arraylist we use for lookup.
			stupidList.add(output);
			output = "";
			if(!dictionary.areWords(stupidList))
			{
				words.remove(iter);
				size--;
				iter--;
				continue;	//Finish here.
			}
			
			
		}
		words.trimToSize();
		return words;
	}
	
	private ArrayList<Word> generateLegalWords(ArrayList<String> possibleWords, Word currentBoardWord, Player player, Board board)
	{
		int snippetStartRow = currentBoardWord.getStartRow(), snippetStartCol = currentBoardWord.getStartColumn();
		int snippetIsVertical = currentBoardWord.getDirection();
		String snippetLetters = currentBoardWord.getLetters().toUpperCase();
		ArrayList<Word> output = new ArrayList<Word>();
		Word temp;
		int snippetIndex;
		for(String s : possibleWords)
		{
			snippetIndex = -1;
			snippetIndex = s.indexOf(snippetLetters);
			if(snippetIsVertical == 1)	//The snippet is vertical, we place this word vertically.
			{
				output.add(new Word(snippetStartRow - snippetIndex, snippetStartCol, 1, s));
			}
			else
			{
				output.add(new Word(snippetStartRow, snippetStartCol - snippetIndex, 0, s));
			}
			
			
		}
		Iterator<Word> iter = output.iterator();
		while(iter.hasNext())
		{
			temp = iter.next();
			if(board.checkWord(temp, player.frame) == 1)
			{
				//The word is legal, rejoice.
				//System.out.println("Created a word object with the following info: Word = " + temp.getLetters());
				//System.out.println("New word row:column:direction = " + temp.getStartRow() +":" + temp.getStartColumn()  + ":" + snippetIsVertical);
			}
			else
			{
				//System.out.println("Word: " + temp.getLetters() + " is not legal here.");
				iter.remove();
			}
		}
		return output;
	}
	
	/**
	 * Takes an array of words, returns the highest scoring one, along with its score.
	 * @param legalWords An arraylist of legal words
	 * @param b The board to score the words against.
	 * @return 2 object array, element 0 is the score, element 1 is the word object.
	 */
	private Object[] highestScoringWord(ArrayList<Word> legalWords, Board b)
	{
		int maxScore = 0, currScore = 0;
		Word maxWord = new Word();
		for(Word w : legalWords)
		{
			currScore = b.getTotalWordScore(w);
			if(!dictionary.areWords(b.getWords())){
				continue;
			}
			if(currScore > maxScore)
			{
				maxWord = w;
				maxScore = currScore;
			}
		}
		Object[] output = new Object[2];
		output[0] = maxScore;
		output[1] = maxWord;
		return output;
	}
	
	private Object[] findLocations(Board b, Player p, long timeLimit)
	{
		int k;
		char c;
		String newSnippet = new String();
		long deadline = System.currentTimeMillis() + timeLimit;
		Object[] output = new Object[2], currScore = new Object[2];
		output[0] = (int) 0;
		output[1] = new Word();
		currScore[0] = (int) 0;
		currScore[1] = new Word();
		for(int i = 14; i >=0; i--)
		{
			for(int j=14; j >=0 && System.currentTimeMillis() < deadline; j--)
			{
				if(b.getSqContents(i, j) != ' ')
				{	
					//Found a tile with a letter.
					
					//Is there an element to the left, if so, don't check horizontally.
					if(j > 0 && b.getSqContents(i, j - 1) != ' ');//Don't evaluate next condition as there is no above.
					else
					{
						k = 0;
						newSnippet = "";
						
							
						c = b.getSqContents(i, j + k);
						while(c != ' ')
						{
							newSnippet += c;
							k++;
							if(j + k == 15){
								break;
							}
							c = b.getSqContents(i, j + k);
						}
						newSnippet = newSnippet.toUpperCase();
						//Find the Highest Score possibility for this snippet.
						currScore = highestScoringWord(generateLegalWords(generateWords(p,newSnippet),new Word(i,j,0,newSnippet),p,b), b);
						//System.out.println("currScore[0] = " + ((int)currScore[0]) + ". currScore[1] = " + ((Word)currScore[1]).getLetters());
						if(((Word)currScore[1]).getLetters() == "");
						else if((Integer) currScore[0] > (Integer) output[0])
						{
							output = currScore;
						}
						newSnippet = "";
						
					}
					
					//Check if there's a letter above, no need to check vertically if there is.

					if(i > 0 && b.getSqContents(i-1, j) != ' '){
						;
					}
					else
					{
						//Check vertically.
						k = 0;
						newSnippet = "";
						c = b.getSqContents(i, j);
						while(c != ' ')
						{
							newSnippet += c;
							k++;
							if(i + k == 15)
								break;
							c = b.getSqContents(i + k, j);
						}
						newSnippet = newSnippet.toUpperCase();
						//Breaks loop with a snippet.
						//Call score method here.
						currScore = highestScoringWord(generateLegalWords(generateWords(p,newSnippet),new Word(i,j,1,newSnippet),p,b), b);
						if(((Word)currScore[1]).getLetters() == "");
						else if((Integer) currScore[0] > (Integer) output[0])
						{
							output = currScore;
						}
						newSnippet = "";
					}
				}
			}
		}
		return output;
	}
	
	/**
	 * Recursive function that generates every possible permutation of a given string.
	 * @param curr The already calculated part of this current permutation
	 * @param str The not currently calculated part of this permutation
	 * @param l An arraylist to store each answer.
	 * @return The arraylist filled with all permutations.
	 */
	private ArrayList<String> permute(String curr, String str, ArrayList<String> l) {
		int n = str.length();
		if(curr.indexOf('@') != -1)
			l.add(curr);
		for (int i = 0; i < n; i++) permute(curr + str.charAt(i), str.substring(0, i) + str.substring(i + 1), l);
		
		return l;
	}
   
	/**
	 * Method to find spaces on the board where moves can be placed (represented by a 2-D boolean array)
	 * Should be run at start of each getCommand() call to find current board availability
	 */
	private boolean[][] findAvailable(Board board){
		boolean[][] available = new boolean[15][15];
		for(boolean[] row : available)
			Arrays.fill(row, false);
		if(board.getSqContents(7, 7) == ' '){ //Only true if no word has been placed on the board
			available[7][7] = true;
		}
		else{ //Otherwise, check all spaces for being both empty and beside a tile (valid available placement)
			for(int row=0; row<15; row++){
				for(int column=0; column<15; column++){
					if(board.getSqContents(row, column) == ' '){
						if(column > 0)
							available[row][column] = (board.getSqContents(row, column-1) != ' ');
						if((column <14) && (!available[row][column]))
							available[row][column] = (board.getSqContents(row, column+1) != ' ');
						if((row > 0) && (!available[row][column]))
							available[row][column] = (board.getSqContents(row-1, column) != ' ');
						if((row < 14) && (!available[row][column]))
							available[row][column] = (board.getSqContents(row+1, column) != ' ');
					}
				}
			}
		}
		return available;
	}
	
	private boolean[][] updateAvailable(boolean[][] available, Board board){
		int startRow = word.getStartRow(), startColumn = word.getStartColumn(), endRow, endColumn;
		if(word.isHorizontal()){
			endColumn = startColumn;
			endRow = startRow + word.getLength() -1;
		}
		else{
			endRow = startRow;
			endColumn = startColumn + word.getLength() -1;
		}
		if(word.isHorizontal()){
			if(startColumn > 0)
				available[startRow][startColumn-1] = (board.getSqContents(startRow, startColumn-1) == ' ');
			if(endColumn < 14)
				available[startRow][startColumn+1] = (board.getSqContents(startRow, startColumn+1) == ' ');
			for(int i=startColumn; i<=endColumn; i++){
				if(startRow>0)
					available[startRow-1][i] = (board.getSqContents(startRow-1, i) == ' ');
				if(startRow<14)
					available[startRow+1][i] = (board.getSqContents(startRow+1, i) == ' ');
			}
		}
		else{
			if(startRow > 0)
				available[startRow-1][startColumn] = (board.getSqContents(startRow-1, startColumn) == ' ');
			if(endColumn < 14)
				available[startRow+1][startColumn] = (board.getSqContents(startRow+1, startColumn) == ' ');
			for(int i=startRow; i<=endRow; i++){
				if(startColumn>0)
					available[i][startColumn-1] = (board.getSqContents(i, startColumn-1) == ' ');
				if(startColumn<14)
					available[i][startColumn+1] = (board.getSqContents(i, startColumn+1) == ' ');
			}
		}
		return available;
	}
	
/**
 * The board object passed to this should be the boardForMethods, so as to preserve instance
 * variables of the other board object
 */
	private Word getSingle (ArrayList<Tile> frame, Board board){
		Word currentWord = new Word();
		Word tempWord = new Word();
		int score = 0, tempScore;
		for(int i=0; i<15; i++){
			for(int j=0; j<15; j++){
				if(available[i][j]){
					for(int x=0; x<frame.size(); x++){
						if(!frame.get(x).isBlank()){
							tempWord = new Word(i, j, 0, Character.toString(frame.get(x).getFace()));
							tempScore = board.getTotalWordScore(tempWord);
							if(dictionary.areWords(board.getWords())){
								if(tempScore > score){
									score = tempScore;
									currentWord = tempWord;
								}
							}
						}
					}
				}
			}
		}
		return currentWord;
	}
	
	/**
	 * Method to simply place a single blank tile
	 * Precondition: The frame must be confirmed to have at least 1 blank tile before running
	 */
	private Word getBlankPlacement(Board board, Player player){
		Word current = new Word(), tempWord;
		int bestScore = 0, tempScore;
		for(int i=0; i<15; i++){
			for(int j=0; j<15; j++){
				if(available[i][j]){
					for(int letter = (int) 'a'; letter<= (int) 'z'; letter++){
						if(i + 1 == 15) {
							break;
						}
						if(board.getSqContents(i+1, j)!=' '){
							tempWord = new Word(i, j, Word.HORIZONTAL, Character.toString((char) letter));
						}
						else{
							tempWord = new Word(i, j, Word.VERTICAL, Character.toString((char) letter));
						}
						tempScore = board.getTotalWordScore(tempWord);
						if(dictionary.areWords(board.getWords())){
							if(tempScore > bestScore){
								current = tempWord;
								bestScore = tempScore;
							}
						}
					}
				}
			}
		}
		if(bestScore<BLANK_MINIMUM_SCORE && player.frame.isFull()){//If score is not 10 or higher with full frame, don't place it
			current = new Word(0, 0, 0, "");
		}
		
		return current;
	}
	
	/**
	 * Method to exchange letters from frame, which attempts to achieve 3 vowels, 4 cons ideally.
	 * @param player the player object to allow access to frame
	 * @return number of tiles being exchanged. If 0, UI.PASS should just be called from getCommand 
	 */
	
	@SuppressWarnings("unused")
	private int exchangeLetters (Player player) {
		ArrayList<Tile> frame = player.frame.getAllTiles();
		
		int vowelCount, consCount, blankCount, exchangeCount=0, frameLetterCount;
		
		String frameLetters = ""; //list of distinct letters in frame
		String exchange = ""; //letters to be exchanged

		Iterator<Tile> frameIterator = frame.iterator();
		while( frameIterator.hasNext() ) { //remove duplicate letters
			Tile curr = frameIterator.next();
			if(frameLetters.contains(Character.toString( curr.getFace() ))) {
				frameIterator.remove();
				exchange += curr.getFace();
				exchangeCount++;
			} else {
				frameLetters += curr.getFace();
			}
		}

		System.out.println("After duplicates. Removed: "+ exchange);
		
		ArrayList<Tile> frameVowels = getFrameVowels(frame);
		ArrayList<Tile> frameConsonants = getFrameConsonants(frame);
		
		vowelCount = frameVowels.size();
		consCount = frameConsonants.size();
		blankCount = frame.size() - vowelCount - consCount;
		
		frameLetterCount = frame.size() - blankCount;
		int vowelBoundary = (int) frameLetterCount/ 2;
		int consonantBoundary = frameLetterCount - vowelBoundary; //equal or greater than vowel count by 1, depending on blanks
		
		while(vowelCount > vowelBoundary) {
			int i=0;
			boolean noExchange = true;
			while(noExchange&& i<vowels.length()) { //remove least valued vowel
				Tile t = new Tile(vowels.charAt(i)); //tile object based on least valued tile
				if(frameVowels.contains(t)) {	//if our frame contains that tile, remove it for exchange
					frameVowels.remove(t);
					exchange += t.getFace();
					noExchange = false;
					exchangeCount++;
					vowelCount--;
				}
				i++;
			}
		}
		
		System.out.println("After vowels. Removed: "+ exchange);
		
		
		while(consCount > consonantBoundary) {
			int i=0;
			boolean noExchange = true;
			while(noExchange && i<consonants.length()) { //remove least valued consonant
				Tile t = new Tile(consonants.charAt(i)); //tile object based on least valued tile
				if(frameConsonants.contains(t)) {	//if our frame contains that tile, remove it for exchange
					frameConsonants.remove(t);
					exchange += t.getFace();
					noExchange = false;
					exchangeCount++;
					consCount--;
				}
				i++;
			}
		}
		

		System.out.println("After cons. Removed: "+ exchange);
		
		letters = exchange;
		System.out.println("Exch: "+exchangeCount);
		
		return exchangeCount;
	}
	
	/**
	 * Method to help exchange letters by seperating vowels from frame
	 * @param frame the frame object to check
	 * @return ArrayList of vowel tiles
	 */
	private ArrayList<Tile> getFrameVowels (ArrayList<Tile> frame) {
		ArrayList<Tile> vowelFrame = new ArrayList<Tile>();
		Tile current;
		
		Iterator<Tile> i = frame.iterator();
		while(i.hasNext()) {
			current = i.next();
			if( vowels.contains( Character.toString( current.getFace() ) ) ) {
				vowelFrame.add( current );
			}
		}
		return(vowelFrame);
	}
	
	/**
	 * Method to help exchange letters by seperating consonants from frame
	 * @param frame the frame object to check
	 * @return ArrayList of consonant tiles
	 */
	private ArrayList<Tile> getFrameConsonants (ArrayList<Tile> frame) {
		ArrayList<Tile> consonantFrame = new ArrayList<Tile>();
		Tile current;
		
		Iterator<Tile> i = frame.iterator();
		while(i.hasNext()) {
			current = i.next();
			if( consonants.contains( Character.toString( current.getFace() ) ) ) {
				consonantFrame.add( current );
			}
		}
		
		return(consonantFrame);
	}
	
	
	public Word getWord () {
		// should not change
		return(word);
	}
		
	public String getLetters () {
		// should not change
		return(letters);
	}
}
