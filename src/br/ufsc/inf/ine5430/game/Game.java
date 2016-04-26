package br.ufsc.inf.ine5430.game;

import java.util.HashMap;

import br.ufsc.inf.ine5430.game.CPU.PlayerType;
import br.ufsc.inf.ine5430.graph.Edge;
import br.ufsc.inf.ine5430.graph.GraphImpl;

public class Game extends GraphImpl {
	private Player player1;
	private Player player2;
	private int turn;
	private Play lastPlay;

	private GameState currentState;

	// Move Generation helpers
	private int[] frame;
	private HashMap<int[], Integer> playedPlaces;

	private CPU cpuPlayer;
	private Player humanPlayer;

	// Piece Values
	public static final int NONE = 0;
	public static final int BLACK = 1;
	public static final int WHITE = 2;

	// Heuristics Values
	public static final int TWO = 1;
	public static final int BROKEN_THREE = 10;
	public static final int THREE = 1000;
	public static final int STRAIGHT_THREE = 100000000;
	public static final long FOUR = 1000000000000L;
	public static final long STRAIGHT_FOUR = 1000000000000000L;
	public static final long FIVE = 999999999999999999L;

	public Game(Player player1, Player player2) {
		super();
		this.player1 = player1;
		this.player2 = player2;
		turn = 1;

		currentState = new GameState("Empty Board", null, null, new int[15][15], player1);
		addNode(currentState);

		frame = new int[] { 5, 10 };
		playedPlaces = new HashMap<>();

		if (player1 instanceof CPU) {
			cpuPlayer = (CPU) player1;
			humanPlayer = player2;
		} else if (player2 instanceof CPU) {
			cpuPlayer = (CPU) player2;
			humanPlayer = player1;
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

	public void makeAPlay(Play play) {
		// Get current state board clone
		int[][] board = currentState.cloneBoard();
		// Add the new piece into the board
		board[play.getX()][play.getY()] = play.getPlayer().getPieceType();

		// Create a new game state with the new board
		GameState newState = new GameState("Turn " + turn, null, currentState, board, play.getPlayer());

		// Add the new state (node) to the game (graph)
		addNode(newState);

		// Create an edge from currentState to the new state
		Edge edge = new Edge(play, newState);
		currentState.addEdge(edge);
		addEdge(currentState, edge);

		// Change the current state to the new one
		currentState = newState;

		// Increment the turn counter
		turn++;

		// Change the last play holder
		lastPlay = play;

		// Change the play generation frame
		updateFrame(play);

		// Add play place to played places HashMap
		playedPlaces.put(new int[] { play.getX(), play.getY() }, play.getPlayer().getPieceType());
	}

	/**
	 * Update the move generation frame, for handling intersection
	 */
	private void updateFrame(Play play) {
		if (turn == 2 && !(lastPlay.getPlayer() instanceof CPU)) {
			frame[0] = Math.max(0, Math.min(play.getX(), play.getY()) - 2);
			frame[1] = Math.min(14, Math.max(play.getX(), play.getY()) + 2);
		} else if (play.getX() < frame[0] || play.getY() < frame[0]) {
			frame[0] = Math.max(0, Math.min(play.getX(), play.getY()) - 2);
		} else if (play.getX() > frame[1] || play.getY() > frame[1]) {
			frame[1] = Math.min(14, Math.max(play.getX(), play.getY()) + 2);
		}
	}

	/**
	 * Gets the winner. In case there is no winner, returns null
	 */
	public Player getWinner() {
		if (isAWin(currentState, lastPlay)) {
			return lastPlay.getPlayer();
		} else {
			return null;
		}
	}

	/**
	 * Check for win in the state with specific play
	 */
	public boolean isAWin(GameState state, Play play) {
		return (play != null && state != null) && (verticalCheckWin(state.getBoard(), play)
				|| horizontalCheckWin(state.getBoard(), play) || diagonalsCheckWin(state.getBoard(), play));
	}

	/**
	 * Transpose the board (exchange rows and columns) and test for Vertical Win
	 */
	private boolean horizontalCheckWin(int[][] board, Play play) {
		int[][] transposedBoard = new int[board.length][board[0].length];

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				transposedBoard[j][i] = board[i][j];
			}
		}

		Play transposedPlay = new Play(play.getPlayer(), play.getY(), play.getX());
		return verticalCheckWin(transposedBoard, transposedPlay);
	}

	/**
	 * Align the board (compensate distance from play point) and test for
	 * Vertical Win
	 */
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
		for (int i = x - shift; i < x; i++) {
			alignedBoard[i][y] = alignedBoard[i][y - shift];
			shift--;
		}

