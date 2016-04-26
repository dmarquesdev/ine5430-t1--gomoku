package br.ufsc.inf.ine5430.game;

import java.util.HashMap;
import java.util.HashSet;

import br.ufsc.inf.ine5430.graph.Edge;
import br.ufsc.inf.ine5430.graph.GraphImpl;
import br.ufsc.inf.ine5430.graph.Node;

public class Game extends GraphImpl {
	private Player player1;
	private Player player2;
	private int turn;
	private Play lastPlay;

	private GameState currentState;

	private int[] frame;
	private HashMap<int[], Integer> playedPlaces;
	private int cpuPlayerPiece;

	public static final int NONE = 0;
	public static final int BLACK = 1;
	public static final int WHITE = 2;

	public static final int BROKEN_THREE = 10;
	public static final int THREE = 20;
	public static final int FOUR = 2000;
	public static final int STRAIGHT_FOUR = 20000;
	public static final int FIVE = 2000000;

	public Game(Player player1, Player player2) {
		super();
		this.player1 = player1;
		this.player2 = player2;
		turn = 1;

		currentState = new GameState("Empty Board", null, null, new int[15][15], player1);
		addNode(currentState);

		frame = new int[] { 5, 8 };
		playedPlaces = new HashMap<>();

		if (player1 instanceof CPU) {
			cpuPlayerPiece = player1.getPieceType();
		} else if (player2 instanceof CPU) {
			cpuPlayerPiece = player2.getPieceType();
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

		Edge edge = new Edge(NONE, newState);
		currentState.addEdge(edge);

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

	private void updateFrame(Play play) {
		if (play.getX() < frame[0] || play.getY() < frame[0]) {
			frame[0] = Math.min(play.getX(), play.getY());
		} else if (play.getX() > frame[1] || play.getY() > frame[1]) {
			frame[1] = Math.max(play.getX(), play.getY());
		}
	}

	public Player getWinner() {
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

	public Play getLastPlay() {
		return lastPlay;
	}

	public void setLastPlay(Play lastPlay) {
		this.lastPlay = lastPlay;
	}

	/**
	 * Evaluate the next moves based on the last play using a radius of 5
	 * places, looking for empty places
	 */
	public HashSet<Play> generateMoves() {
		HashSet<Play> plays = new HashSet<>();
		int start = frame[0], end = frame[1];

		for (int i = start; i <= end; i++) {
			for (int j = start; j <= end; j++) {
				if (currentState.getBoard()[i][j] == Game.NONE) {
					Play p = new Play(getNextPlayer(), i, j);
					plays.add(p);
				}
			}
		}

		return plays;
	}

	public void calculateValue(GameState state) {
		int value = 0;

		for (int[] place : playedPlaces.keySet()) {
			int piece = playedPlaces.get(place);

			// Limits
			int x = place[0], y = place[1];
			// Top
			int limitTop = Math.max(0, x - 4);
			// Right
			int limitRight = Math.min(14, y + 4);
			// Bottom
			int limitBottom = Math.min(14, x + 4);
			// Left
			int limitLeft = Math.max(0, y - 4);

			// Top-Right
			int limitTR = Math.min(4, Math.min(limitTop, limitRight));
			// Bottom-Right
			int limitBR = Math.min(4, Math.min(limitBottom, limitRight));
			// Bottom-Left
			int limitBL = Math.min(4, Math.min(limitBottom, limitLeft));
			// Top-Left
			int limitTL = Math.min(4, Math.min(limitTop, limitLeft));

			int[] shape = new int[5];
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

			int shapeValue = calculateShape(shape);

			if (piece != cpuPlayerPiece) {
				shapeValue *= -1;
			}

			value += shapeValue;

			// Bottom
			count = 0;
			shape = new int[5];
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

			if (piece != cpuPlayerPiece) {
				shapeValue *= -1;
			}

			value += shapeValue;

			// Left
			count = 0;
			shape = new int[5];
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

			if (piece != cpuPlayerPiece) {
				shapeValue *= -1;
			}

			value += shapeValue;

			// Right
			count = 0;
			shape = new int[5];
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

			if (piece != cpuPlayerPiece) {
				shapeValue *= -1;
			}

			value += shapeValue;

			// Top-Right
			count = 0;
			shape = new int[5];
			for (int i = 0; i <= limitTR; i++) {
				if (state.getBoard()[Math.min(x + i, 14)][Math.min(y + i, 14)] == piece) {
					shape[count] = 2;
				} else if (state.getBoard()[Math.min(x + i, 14)][Math.min(y + i, 14)] == NONE) {
					shape[count] = 1;
				} else {
					shape[count] = -1;
				}
				count++;
			}

			shapeValue = calculateShape(shape);

			if (piece != cpuPlayerPiece) {
				shapeValue *= -1;
			}

			value += shapeValue;

			// Bottom-Right
			count = 0;
			shape = new int[5];
			for (int i = 0; i <= limitBR; i++) {
				if (state.getBoard()[Math.max(x - i, 0)][Math.min(y + i, 14)] == piece) {
					shape[count] = 2;
				} else if (state.getBoard()[Math.max(x - i, 0)][Math.min(y + i, 14)] == NONE) {
					shape[count] = 1;
				} else {
					shape[count] = -1;
				}
				count++;
			}

			shapeValue = calculateShape(shape);

			if (piece != cpuPlayerPiece) {
				shapeValue *= -1;
			}

			value += shapeValue;

			// Bottom-Left
			count = 0;
			shape = new int[5];
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

			if (piece != cpuPlayerPiece) {
				shapeValue *= -1;
			}

			value += shapeValue;

			// Top-Left
			count = 0;
			shape = new int[5];
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

			if (piece != cpuPlayerPiece) {
				shapeValue *= -1;
			}

			value += shapeValue;
		}

		state.setValue(value);
	}

	private int calculateShape(int[] shape) {
		int emptySpaces = 0, sequence = 0, enemy = 0;
		for (int value : shape) {
			if (value == 1) {
				emptySpaces++;
			} else if (value == -1) {
				enemy++;
			} else if (value == 2) {
				sequence++;
			} else {
				break;
			}
		}

		// Five
		if (sequence == 5) {
			return FIVE;
		}

		// Four
		if (emptySpaces == 1 && enemy == 0) {
			// Straight four
			if (shape[0] == 0 || shape[4] == 0) {
				return STRAIGHT_FOUR;
			}
			return FOUR;
		}

		// Three
		if (sequence == 3) {
			return THREE;
		}

		// Broken Three
		if (emptySpaces >= 1 && enemy <= 1 && emptySpaces <= 2) {
			return BROKEN_THREE;
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
