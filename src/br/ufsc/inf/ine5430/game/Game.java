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

		currentState = new GameState(turn, "Clear Board", null, new Piece[15][15], player1);
		possibleStates = new HashSet<>();
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
		//TODO implement
	}
	
	public boolean isAWin(GameState state) {
		//TODO implement
		return false;
	}
	
	@Override
	public boolean isLeafNode(Node node) {
		// TODO calculate next nodes
		return super.isLeafNode(node);
	}

}