		// Bottom-Right
		shift = Math.min(4, limitBR);
		for (int i = x + shift; i > x; i--) {
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
		for (int i = x - shift; i < x; i++) {
			alignedBoard[i][y] = alignedBoard[i][y + shift];
			shift--;
		}

		// Bottom-Left
		shift = Math.min(4, limitBL);
		for (int i = x + shift; i > x; i--) {
			alignedBoard[i][y] = alignedBoard[i][y - shift];
			shift--;
		}

		win = verticalCheckWin(alignedBoard, play);

		if (win) {
			return true;
		}

		return false;
	}

	/**
	 * Check the top and the bottom of a play in the board for 5 consecutive
	 * pieces of the same color
	 */
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

	/**
	 * Get Piece color from row and column
	 */
	public int getPiece(int row, int column) {
		return currentState.getBoard()[row][column];
	}

	/**
	 * Swap between players using the current player
	 */
	public Player getNextPlayer() {
		return (currentState.getPlayer().equals(player1)) ? player2 : player1;
	}

	@Override
	public String toString() {
		return lastPlay.toString() + "\nFrame: {" + frame[0] + ", " + frame[1] + "}\n" + currentState.toString();
	}

	public Play getLastPlay() {
		return lastPlay;
	}

	public void setLastPlay(Play lastPlay) {
		this.lastPlay = lastPlay;
	}

	/**
	 * Evaluate the next game states and create some nodes of the game graph
	 */
	public void generateMoves(PlayerType player) {

		int start = frame[0], end = frame[1];

		for (int i = start; i <= end; i++) {
			for (int j = start; j <= end; j++) {
				if (currentState.getBoard()[i][j] == Game.NONE) {
					Play p = new Play((player == PlayerType.CPU) ? cpuPlayer : humanPlayer, i, j);

					if (currentState.containsEdgeWithValue(p) == null) {
						int[][] board = currentState.cloneBoard();
						board[i][j] = p.getPlayer().getPieceType();
						GameState gs = new GameState("", null, currentState, board, p.getPlayer());
						addNode(gs);

						Edge edge = new Edge(p, gs);
						currentState.addEdge(edge);
						addEdge(currentState, edge);
					}
				}
			}
		}
	}

