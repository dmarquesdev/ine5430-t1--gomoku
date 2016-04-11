package br.ufsc.inf.ine5430.game;

import java.util.HashSet;

import br.ufsc.inf.ine5430.graph.Edge;
import br.ufsc.inf.ine5430.graph.Node;

public class GameState extends Node {
	private Piece[][] board;
	private Player player;
	private Integer value;
	private int alfa;
	private int beta;

	public GameState(int id, String label, HashSet<Edge> edges, Piece[][] board, Player player) {
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

	public int getValue() {
		if(value == null) {
			//TODO choose between heuristics and utility and calculate
			value = 0;
		}
		return value;
	}

	public int getAlfa() {
		return alfa;
	}

	public void setAlfa(int alfa) {
		this.alfa = alfa;
	}

	public int getBeta() {
		return beta;
	}

	public void setBeta(int beta) {
		this.beta = beta;
	}

}
