package br.ufsc.inf.ine5430.game;

import br.ufsc.inf.ine5430.graph.Edge;

public class CPU extends Player {

	enum PlayerType {
		HUMAN(Game.BLACK), CPU(Game.WHITE);
		
		private PlayerType(int piece) {
			this.piece = piece;
		}
		
		private int piece;
		
		public int getPiece() {
			return piece;
		}
	}

	public CPU(int id, String name, int pieceType) {
		super(id, name, pieceType);
	}

	public Play createAPlay(Game game) {
		long[] minimaxResult = minimax(game, 2, PlayerType.CPU, Long.MIN_VALUE, Long.MAX_VALUE);
		Play bestPlay = new Play(this, (int) minimaxResult[1], (int) minimaxResult[2]);
		System.out.println("Place: {" + minimaxResult[1] + ", " + minimaxResult[2] + "}, Value: " + minimaxResult[0]);
		return bestPlay;
	}

	private long[] minimax(Game game, int depth, PlayerType player, long alfa, long beta) {
		game.generateMoves(player);

		long score;
		int bestRow = -1;
		int bestColumn = -1;

		if (game.getCurrentState().getEdges().isEmpty() || depth == 0) {
			game.calculateValue(game.getCurrentState(), player);
			score = game.getCurrentState().getValue();
			return new long[] { score, bestRow, bestColumn };
		} else {
			for (Edge edge : game.getCurrentState().getEdges()) {
				GameState currentState = game.getCurrentState();
				game.setCurrentState((GameState) edge.getNode());
				if (player == PlayerType.CPU) {
					score = minimax(game, depth - 1, getNextPlayer(player), alfa, beta)[0];
					if (score > alfa) {
						alfa = score;
						bestRow = ((Play) edge.getValue()).getX();
						bestColumn = ((Play) edge.getValue()).getY();
					}
				} else {
					score = minimax(game, depth - 1, getNextPlayer(player), alfa, beta)[0];
					if (score < beta) {
						beta = score;
						bestRow = ((Play) edge.getValue()).getX();
						bestColumn = ((Play) edge.getValue()).getY();
					}
				}
				game.setCurrentState(currentState);
				if (alfa >= beta)
					break;
			}

			return new long[] { (player == PlayerType.CPU) ? alfa : beta, bestRow, bestColumn };
		}
	}

	private PlayerType getNextPlayer(PlayerType player) {
		return (player == PlayerType.CPU) ? PlayerType.HUMAN : PlayerType.CPU;
	}
}
