package br.ufsc.inf.ine5430.game;

import java.util.HashSet;

import br.ufsc.inf.ine5430.graph.Edge;
import br.ufsc.inf.ine5430.graph.Node;

public class GameState extends Node {
	private int[][] board;
	private Player player;
	private Integer value;
	private int alfa;
	private int beta;

	public GameState(String label, HashSet<Edge> edges, GameState previous, int[][] board, Player player) {
		super(0, label, previous, edges);
		this.board = board;
		this.player = player;
	}

	public int[][] getBoard() {
		return board;
	}

	public void setBoard(int[][] board) {
		this.board = board;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getValue() {
		if (value == null) {
			// TODO choose between heuristics and utility and calculate
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

	public int[][] cloneBoard() {
		return cloneBoard(getBoard());
	}
	
	public static int[][] cloneBoard(int[][] board) {
		int[][] clone = new int[board.length][board[0].length];
		for(int i = 0; i < clone.length; i++) {
			for(int j = 0; j < clone[0].length; j++) {
				clone[i][j] = board[i][j];
			}
		}
		
		return clone;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(getLabel() + "\n");
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				switch (board[i][j]) {
				case Game.BLACK:
					sb.append("*");
					break;
				case Game.NONE:
					sb.append("-");
					break;
				case Game.WHITE:
					sb.append("o");
					break;
				}
			}
			sb.append("\n");
		}

		return sb.toString();
	}
}