	/**
	 * Calculate the heuristics value from a state using a player type
	 */
	public void calculateValue(GameState state, PlayerType player) {
		int value = 0;

		for (int[] place : playedPlaces.keySet()) {
			int piece = playedPlaces.get(place);

			// Limits
			int x = place[0], y = place[1];
			// Top
			int limitTop = Math.max(0, x - 5);
			// Right
			int limitRight = Math.min(14, y + 5);
			// Bottom
			int limitBottom = Math.min(14, x + 5);
			// Left
			int limitLeft = Math.max(0, y - 5);

			// Top-Right
			int limitTR = Math.min(4, Math.min(limitTop, limitRight));
			// Bottom-Right
			int limitBR = Math.min(4, Math.min(limitBottom, limitRight));
			// Bottom-Left
			int limitBL = Math.min(4, Math.min(limitBottom, limitLeft));
			// Top-Left
			int limitTL = Math.min(4, Math.min(limitTop, limitLeft));

			int[] shape = new int[6];
			int count = 0;

			// Top
			for (int i = x; i >= limitTop; i--) {
				if (state.getBoard()[i][y] == piece) {
					shape[count] = 2;
				} else if (state.getBoard()[i][y] == NONE) {
					shape[count] = 1;
				} else {
					shape[count] = -1;
				}
				count++;
			}

			long shapeValue = calculateShape(shape);

			if (piece != player.getPiece()) {
				shapeValue *= -1;
			}

			value += shapeValue;

			// Bottom
			count = 0;
			shape = new int[6];
			for (int i = x; i <= limitBottom; i++) {
				if (state.getBoard()[i][y] == piece) {
					shape[count] = 2;
				} else if (state.getBoard()[i][y] == NONE) {
					shape[count] = 1;
				} else {
					shape[count] = -1;
				}
				count++;
			}

			shapeValue = calculateShape(shape);

			if (piece != player.getPiece()) {
				shapeValue *= -1;
			}

			value += shapeValue;

			// Left
			count = 0;
			shape = new int[6];
			for (int i = y; i >= limitLeft; i--) {
				if (state.getBoard()[x][i] == piece) {
					shape[count] = 2;
				} else if (state.getBoard()[x][i] == NONE) {
					shape[count] = 1;
				} else {
					shape[count] = -1;
				}
				count++;
			}

			shapeValue = calculateShape(shape);

			if (piece != player.getPiece()) {
				shapeValue *= -1;
			}

			value += shapeValue;

			// Right
			count = 0;
			shape = new int[6];
			for (int i = y; i <= limitRight; i++) {
				if (state.getBoard()[x][i] == piece) {
					shape[count] = 2;
				} else if (state.getBoard()[x][i] == NONE) {
					shape[count] = 1;
				} else {
					shape[count] = -1;
				}
				count++;
			}

			shapeValue = calculateShape(shape);

			if (piece != player.getPiece()) {
				shapeValue *= -1;
			}

			value += shapeValue;

			// Top-Right
			count = 0;
			shape = new int[6];
			for (int i = 0; i <= limitTR; i++) {
				if (state.getBoard()[Math.max(x - i, 14)][Math.min(y + i, 14)] == piece) {
					shape[count] = 2;
				} else if (state.getBoard()[Math.max(x - i, 14)][Math.min(y + i, 14)] == NONE) {
					shape[count] = 1;
				} else {
					shape[count] = -1;
				}
				count++;
			}

			shapeValue = calculateShape(shape);

			if (piece != player.getPiece()) {
				shapeValue *= -1;
			}

			value += shapeValue;

			// Bottom-Right
			count = 0;
			shape = new int[6];
			for (int i = 0; i <= limitBR; i++) {
				if (state.getBoard()[Math.min(x + i, 14)][Math.min(y + i, 14)] == piece) {
					shape[count] = 2;
				} else if (state.getBoard()[Math.min(x + i, 0)][Math.min(y + i, 14)] == NONE) {
					shape[count] = 1;
				} else {
					shape[count] = -1;
				}
				count++;
			}

			shapeValue = calculateShape(shape);

			if (piece != player.getPiece()) {
				shapeValue *= -1;
			}

			value += shapeValue;

			// Bottom-Left
			count = 0;
			shape = new int[6];
			for (int i = 0; i <= limitBL; i++) {
				if (state.getBoard()[Math.min(x + i, 14)][Math.max(y - i, 0)] == piece) {
					shape[count] = 2;
				} else if (state.getBoard()[Math.min(x + i, 14)][Math.max(y - i, 0)] == NONE) {
					shape[count] = 1;
				} else {
					shape[count] = -1;
				}
				count++;
			}

			shapeValue = calculateShape(shape);

			if (piece != player.getPiece()) {
				shapeValue *= -1;
			}

			value += shapeValue;

			// Top-Left
			count = 0;
			shape = new int[6];
			for (int i = 0; i <= limitTL; i++) {
				if (state.getBoard()[Math.max(x - i, 0)][Math.max(y - i, 0)] == piece) {
					shape[count] = 2;
				} else if (state.getBoard()[Math.max(x - i, 0)][Math.max(y - i, 0)] == NONE) {
					shape[count] = 1;
				} else {
					shape[count] = -1;
				}
				count++;
			}

			shapeValue = calculateShape(shape);

			if (piece != player.getPiece()) {
				shapeValue *= -1;
			}

			value += shapeValue;
		}

		state.setValue(value);
	}

	/**
	 * Decide the value of a shape of 6 places
	 */
	private long calculateShape(int[] shape) {
		int emptySpaces = 0, sequence = 0, total = 0, lastValue = 0;
		for (int value : shape) {
			lastValue = value;
			if (value == 1) {
				emptySpaces++;
			} else if (value == 2) {
				total++;
				if (lastValue == value) {
					sequence++;
				}
			}
		}

		// Five
		if (sequence == 5) {
			return FIVE;
		}

		// Four
		if (total == 4) {
			// Straight four
			if (shape[0] == 1 && shape[5] == 1) {
				return STRAIGHT_FOUR;
			}
			return FOUR;
		}

		// Three
		if (sequence == 3 && total == 3) {
			if ((shape[0] == 1 || shape[1] == 1) && (shape[4] == 1 || shape[5] == 1)) {
				return STRAIGHT_THREE;
			}
			return THREE;
		}

		// Broken Three
		if (total == 3 && emptySpaces >= 1) {
			return BROKEN_THREE;
		}

		if (sequence == 2 && total == 2) {
			return TWO;
		}

		return 0;
	}

	public HashMap<int[], Integer> getPlayedPlaces() {
		return playedPlaces;
	}

	public void setPlayedPlaces(HashMap<int[], Integer> playedPlaces) {
		this.playedPlaces = playedPlaces;
	}

}
