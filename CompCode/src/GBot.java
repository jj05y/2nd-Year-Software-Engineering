/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Team:				NorfolkNChance																		   //
//	Students:			Benjamin Ellafi, Gary Mac Elhineny and Przemyslaw Gawkowski   					       //
//	Student Numbers:	13920022, 13465572 and 13473698									                       //
//																										       //
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.ArrayList;

public class GBot implements Bot {
	private Word word = new Word();
	private String letters;
	private int bestScore;
	private ArrayList<Word> tempPermWords;
	private ArrayList<Word> allPermWords;
	public static final int SKIP =2;
	public static final int VALID_PLAY=1;
	public static final int NONVALID_PLAY=0;
	
	public void reset () {
		allPermWords= new ArrayList<Word>();
		tempPermWords = new ArrayList<Word>();
		bestScore=0;
		word.setWord(7, 7, Word.HORIZONTAL, "HELLO");
		letters = "XYZ";
	}
	
	public int getCommand (Player player, Board board, Dictionary dictionary) {
		ArrayList<String> perms = new ArrayList<String>();
		char boardChar;
		bestScore=0;
		int orientation=0;
		int command;
		long start = System.currentTimeMillis();
		long end;
		
		//If it's the first move
		if(board.isFirstPlay()==true){
			String toPermute;
			toPermute=stringFromFrame(player);
			perms =permutations(toPermute);
			perms = validatePerms(perms, dictionary);
			if(perms.size()==0) {
				letters=toPermute;
				return UI.COMMAND_EXCHANGE;
			}
			
			for(int i=0; i < perms.size(); i++) 
				getBestMoveIndex(7, 7, Word.HORIZONTAL, perms.get(i), board, dictionary, player);	
			//play best word
			return UI.COMMAND_PLAY;
		}
		
		//If it's not the first move do this
		String toPermute=stringFromFrame(player);
		for(int row=0; row < Board.SIZE; row++ ) {
			for(int col=0; col <Board.SIZE; col++) {
				if(board.getSqContents(row, col)!= Board.EMPTY) {
						
					orientation= getWordDirection(row, col, board);
					if(orientation== SKIP)
						continue;
					
					//check all tiles on board for perms
					boardChar = board.getSqContents(row, col);
					command =getPermsOnChar(row, col, orientation,toPermute, board, boardChar, player, dictionary);
					if(command==UI.COMMAND_EXCHANGE) 
						return command;
				}
			}
		}
		tempPermWords.clear();
		allPermWords.clear();
			
		//Play best word here
		if(bestScore >0) {
			bestScore=0;
			end = System.currentTimeMillis();
			System.out.println("Took : " + ((end - start) / 1000));
			return UI.COMMAND_PLAY;
		}
		
	return(UI.COMMAND_PASS);
}

	public Word getWord () {
		// should not change
		return(word);
	}
		
	public String getLetters () {
		// should not change
		return(letters);
	}
	
	//permutations of Frame
	public ArrayList<String> permutations(String toPermute) {
	    ArrayList<String> perms = new ArrayList<String>();
	    char first;
	    String rest;
	    
	    //if frame only has one tile, then there's only one permutation for it
	    if (toPermute.length() == 1) {					
	        perms.add(toPermute);
	        return perms;
	    }
	    first = toPermute.charAt(0);				//(car L)
	    rest = toPermute.substring(1);				//(cdr L)
	    
	    //For a string of length n, get all permutations of length n
	    for (String permutation : permutations(rest)) 
	    	perms.addAll(insertAtAllPositions(first, permutation));
	    
	    //Get all substring permutations of the initial word
	    for(int i = 0 ; i < toPermute.length() ; i++ ) {
	    	for( int j = 1 ; j <= toPermute.length() - i ; j++ ) {
	    		String subString = toPermute.substring(i, i+j);	
	    		perms.add(subString);
	         }
	    }
	   
		return perms;
	}
	
	private int getPermsOnChar(int row, int col, int orientation, String toPermute, Board board, char boardChar, Player player, Dictionary dictionary){
		ArrayList<String> perms = new ArrayList<String>();
		ArrayList<String> temp = new ArrayList<String>();
		
		temp =permutations(toPermute);
		for (int i =0; i < temp.size();i++) 
	    	perms.addAll(insertAtAllPositions(row, col, boardChar, temp.get(i), board, orientation, player));
		
		//Validate all words made in the above loop
		allPermWords =validateWords(tempPermWords, dictionary);
			
		if(allPermWords.size()==0) {
			letters=toPermute;
			return UI.COMMAND_EXCHANGE;
		}
		 
		for(int i=0; i < allPermWords.size(); i++) 
			getBestMoveIndex(allPermWords.get(i).getStartRow(), allPermWords.get(i).getStartColumn(), allPermWords.get(i).getDirection(), allPermWords.get(i).getLetters(),board, dictionary, player);
		
		return 0;
	}

