package br.ufsc.inf.ine5430.game;

import java.util.Arrays;
import java.util.Set;

import br.ufsc.inf.ine5430.graph.Edge;
import br.ufsc.inf.ine5430.graph.Node;

public class GameState extends Node {
	private Piece[][] board;
	private Player player;

	public GameState(int id, String label, Set<Edge> edges, Piece[][] board, Player player) {
		super(id, label, edges);
		this.board = board;
		this.player = player;
	}

	public Piece[][] getBoard() {
		return board;
	}

	public void setBoard(Piece[][] board) {
		this.board = board;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.deepHashCode(board);
		result = prime * result + ((player == null) ? 0 : player.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameState other = (GameState) obj;
		if (!Arrays.deepEquals(board, other.board))
			return false;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player))
			return false;
		return true;
	}

}
