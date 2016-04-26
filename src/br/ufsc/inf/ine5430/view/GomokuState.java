package br.ufsc.inf.ine5430.view;

import br.ufsc.inf.ine5430.game.CPU;
import br.ufsc.inf.ine5430.game.Game;
import br.ufsc.inf.ine5430.game.Play;
import br.ufsc.inf.ine5430.game.Player;

/**
 * Interface to the GUI, wrapping solution modeling
 *
 */
public class GomokuState {
	private Game game;
	private Player currentPlayer;
	private Player winner;

	public static final int NONE = Game.NONE;
	public static final int BLACK = Game.BLACK;
	public static final int WHITE = Game.WHITE;

	public GomokuState(int size) {
		// For Human x Human version		
		// Player p1 = new Player("Human 1", Game.BLACK), p2 = new Player("Human 2", Game.WHITE);
		
		Player p1 = new Player("Human", Game.BLACK), p2 = new CPU("CPU", Game.WHITE);
		game = new Game(p1, p2);
		currentPlayer = p1;
	}

	public int getPiece(int row, int col) {
		return game.getPiece(row, col);
	}

	public int getWinner() {
		if(winner == null) {
			winner = game.getWinner();
		}
		
		if (winner != null) {
			return winner.getPieceType();
		} else {
			return NONE;
		}
	}

	public boolean playPiece(int row, int col) {
		if (row >= 15 || row < 0 || col >= 15 || col < 0 || getPiece(row, col) != NONE) {
			return false;
		}

		Play play = new Play(currentPlayer, row, col);
		game.makeAPlay(play);
		
		currentPlayer = game.getNextPlayer();
		
		if(getWinner() != NONE) {
			return true;
		}
		
		if(currentPlayer instanceof CPU) {
			game.makeAPlay(((CPU) currentPlayer).createAPlay(game));
			currentPlayer = game.getNextPlayer();
		}

		return true;
	}

	@Override
	public String toString() {
		return game.toString();
	}
}