	//for frame permutations
	private ArrayList<String> insertAtAllPositions(char firstInPerm, String currPerm) {
	    ArrayList<String> newPerms = new ArrayList<String>();
	    
	    //get all perms where the first char in string is inserted at all indexes of the string
	    for (int i = 0; i <= currPerm.length(); ++i) {
	    	String newPerm = currPerm.substring(0, i) + firstInPerm + currPerm.substring(i);							
	        newPerms.add(newPerm);
	    }
	    return newPerms;
	}
	
	//for board and frame permutations
	//since we actually need references to the row, col for getting the best word, this is needed
	private ArrayList<String> insertAtAllPositions( int row, int col, char firstInPerm, String currPerm, Board board, int orientation, Player player) {
	    ArrayList<String> newPerms = new ArrayList<String>();
	    
	    if(orientation ==Word.HORIZONTAL) {
	    	for (int i = 0; i <= currPerm.length(); i++) {
	    		Word tempWord= new Word();
	    		String newPerm = currPerm.substring(0, i) + firstInPerm + currPerm.substring(i);
	    		//only adds up words
	    		tempWord.setWord(row-i, col, Word.VERTICAL, newPerm);
	    		tempPermWords.add(tempWord);

	    		newPerms.add(newPerm);
	    	}
	    }
	    else {
	    	for (int i = 0; i <= currPerm.length(); i++) {
	    		Word tempWord= new Word();
	    		String newPerm = currPerm.substring(0, i) + firstInPerm + currPerm.substring(i);
	    		tempWord.setWord(row, col-i, Word.HORIZONTAL, newPerm);
	    		tempPermWords.add(tempWord);
	    		
	    		newPerms.add(newPerm);
	    	}
	    }
	    return newPerms;
	}
	
	//only needed for the first move since it doesn't need to change indexs for the first move
	private ArrayList<String> validatePerms(ArrayList<String> unvalidatedPerms, Dictionary dictionary) {
		ArrayList<String> validPerms = new ArrayList<String>();
		ArrayList<String> temp = new ArrayList<String>();
		boolean validWord=false;
		
		//pass one perm at a time to the dictionary.
		for(int i=0; i < unvalidatedPerms.size(); i++) {
			temp.add(unvalidatedPerms.get(i));
			validWord = dictionary.areWords(temp);
			if(validWord==true) 
				validPerms.add(unvalidatedPerms.get(i));
			temp.clear();
		}
		return validPerms;
	}
	
	//validate the words to be played before testing if they're playable on board to minimize processor time
	private ArrayList<Word> validateWords(ArrayList<Word> unvalidatedWords, Dictionary dictionary) {
		ArrayList<Word> validWords = new ArrayList<Word>();
		ArrayList<String> temp = new ArrayList<String>();
		boolean validWord=false;
		
		for(int i=0; i < unvalidatedWords.size(); i++) {
			temp.add(unvalidatedWords.get(i).getLetters());
			validWord = dictionary.areWords(temp);
			if(validWord==true) 
				validWords.add(unvalidatedWords.get(i));
			temp.clear();
		}
		return validWords;
	}
	
