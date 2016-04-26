package br.ufsc.inf.ine5430.game;

import java.util.HashSet;

public class CPU extends Player {

	enum PlayerType {
		HUMAN, CPU;
	}

	public CPU(int id, String name, int pieceType) {
		super(id, name, pieceType);
	}

	public Play createAPlay(Game game) {
		int[] minimaxResult = minimax(game, 2, PlayerType.CPU, Integer.MIN_VALUE, Integer.MAX_VALUE);
		Play bestPlay = new Play(this, minimaxResult[1], minimaxResult[2]);
		return bestPlay;
	}

	private int[] minimax(Game game, int depth, PlayerType player, int alfa, int beta) {
		HashSet<Play> possibleMoves = game.generateMoves();

		int score;
		int bestRow = -1;
		int bestColumn = -1;

		if (possibleMoves.isEmpty() || depth == 0) {
			game.calculateValue(game.getCurrentState());
			score = game.getCurrentState().getValue();
			return new int[] { score, bestRow, bestColumn };
		} else {
			GameState currentState = game.getCurrentState();
			int turn = game.getTurn();
			Play lastPlay = game.getLastPlay();

			for (Play play : possibleMoves) {
				game.makeAPlay(play);
				if (player == PlayerType.CPU) {
					score = minimax(game, depth - 1, getNextPlayer(player), alfa, beta)[0];
					if (score > alfa) {
						alfa = score;
						bestRow = play.getX();
						bestColumn = play.getY();
					}
				} else {
					score = minimax(game, depth - 1, getNextPlayer(player), alfa, beta)[0];
					if (score < beta) {
						beta = score;
						bestRow = play.getX();
						bestColumn = play.getY();
					}
				}
				game.setCurrentState(currentState);
				game.setTurn(turn);
				game.setLastPlay(lastPlay);
				game.getPlayedPlaces().remove(new int[]{play.getX(), play.getY()});
				if (alfa >= beta)
					break;
			}

			return new int[] { (player == PlayerType.CPU) ? alfa : beta, bestRow, bestColumn };
		}
	}

	private PlayerType getNextPlayer(PlayerType player) {
		return (player == PlayerType.CPU) ? PlayerType.HUMAN : PlayerType.CPU;
	}
}
