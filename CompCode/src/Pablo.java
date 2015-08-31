import java.util.ArrayList;
import java.util.List;
import java.util.Random;
;public class Pablo implements Bot {

	private Word word = new Word();
	private String letters;
	private double startTime;
	
	public void reset() {
		word.setWord(0, 0, Word.HORIZONTAL, "HELLO");
		letters = "XYZ";
	}
	
	private int checkCode = UI.WORD_OK;
	private Found[] found = new Found[100];
	private Random rand = new Random(System.currentTimeMillis());
	private int or = rand.nextInt(2);
	private Word best = new Word();
	
	public int getCommand (Player player, Board board, Dictionary dictionary) {
		// make a decision on the play here
		// use board.getSqContents to check what is on the board
		// use Board.SQ_VALUE to check the multipliers
		// use frame.getAllTiles to check what letters you have
		// return the corresponding commandCode from UI
		// if a play, put the start position and letters into word
		// if an exchange, put the characters into letters
		startTime = System.currentTimeMillis();
		String availableLetters = new String("");
		
		Tile face;
		Frame frame = player.getFrame();
		
		
		for(int i = 0; i < frame.size(); i++)
		{
			face = frame.getTile(i);
			availableLetters += face.getFace();
		}
		
		if(availableLetters.indexOf('*') >= 0)
		{
			letters = "*";
			return(UI.COMMAND_EXCHANGE);
		}
		
		else if(board.isFirstPlay() == true)
		{
			firstPlay(player, board, dictionary, availableLetters);
			return(UI.COMMAND_PLAY);
		}
		
		else
		{
			anySubsequentPlay(player, board, dictionary, availableLetters);
			System.out.println("Turn time: " + (System.currentTimeMillis() - startTime));
			return(UI.COMMAND_PLAY);
		}

	}
	//Base case play.
	private int firstPlay(Player player, Board board, Dictionary dictionary, String availableLetters)
	{
		List<String> hold = new ArrayList<String>();
		List<String> list = doPerm("", availableLetters, hold);
				
		String bestWord = "";
		int bestScore = 0;
			
		for(String element : list)
		{
			
			ArrayList<String> dictCheck = new ArrayList<String>();
			dictCheck.add(element);
				
			if(dictionary.areWords(dictCheck) == true)
			{
				word.setWord(7, 7, 0, element);
					
				if(board.getTotalWordScore(word) > bestScore)
				{
					bestScore = board.getTotalWordScore(word);
					bestWord = element;
				}
			}
		}
			
		word.setWord(7, 7, 0, bestWord);
		return(UI.COMMAND_PLAY);
	}
	//Records every hit on the board
	public class Found{
		int row, column;
		char face;
		Found(int r, int c, char f)
		{
			this.row = r;
			this.column = c;
			this.face = f;
		}
		
		public int getRow(){return row;}
		public int getColumn(){return column;}
		public char getFace(){return face;}
	}
	//Play any word that is legal and has a value above a certain number
	private int anySubsequentPlay(Player player, Board board, Dictionary dictionary, String availableLetters)
	{
		int bestScore = 0;
		int holderScore = 0;
		int foundCount = 0;
		int wordLength = 0;
		int spacesAfterWord = 0;
		String wordOnBoard = "";
		List<String>  list = null;
		ArrayList<String> criminal = new ArrayList<String>();
		List<String> hold = new ArrayList<String>();
		List<String> useable = new ArrayList<String>();
		String tileFinder = "";
						
		for(int i = 0;i < Board.SIZE; i++)
		{
			for(int k = 0; k < Board.SIZE; k++)
			{
				if(board.getSqContents(i, k) != ' ')
				{
					found[foundCount] = new Found(i, k, board.getSqContents(i, k));
					foundCount++;
				}
			}
		}
		
		for (int tileNumber = 0; tileNumber < player.getFrame().size(); tileNumber++) {
			tileFinder += player.getFrame().getTile(tileNumber).getFace();
		}
		
		
		OuterLoop:
		for(int foundNumber = 0; foundNumber < foundCount; foundNumber++)
		{
			char face = found[foundNumber].getFace();
			tileFinder += face;
			
			list = doPerm("", tileFinder, hold);
			
					
			for(String pop: list)
			{
				criminal.add(pop);
				if(dictionary.areWords(criminal) == true)
				{
					useable.add(pop);
				}
				criminal.clear();
			}
			
			for(String elements: useable)
			{
				int x = 0;
				int[] index = new int[5];
				
				for(int lPosition = 0; lPosition < elements.length(); lPosition++)
				{
					if(elements.charAt(lPosition) == face)
					{
						index[x] = lPosition;
						x++;
					}
				}
				
				for(int newPos = 0; newPos < x; newPos++)
				{
					if(or == 0)
					{
						Word temp = new Word(found[foundNumber].getRow(), (found[foundNumber].getColumn() - index[newPos]), Word.HORIZONTAL, elements);
												
						if(board.checkWord(temp, player.getFrame()) == checkCode)
						{
							
							holderScore = board.getTotalWordScore(temp);
							
							ArrayList<String> pointless = new ArrayList<String>();
							pointless.add(elements);
							
							if(holderScore > 8 && dictionary.areWords(pointless) == true && dictionary.areWords(board.getWords()) == true)
							{
								
									bestScore = holderScore;
									best.setWord(temp.getStartRow(), temp.getStartColumn(), temp.getDirection(), temp.getLetters());
									break OuterLoop;
							}
							
						}
						or++;
					}
					
					else
					{
						Word temp = new Word((found[foundNumber]. getRow() - index[newPos]), found[foundNumber].getColumn(), Word.VERTICAL, elements);
						if(board.checkWord(temp, player.getFrame()) == checkCode)
						{
							
							ArrayList<String> pointless = new ArrayList<String>();
							pointless.add(elements);
							
							holderScore = board.getTotalWordScore(temp);
							if(holderScore > 8 && dictionary.areWords(pointless) == true && dictionary.areWords(board.getWords()) == true)
							{
								bestScore = holderScore;
								best.setWord(temp.getStartRow(), temp.getStartColumn(), temp.getDirection(), temp.getLetters());
								break OuterLoop;
							}
						}
						or--;
					}
				}
			}
			hold.clear();
			useable.clear();
			list.clear();

		}
		
		word.setWord(best.getStartRow(), best.getStartColumn(), best.getDirection(), best.getLetters());
		return(UI.COMMAND_PLAY);
	}
	
	public Word getWord () {
		// should not change
		return(word);
	}
		
	public String getLetters () {
		// should not change
		return(letters);
	}
	
	private static List<String> doPerm(String curr, String str, List<String> list) {

		int n = str.length();
		list.add(curr);
		for (int i = 0; i < n; i++) doPerm(curr + str.charAt(i), str.substring(0, i) + str.substring(i + 1), list);
		return list;
	}
	
	public static void main (String args[]) {
		List<String> hold = new ArrayList<String>();
		List<String> list;
		list = doPerm("", "ABCQWERR", hold);
		list = doPerm("ABCQWERR", "ABCQWERR", list);
		System.out.println(list);
	}
}