	private void getBestMoveIndex(int row, int col, int orientation, String permutation, Board board, Dictionary dictionary, Player player) {
		Word myWord= new Word();
		int temp=0;
		int validPlay=VALID_PLAY;
		int checkSideWords= NONVALID_PLAY;
	
		//sometimes the word generated is a null word, this essentially stops them being validated
		myWord.setWord(row, col, orientation, permutation);
		if(myWord.getLetters()=="")
			validPlay =NONVALID_PLAY;
		
		//Check if move inbounds
		if(orientation==Word.HORIZONTAL) 
			if(row+permutation.length()>15 || row+permutation.length()<0)
				validPlay =NONVALID_PLAY;
				
		if(orientation==Word.VERTICAL) 
			if(col+permutation.length()>15 || col+permutation.length()<0)
				validPlay =NONVALID_PLAY;
		
		//Set the very first valid Perm to be the best Move
		if(bestScore==0 && validPlay == VALID_PLAY) {
			try {
				temp = board.getTotalWordScore(myWord);
				
				if(board.checkWord(myWord, player.getFrame())==UI.WORD_OK){
					checkSideWords = checkWord(myWord, board);
					if(checkSideWords ==VALID_PLAY && checkExtensions(myWord, dictionary, board)==VALID_PLAY) {
						word =myWord;
						bestScore = temp;
					}
				}
			}catch(ArrayIndexOutOfBoundsException e){}
		}
		
		//If the next valid perm has a better score, update best Score, avoid parallel words
		else if(bestScore>0 && validPlay == VALID_PLAY){
			try {
					temp = board.getTotalWordScore(myWord);
					if(temp > bestScore && board.checkWord(myWord, player.getFrame())==UI.WORD_OK) {
						checkSideWords = checkWord(myWord, board);
						if(checkSideWords ==VALID_PLAY && checkExtensions(myWord, dictionary, board)==VALID_PLAY) {
							word =myWord;
							bestScore = temp;
						}
					}
				
			}catch(ArrayIndexOutOfBoundsException e){}
		}
		
	}	
	private int getWordDirection(int row, int col, Board board) {
		int orientation=0;
		//Decide if word on board is down or across orientation wise
		try{
			if(board.getSqContents(row-1, col)==Board.EMPTY && board.getSqContents(row+1, col)==Board.EMPTY) {
				orientation =Word.HORIZONTAL;
			}
			else if(board.getSqContents(row, col-1)==Board.EMPTY && board.getSqContents(row, col+1)==Board.EMPTY) {
				orientation = Word.VERTICAL;
			}
			else
				orientation = SKIP;
			
		}catch(ArrayIndexOutOfBoundsException e){}
		
		return orientation;
	}
	
	private String stringFromFrame(Player player){
		String string="";
		for(int i=0; i < player.getFrame().size();i++) {
			if(player.getFrame().getAllTiles().get(i).getFace()==Tile.BLANK) {
				string+= 'a';
			}
			else {
				string+=player.getFrame().getAllTiles().get(i).getFace();
			}
		}
		
		return string;
	}
	private int checkWord(Word toPlay, Board board) {
		int numSurroundingTiles=0;
		int row = toPlay.getStartRow();
		int col = toPlay.getStartColumn();
		
		//checks if parallel words being played, we want to avoid these
		if(toPlay.isHorizontal()) {
			try{
				for(int i=0; i < toPlay.getLetters().length();i++) {
					if(board.getSqContents(row+1,col)!= Board.EMPTY && board.getSqContents(row-1,col)!= Board.EMPTY)
						numSurroundingTiles++;
					if(board.getSqContents(row+1,col)== Board.EMPTY && board.getSqContents(row-1,col)!= Board.EMPTY)
						numSurroundingTiles++;
					if(board.getSqContents(row+1,col)!= Board.EMPTY && board.getSqContents(row-1,col)== Board.EMPTY)
						numSurroundingTiles++;
					col++;
				}
				if(board.getSqContents(row,toPlay.getStartColumn()-1)!= Board.EMPTY && board.getSqContents(row,toPlay.getStartColumn()+toPlay.getLength())!= Board.EMPTY)
					numSurroundingTiles++;
				
			}catch(ArrayIndexOutOfBoundsException e){}
			if(numSurroundingTiles <=1) {
				return VALID_PLAY;
			}
			else{
				return NONVALID_PLAY;
			}
		}
		
		else { //it's vertical
			try {	
				for(int i=0; i < toPlay.getLetters().length();i++) {
					if(board.getSqContents(row,col+1)!= Board.EMPTY && board.getSqContents(row,col-1)!= Board.EMPTY) 
						numSurroundingTiles++;
					if(board.getSqContents(row,col+1)== Board.EMPTY && board.getSqContents(row,col-1)!= Board.EMPTY)
						numSurroundingTiles++;
					if(board.getSqContents(row,col+1)!= Board.EMPTY && board.getSqContents(row,col-1)== Board.EMPTY)
						numSurroundingTiles++;
					row++;
				}
				if(board.getSqContents(toPlay.getStartRow()-1,col)!= Board.EMPTY && board.getSqContents(toPlay.getStartRow()+toPlay.getLength(),col)!= Board.EMPTY)
					numSurroundingTiles++;
			}catch(ArrayIndexOutOfBoundsException e){}
			if(numSurroundingTiles ==1) {
				return VALID_PLAY;
			}
			else{
				return NONVALID_PLAY;
			}
		}
	}
	
