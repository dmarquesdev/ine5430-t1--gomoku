package br.ufsc.inf.ine5430.game;

import java.util.HashSet;

import br.ufsc.inf.ine5430.graph.GraphImpl;
import br.ufsc.inf.ine5430.graph.Node;

public class Game extends GraphImpl {
	private Player player1;
	private Player player2;
	private int turn;

	private GameState currentState;
	private HashSet<GameState> possibleStates;

	public Game(Player player1, Player player2) {
		super();
		this.player1 = player1;
		this.player2 = player2;
		turn = 1;

		currentState = new GameState(turn, "Clear Board", null, null, new Piece[15][15], player1);
		possibleStates = new HashSet<>();
	}

	enum TransposeFrom {
		HORIZONTAL('-'), DIAGONAL_LEFT_TOP_RIGHT_BOTTOM('\\'), DIAGONAL_RIGHT_TOP_LEFT_BOTTOM('/');

		private char label;

		TransposeFrom(char label) {
			this.label = label;
		}

		public char getLabel() {
			return label;
		}
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public GameState getCurrentState() {
		return currentState;
	}

	public void setCurrentState(GameState currentState) {
		this.currentState = currentState;
	}

	public HashSet<GameState> getPossibleStates() {
		return possibleStates;
	}

	public void setPossibleStates(HashSet<GameState> possibleStates) {
		this.possibleStates = possibleStates;
	}

	public void makeAPlay(Play play) {
		// TODO implement
	}

	public boolean isAWin(GameState state, Play play) {
		return verticalCheckWin(state.getBoard(), play) || horizontalCheckWin(state.getBoard(), play)
				|| diagonalsCheckWin(state.getBoard(), play);
	}

	private boolean horizontalCheckWin(Piece[][] board, Play play) {
		// Transpose the board (exchange rows and columns) and test for Vertical
		// Win
		Piece[][] transposedBoard = new Piece[board.length][board[0].length];

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				transposedBoard[j][i] = board[i][j];
			}
		}

		Play transposedPlay = new Play(play.getPlayer(), play.getY(), play.getX());
		return verticalCheckWin(transposedBoard, transposedPlay);
	}

	private boolean diagonalsCheckWin(Piece[][] board, Play play) {
		boolean win = false;

		// Align the board and test for Vertical Win
		Piece[][] alignedBoard = board.clone();
		
		int shift = 4;
		for (int i = Math.max(0, play.getX() - 4); i < play.getX(); i++) {
			alignedBoard[i][play.getY()] = alignedBoard[i][play.getY() - shift];
			shift--;
		}
		
		shift = 4;
		for (int i = Math.max(15, play.getX() + 4); i > play.getX(); i--) {
			alignedBoard[i][play.getY()] = alignedBoard[i][play.getY() + shift];
			shift--;
		}
		
		win = verticalCheckWin(alignedBoard, play);

		if (win) {
			return true;
		}

		// Align the board and test for Vertical Win
		alignedBoard = board.clone();
		
		shift = 4;
		for (int i = Math.max(0, play.getX() - 4); i < play.getX(); i++) {
			alignedBoard[i][play.getY()] = alignedBoard[i][play.getY() + shift];
			shift--;
		}
		
		shift = 4;
		for (int i = Math.max(15, play.getX() + 4); i > play.getX(); i--) {
			alignedBoard[i][play.getY()] = alignedBoard[i][play.getY() - shift];
			shift--;
		}
		
		win = verticalCheckWin(alignedBoard, play);

		if (win) {
			return true;
		}

		return false;
	}

	private boolean verticalCheckWin(Piece[][] board, Play play) {
		int pieceCount = 1;
		// Vertical check
		// ==== Check 5 places bottom for consecutive pieces
		for (int i = play.getX() + 1; i < play.getX() + 5; i++) {
			if (board[i][play.getY()] == play.getPlayer().getPieceType()) {
				pieceCount++;
			} else {
				break;
			}
		}

		if (pieceCount == 5) {
			return true;
		}

		// ==== Check 5 places top
		// ==== (consider the number of consecutive pieces to the bottom)
		for (int i = play.getX() - 1; i < play.getX() - 5 + pieceCount; i--) {
			if (board[i][play.getY()] == play.getPlayer().getPieceType()) {
				pieceCount++;
			} else {
				break;
			}
		}

		if (pieceCount == 5) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isLeafNode(Node node) {
		// TODO calculate next nodes
		return super.isLeafNode(node);
	}

}
