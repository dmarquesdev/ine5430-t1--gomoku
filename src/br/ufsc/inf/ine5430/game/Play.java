package br.ufsc.inf.ine5430.game;

public class Play {
	private Player player;
	private int x;
	private int y;

	public Play(Player player, int x, int y) {
		super();
		this.player = player;
		if (x >= 0 && x < 15)
			this.x = x;
		if (y >= 0 && y < 15)
			this.y = y;
	}

	public Player getPlayer() {
		return player;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((player == null) ? 0 : player.hashCode());
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Play other = (Play) obj;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player))
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Player: " + player.toString() + "\nRow: " + x + "\nColumn: " + y;
	}
}