	//check to see if extended words are valid 
	public int checkExtensions(Word word, Dictionary dictionary, Board board) {
		int validMove=NONVALID_PLAY;
		boolean success=false;
		Word wordToCheck= new Word();
		ArrayList<Word> words = new ArrayList<Word>();
		int prefixIndex=1;
		int suffixIndex=1;
		boolean doPrefix=false;
		boolean doSuffix=false;
		String stringToCheck="";
		String prefix="";
		String suffix="";
		
		if(word.isHorizontal()) {
			if(board.getSqContents(word.getStartRow(), word.getStartColumn()-1)!= Board.EMPTY) {
				for(prefixIndex=1; board.getSqContents(word.getStartRow(), word.getStartColumn()-prefixIndex)!= Board.EMPTY; prefixIndex++) {
					prefix +=board.getSqContents(word.getStartRow(), word.getStartColumn()-prefixIndex);
					doPrefix=true;
				}
			}
			
			if(board.getSqContents(word.getStartRow(), word.getStartColumn()+word.getLength()+1)!= Board.EMPTY) {
				for(suffixIndex=1; board.getSqContents(word.getStartRow(), word.getStartColumn()+word.getLength()+suffixIndex)!= Board.EMPTY; suffixIndex++) {
					suffix +=board.getSqContents(word.getStartRow(), word.getStartColumn()+word.getLength()+suffixIndex);
					doSuffix=true;
				}	
			}
			
			if(doPrefix ==true && doSuffix==true) {
				stringToCheck =prefix+word.getLetters()+suffix;
				wordToCheck.setWord(word.getStartRow(), word.getStartColumn()-prefixIndex, Word.HORIZONTAL, stringToCheck);
				words.add(wordToCheck);
				words = validateWords(words, dictionary);
			}
			else if(doPrefix ==false && doSuffix==true) {
				stringToCheck =word.getLetters()+suffix;
				wordToCheck.setWord(word.getStartRow(), word.getStartColumn(), Word.HORIZONTAL, stringToCheck);
				words.add(wordToCheck);
				words = validateWords(words, dictionary);
			}
			else if(doPrefix ==true && doSuffix==false) {
				stringToCheck =prefix+word.getLetters();
				wordToCheck.setWord(word.getStartRow(), word.getStartColumn()-prefixIndex, Word.HORIZONTAL, stringToCheck);
				words.add(wordToCheck);
				words = validateWords(words, dictionary);
			}
			
			else 
				return VALID_PLAY;
			
			if(words.size()>=1)
				return VALID_PLAY;
			else 
				return NONVALID_PLAY;
			
		}
		
		else {
			
			if(board.getSqContents(word.getStartRow()-1, word.getStartColumn())!= Board.EMPTY) {
				for(prefixIndex=1; board.getSqContents(word.getStartRow()-prefixIndex, word.getStartColumn())!= Board.EMPTY; prefixIndex++) {
					prefix +=board.getSqContents(word.getStartRow()-prefixIndex, word.getStartColumn());
					doPrefix=true;
				}
			}
				
			if(board.getSqContents(word.getStartRow()+word.getLength()+1, word.getStartColumn())!= Board.EMPTY) {
				for(suffixIndex=1; board.getSqContents(word.getStartRow()+word.getLength()+suffixIndex, word.getStartColumn())!= Board.EMPTY; suffixIndex++) {
					suffix +=board.getSqContents(word.getStartRow()+word.getLength()+suffixIndex, word.getStartColumn());
					doSuffix=true;
				}
			}
			
			if(doPrefix ==true && doSuffix==true) {
				stringToCheck =prefix+word.getLetters()+suffix;
				wordToCheck.setWord(word.getStartRow()-prefixIndex, word.getStartColumn(), Word.VERTICAL, stringToCheck);
				words.add(wordToCheck);
				words = validateWords(words, dictionary);
			}
			else if(doPrefix ==false && doSuffix==true) {
				stringToCheck =word.getLetters()+suffix;
				wordToCheck.setWord(word.getStartRow(), word.getStartColumn(), Word.VERTICAL, stringToCheck);
				words.add(wordToCheck);
				words = validateWords(words, dictionary);
			}
			else if(doPrefix ==true && doSuffix==false) {
				stringToCheck =prefix+word.getLetters();
				wordToCheck.setWord(word.getStartRow()-prefixIndex, word.getStartColumn(), Word.VERTICAL, stringToCheck);
				words.add(wordToCheck);
				words = validateWords(words, dictionary);
			}
			
			else 
				return VALID_PLAY;
		
			if(words.size()>=1)
				return VALID_PLAY;
			else 
				return NONVALID_PLAY;
			
		}
		
	}
	
}
