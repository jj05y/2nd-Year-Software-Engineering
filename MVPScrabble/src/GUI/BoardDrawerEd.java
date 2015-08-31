package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Ass1.Player;
import Ass2.Consts;
import Ass2.Square;
import Ass3.Scrabble;


public class BoardDrawerEd extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int SQUARE_SIZE = 10;

	Scrabble board;
	Player player1;
	Player player2;

	JPanel scorePanel = new JPanel();
	JLabel score1Label = new JLabel();
	JLabel score2Label = new JLabel();

	JPanel gridPanel = new JPanel();
	JLabel[][] grid = new JLabel[Consts.BOARD_SIZE][Consts.BOARD_SIZE];
	JPanel[][] squares = new JPanel[Consts.BOARD_SIZE][Consts.BOARD_SIZE];

	public BoardDrawerEd(Scrabble board, Player player1, Player player2) {
		setLayout(new BorderLayout());
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		setLocation(screenSize.width / 4, screenSize.height / 4);

		this.board = board;
		this.player1 = player1;
		this.player2 = player2;
		initGUI();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
	}

	private void initGUI() {
		initGrid();
		initScorePanel();
		add(gridPanel);
		add(scorePanel, BorderLayout.SOUTH);
	}

	private void initScorePanel() {
		scorePanel.setLayout(new GridLayout(1, 2));
		score1Label.setText(player1.getClass().getName());
		score2Label.setText(player2.getClass().getName());
		scorePanel.add(score1Label);
		scorePanel.add(score2Label);
	}

	private void initGrid() {
		gridPanel.setLayout(new GridLayout(Consts.BOARD_SIZE, Consts.BOARD_SIZE));
		for (int row = 0; row < Consts.BOARD_SIZE; row++) {
			for (int column = 0; column < Consts.BOARD_SIZE; column++) {
				Square square = board.getSquare(row, column);
				JPanel panel = new JPanel();
				JLabel label = new JLabel(getContent(square));
				label.setBackground(getSquareColor(square));
				panel.setBackground(getSquareColor(square));
				panel.setSize(50, 50);
				panel.setBorder(BorderFactory.createEtchedBorder());
				panel.add(label);
				squares[row][column] = panel;
				grid[row][column] = label;
				gridPanel.add(panel);
			}
		}
	}

	
	
	public Color getSquareColor(Square square) {
		
		if(square.getLetterMultiplier() == 3){
			return Color.RED;
		}else if(square.getWordMultiplier() == 3){
			return new Color(255, 228, 150);
		}else if(square.getWordMultiplier() == 2){
			return Color.CYAN;
		}else if(square.getLetterMultiplier() == 2){
			return new Color(100, 149, 237);
		}else if(square.getDisplayChar() == "*"){
			return new Color(0, 0, 0);
		}
		return new Color(210, 210, 210);
		
		/*
		int content = square.getLetterMultiplier();
		switch (content) {
		case square.getLetterMultiplier();
			return Color.RED;
		case square.getWordMultiplier():
			return new Color(255, 228, 150);
		case square.getWordMultiplier():
			return Color.CYAN;
		case square.getLetterMultiplier():
			return new Color(100, 149, 237);
		case square.:
			return new Color(0, 0, 0);
		}
		return new Color(210, 210, 210);
		*/
	}

	private String getContent(Square square) {
		String content = square.getDisplayChar();
		if (square.getDisplayChar() != null) {
			return ""+content;
		}
		return "";
	}

	public void updateScores(Player player, int turn) {
		String text = (turn < 0) ? player1.getClass().getSimpleName() : player2
				.getClass().getSimpleName();

		text = text + ": " + " (" + player.getScore() + ") points!";
		if (turn < 0)
			score1Label.setText(text);
		else
			score2Label.setText(text);
	}
	public void updateBoard() {
		for (int row = 0; row < Consts.BOARD_SIZE; row++) {
			for (int column = 0; column < Consts.BOARD_SIZE; column++) {
				JLabel label = grid[row][column];
				Square square = board.getSquare(row, column);
				if (square.getLetterMultiplier() == 1 && square.getWordMultiplier() == 1 && square.getDisplayChar() != "   ")
					squares[row][column].setBackground(Color.WHITE);
				label.setText(getContent(square));
			}
		}
	}

}


