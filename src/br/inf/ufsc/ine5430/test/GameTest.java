package br.inf.ufsc.ine5430.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import br.ufsc.inf.ine5430.game.Game;
import br.ufsc.inf.ine5430.game.Play;
import br.ufsc.inf.ine5430.game.Player;

public class GameTest {

	private Game game;

	@Before
	public void setUp() throws Exception {
		Player p1 = new Player(1, "Human 1", Game.BLACK), p2 = new Player(2, "Human 2", Game.WHITE);
		game = new Game(p1, p2);
	}

	@Test
	public void verificarVitorialVertical() {
		Play play = new Play(game.getPlayer1(), 5, 7);
		game.makeAPlay(play);

		play = new Play(game.getNextPlayer(), 0, 0);
		game.makeAPlay(play);

		play = new Play(game.getNextPlayer(), 6, 7);
		game.makeAPlay(play);

		play = new Play(game.getNextPlayer(), 2, 6);
		game.makeAPlay(play);

		play = new Play(game.getNextPlayer(), 4, 7);
		game.makeAPlay(play);

		play = new Play(game.getNextPlayer(), 8, 4);
		game.makeAPlay(play);

		play = new Play(game.getNextPlayer(), 2, 7);
		game.makeAPlay(play);

		play = new Play(game.getNextPlayer(), 3, 9);
		game.makeAPlay(play);

		play = new Play(game.getNextPlayer(), 3, 7);
		game.makeAPlay(play);

		assertEquals(game.getPlayer1(), game.isAWin());
	}

}
