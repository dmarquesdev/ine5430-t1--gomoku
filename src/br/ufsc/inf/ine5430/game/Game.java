package br.ufsc.inf.ine5430.game;

import br.ufsc.inf.ine5430.graph.GraphImpl;
import br.ufsc.inf.ine5430.graph.Node;

public class Game extends GraphImpl {
	private Player player1;
	private Player player2;
	private int turn;
	private Play lastPlay;

	private GameState currentState;
	
	public static final int NONE = 0;
	public static final int BLACK = 1;
	public static final int WHITE = 2;

	public Game(Player player1, Player player2) {
		super();
		this.player1 = player1;
		this.player2 = player2;
		turn = 1;

		currentState = new GameState("Empty Board", null, null, new int[15][15], player1);
		addNode(currentState);
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

	public synchronized void makeAPlay(Play play) {
		// Get current state board clone
		int[][] board = currentState.cloneBoard();
		// Add the new piece into the board
		board[play.getX()][play.getY()] = play.getPlayer().getPieceType();

		// Create a new game state with the new board
		GameState newState = new GameState("Turn " + turn, null, currentState, board, play.getPlayer());

		// Add the new state (node) to the game (graph)
		addNode(newState);

		// Change the current state to the new one
		currentState = newState;

		// Increment the turn counter
		turn++;

		// Change the last play holder
		lastPlay = play;
	}

	public Player isAWin() {
		if (isAWin(currentState, lastPlay)) {
			return lastPlay.getPlayer();
		} else {
			return null;
		}
	}

	public boolean isAWin(GameState state, Play play) {
		return (play != null && state != null) && (verticalCheckWin(state.getBoard(), play)
				|| horizontalCheckWin(state.getBoard(), play) || diagonalsCheckWin(state.getBoard(), play));
	}

	private boolean horizontalCheckWin(int[][] board, Play play) {
		// Transpose the board (exchange rows and columns) and test for Vertical
		// Win
		int[][] transposedBoard = new int[board.length][board[0].length];

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				transposedBoard[j][i] = board[i][j];
			}
		}

		Play transposedPlay = new Play(play.getPlayer(), play.getY(), play.getX());
		return verticalCheckWin(transposedBoard, transposedPlay);
	}

	private boolean diagonalsCheckWin(int[][] board, Play play) {
		boolean win = false;

		// Align the board and test for Vertical Win
		int[][] alignedBoard = GameState.cloneBoard(board);

		// Limits
		int x = play.getX(), y = play.getY();
		// Top-Left
		int limitTL = Math.min(x, y);
		// Top-Right
		int limitTR = Math.min(x, 14 - y);
		// Bottom-Left
		int limitBL = Math.min(14 - x, y);
		// Bottom-Right
		int limitBR = Math.min(14 - x, 14 - y);

		// Top-Left
		int shift = Math.min(4, limitTL);
		for (int i = x-shift; i < x; i++) {
			alignedBoard[i][y] = alignedBoard[i][y - shift];
			shift--;
		}

		// Bottom-Right
		shift = Math.min(4, limitBR);
		for (int i = x+shift; i > x; i--) {
			alignedBoard[i][y] = alignedBoard[i][y + shift];
			shift--;
		}

		win = verticalCheckWin(alignedBoard, play);

		if (win) {
			return true;
		}

		// Align the board and test for Vertical Win
		alignedBoard = GameState.cloneBoard(board);

		// Top-Right
		shift = Math.min(4, limitTR);
		for (int i = x-shift; i < x; i++) {
			alignedBoard[i][y] = alignedBoard[i][y + shift];
			shift--;
		}

		// Bottom-Left
		shift = Math.min(4, limitBL);
		for (int i = x+shift; i > x; i--) {
			alignedBoard[i][y] = alignedBoard[i][y - shift];
			shift--;
		}

		win = verticalCheckWin(alignedBoard, play);

		if (win) {
			return true;
		}

		return false;
	}

	private boolean verticalCheckWin(int[][] board, Play play) {
		int pieceCount = 1;

		int x = play.getX();

		// Vertical check
		// ==== Check 5 places bottom for consecutive pieces
		for (int i = Math.min(x + 1, 14); i <= Math.min(x + 5, 14); i++) {
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
		int consecutives = pieceCount;

		for (int i = Math.max(x - 1, 0); i >= Math.max(x - 5 + consecutives, 0); i--) {
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

	public int getPiece(int row, int column) {
		return currentState.getBoard()[row][column];
	}

	public Player getNextPlayer() {
		return (currentState.getPlayer().equals(player1)) ? player2 : player1;
	}
	
	@Override
	public String toString() {
		return lastPlay.toString() + "\n" + currentState.toString();
	}

}
