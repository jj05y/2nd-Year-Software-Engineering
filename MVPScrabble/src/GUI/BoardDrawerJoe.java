package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Ass1.Player;
import Ass2.Consts;
import Ass2.Square;

@SuppressWarnings("serial")
public class BoardDrawerJoe extends JFrame {

	private Square[][] board;
	private ImageIcon bgPic;
	private JLabel bgLabel;
	private JLabel scoreLabel;
	private JLabel nothing[][];
	private Player player;
	private JPanel masterPanel;
	private JPanel boardPanel;
	private JPanel scorePanel;

	public BoardDrawerJoe(Square[][] b, Player p) {
		super("MVP SCRABBLE");
		this.player = p;
		this.board = b;

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(700, 700);
		this.setLocationRelativeTo(null);
		
		masterPanel = new JPanel();
		boardPanel = new JPanel();
		scorePanel = new JPanel();

		bgPic = new ImageIcon(getClass().getResource("/GUI/pics/bgs.png"));
		bgLabel = new JLabel();
		bgLabel.setIcon(bgPic);

		scoreLabel = new JLabel(player.getName() + " has score " + player.getScore());

		bgLabel.setSize(600, 600);
		bgLabel.setLayout(new GridLayout(Consts.BOARD_SIZE, Consts.BOARD_SIZE));
		drawTiles();

		scorePanel.add(scoreLabel, BorderLayout.SOUTH);
		bgLabel.validate();
		boardPanel.add(bgLabel);
		
		masterPanel.add(boardPanel, BorderLayout.CENTER);
		masterPanel.add(scorePanel, BorderLayout.SOUTH);
		
		add(masterPanel);

		this.setVisible(true);

	}

	private void drawTiles() {

		// making loads of nothing
		nothing = new JLabel[Consts.BOARD_SIZE][Consts.BOARD_SIZE];

		for (int i = 0; i < Consts.BOARD_SIZE; i++) {
			for (int j = 0; j < Consts.BOARD_SIZE; j++) {

				nothing[i][j] = new JLabel();
				nothing[i][j].setSize(40, 40);
				nothing[i][j].setIcon(new ImageIcon(getClass().getResource("/GUI/pics/empty.png")));

			}
		}

		// putting in tiles,
		for (int i = 0; i < Consts.BOARD_SIZE; i++) {
			for (int j = 0; j < Consts.BOARD_SIZE; j++) {

				if (board[i][j].getTile() == null) {
					bgLabel.add(nothing[i][j]);
				} else {
					bgLabel.add(board[i][j].getTile().getLabel());
				}
			}

		}

	}

}
